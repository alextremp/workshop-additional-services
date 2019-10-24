package com.github.alextremp.additionalservices.application.mapper;

import com.github.alextremp.additionalservices.application.dto.BooleanOperatorJson;
import com.github.alextremp.additionalservices.application.dto.InOperatorJson;
import com.github.alextremp.additionalservices.application.dto.LeftRightOperatorJson;
import com.github.alextremp.additionalservices.application.dto.LoadRuleJson;
import com.github.alextremp.additionalservices.domain.additionalservice.loadrule.*;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.function.Function;

import static com.github.alextremp.additionalservices.application.dto.BooleanOperatorJson.*;

public class LoadRuleDomain2JsonMapper implements Mapper<LoadRule, LoadRuleJson> {

  private final FluentMap<Class<? extends LoadRule>, SubLoadRuleMapper> subClassMappers;
  private final DataExtractor2DataValueJsonMapper dataExtractor2DataValueJsonMapper;

  public LoadRuleDomain2JsonMapper(DataExtractor2DataValueJsonMapper dataExtractor2DataValueJsonMapper) {
    this.dataExtractor2DataValueJsonMapper = dataExtractor2DataValueJsonMapper;

    ListLoadRuleMapper listLoadRuleMapper = new ListLoadRuleMapper(this);
    ComparisonLoadRuleMapper comparisonLoadRuleMapper = new ComparisonLoadRuleMapper();

    this.subClassMappers = new FluentMap<Class<? extends LoadRule>, SubLoadRuleMapper>()
        .fluentPut(AndLoadRule.class, listLoadRuleMapper)
        .fluentPut(OrLoadRule.class, listLoadRuleMapper)
        .fluentPut(EqualLoadRule.class, comparisonLoadRuleMapper)
        .fluentPut(LessThanLoadRule.class, comparisonLoadRuleMapper)
        .fluentPut(GreaterThanLoadRule.class, comparisonLoadRuleMapper)
        .fluentPut(NotLoadRule.class, new NotLoadRuleMapper(this))
        .fluentPut(InLoadRule.class, new InLoadRuleMapper());
  }

  @Override
  public Mono<LoadRuleJson> map(LoadRule loadRule) {
    return Mono.fromCallable(() -> subClassMappers.getNotNull(loadRule.getClass()))
        .flatMap(subLoadRuleMapper -> subLoadRuleMapper.map(loadRule));
  }

  private interface SubLoadRuleMapper extends Mapper<LoadRule, LoadRuleJson> {
  }

  private class ListLoadRuleMapper implements SubLoadRuleMapper {

    private final LoadRuleDomain2JsonMapper loadRuleDomain2JsonMapper;
    private final FluentMap<Class<? extends ListLoadRule>, Function<List<LoadRuleJson>, LoadRuleJson>> operatorSetters;

    private ListLoadRuleMapper(LoadRuleDomain2JsonMapper loadRuleDomain2JsonMapper) {
      this.loadRuleDomain2JsonMapper = loadRuleDomain2JsonMapper;
      this.operatorSetters = new FluentMap<Class<? extends ListLoadRule>, Function<List<LoadRuleJson>, LoadRuleJson>>()
          .fluentPut(AndLoadRule.class, loadRuleJsons -> setAnd(loadRuleJsons))
          .fluentPut(OrLoadRule.class, loadRuleJsons -> setOr(loadRuleJsons));
    }

    @Override
    public Mono<LoadRuleJson> map(LoadRule loadRule) {
      return Mono.fromCallable(() -> (ListLoadRule) loadRule)
          .flatMapIterable(listLoadRule -> listLoadRule.loadRules())
          .flatMap(loadRuleDomain2JsonMapper::map)
          .collectList()
          .map(list -> operatorSetters.getNotNull(loadRule.getClass()).apply(list));
    }

    private LoadRuleJson setAnd(List<LoadRuleJson> loadRuleJsons) {
      return new LoadRuleJson()
          .setOperator(AND)
          .setAnd(loadRuleJsons);
    }

    private LoadRuleJson setOr(List<LoadRuleJson> loadRuleJsons) {
      return new LoadRuleJson()
          .setOperator(OR)
          .setOr(loadRuleJsons);
    }
  }

  private class ComparisonLoadRuleMapper implements SubLoadRuleMapper {

    private final FluentMap<Class<? extends ComparisonLoadRule>, Function<LeftRightOperatorJson, LoadRuleJson>> operatorSetters;

    private ComparisonLoadRuleMapper() {
      this.operatorSetters = new FluentMap<Class<? extends ComparisonLoadRule>, Function<LeftRightOperatorJson, LoadRuleJson>>()
          .fluentPut(EqualLoadRule.class, leftRightOperatorJson -> setEqual(leftRightOperatorJson))
          .fluentPut(LessThanLoadRule.class, leftRightOperatorJson -> setLessThan(leftRightOperatorJson))
          .fluentPut(GreaterThanLoadRule.class, leftRightOperatorJson -> setGreaterThan(leftRightOperatorJson));
    }

    @Override
    public Mono<LoadRuleJson> map(LoadRule loadRule) {
      return Mono.fromCallable(() -> (ComparisonLoadRule) loadRule)
          .flatMap(comparisonLoadRule -> Mono.zip(
              dataExtractor2DataValueJsonMapper.map(comparisonLoadRule.leftDataExtractor()),
              dataExtractor2DataValueJsonMapper.map(comparisonLoadRule.rightDataExtractor())
          ))
          .map(tuple -> new LeftRightOperatorJson(tuple.getT1(), tuple.getT2()))
          .map(leftRightOperatorJson -> operatorSetters.getNotNull(loadRule.getClass()).apply(leftRightOperatorJson));
    }

    private LoadRuleJson setEqual(LeftRightOperatorJson leftRightOperatorJson) {
      return new LoadRuleJson()
          .setOperator(EQUAL)
          .setEqual(leftRightOperatorJson);
    }

    private LoadRuleJson setLessThan(LeftRightOperatorJson leftRightOperatorJson) {
      return new LoadRuleJson()
          .setOperator(LESS_THAN)
          .setLessThan(leftRightOperatorJson);
    }

    private LoadRuleJson setGreaterThan(LeftRightOperatorJson leftRightOperatorJson) {
      return new LoadRuleJson()
          .setOperator(GREATER_THAN)
          .setGreaterThan(leftRightOperatorJson);
    }
  }

  private class NotLoadRuleMapper implements SubLoadRuleMapper {
    private final LoadRuleDomain2JsonMapper loadRuleDomain2JsonMapper;

    private NotLoadRuleMapper(LoadRuleDomain2JsonMapper loadRuleDomain2JsonMapper) {
      this.loadRuleDomain2JsonMapper = loadRuleDomain2JsonMapper;
    }

    @Override
    public Mono<LoadRuleJson> map(LoadRule loadRule) {
      return Mono.fromCallable(() -> (NotLoadRule) loadRule)
          .map(notLoadRule -> notLoadRule.loadRule())
          .flatMap(loadRuleDomain2JsonMapper::map)
          .map(loadRuleJson -> new LoadRuleJson()
              .setOperator(BooleanOperatorJson.NOT)
              .setNot(loadRuleJson)
          );
    }
  }

  private class InLoadRuleMapper implements SubLoadRuleMapper {

    private InLoadRuleMapper() {
    }

    @Override
    public Mono<LoadRuleJson> map(LoadRule loadRule) {
      return Mono.fromCallable(() -> (InLoadRule) loadRule)
          .flatMap(inLoadRule -> Mono.zip(
              dataExtractor2DataValueJsonMapper.map(inLoadRule.dataExtractor()),
              Mono.just(inLoadRule.collection())
          ))
          .map(tuple -> new InOperatorJson(tuple.getT1(), tuple.getT2()))
          .map(inOperatorJson -> new LoadRuleJson()
              .setOperator(BooleanOperatorJson.IN)
              .setIn(inOperatorJson)
          );
    }
  }
}
