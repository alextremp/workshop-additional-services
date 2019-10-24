package com.github.alextremp.additionalservices.application.mapper;

import com.github.alextremp.additionalservices.application.dto.BooleanOperatorJson;
import com.github.alextremp.additionalservices.application.dto.InOperatorJson;
import com.github.alextremp.additionalservices.application.dto.LeftRightOperatorJson;
import com.github.alextremp.additionalservices.application.dto.LoadRuleJson;
import com.github.alextremp.additionalservices.domain.additionalservice.loadrule.*;
import reactor.core.publisher.Mono;

public class LoadRuleDomain2JsonMapper implements Mapper<LoadRule, LoadRuleJson> {

  private final FluentMap<Class<? extends LoadRule>, SubLoadRuleMapper> subClassMappers;
  private final DataExtractor2DataValueJsonMapper dataExtractor2DataValueJsonMapper;

  public LoadRuleDomain2JsonMapper(DataExtractor2DataValueJsonMapper dataExtractor2DataValueJsonMapper) {
    this.dataExtractor2DataValueJsonMapper = dataExtractor2DataValueJsonMapper;
    this.subClassMappers = new FluentMap<Class<? extends LoadRule>, SubLoadRuleMapper>()
        .fluentPut(AndLoadRule.class, new ListLoadRuleMapper(this, BooleanOperatorJson.AND))
        .fluentPut(OrLoadRule.class, new ListLoadRuleMapper(this, BooleanOperatorJson.OR))
        .fluentPut(EqualLoadRule.class, new ComparisonLoadRuleMapper(BooleanOperatorJson.EQUAL))
        .fluentPut(LessThanLoadRule.class, new ComparisonLoadRuleMapper(BooleanOperatorJson.LESS_THAN))
        .fluentPut(GreaterThanLoadRule.class, new ComparisonLoadRuleMapper(BooleanOperatorJson.GREATER_THAN))
        .fluentPut(NotLoadRule.class, new NotLoadRuleMapper(this))
        .fluentPut(InLoadRule.class, new InLoadRuleMapper(this));
  }

  @Override
  public Mono<LoadRuleJson> map(LoadRule loadRule) {
    return Mono.fromCallable(() -> new LoadRuleJson())
        .flatMap(dto -> map(loadRule, dto));
  }

  private Mono<LoadRuleJson> map(LoadRule loadRule, LoadRuleJson loadRuleJson) {
    return Mono.fromCallable(() -> subClassMappers.getNotNull(loadRule.getClass()))
        .flatMap(subLoadRuleMapper -> subLoadRuleMapper.map(loadRule, loadRuleJson));
  }

  private interface SubLoadRuleMapper {
    Mono<LoadRuleJson> map(LoadRule loadRule, LoadRuleJson dto);
  }

  private class ListLoadRuleMapper implements SubLoadRuleMapper {

    private final LoadRuleDomain2JsonMapper loadRuleDomain2JsonMapper;
    private final BooleanOperatorJson operator;

    private ListLoadRuleMapper(LoadRuleDomain2JsonMapper loadRuleDomain2JsonMapper, BooleanOperatorJson operator) {
      this.loadRuleDomain2JsonMapper = loadRuleDomain2JsonMapper;
      this.operator = operator;
    }

    @Override
    public Mono<LoadRuleJson> map(LoadRule loadRule, LoadRuleJson loadRuleJson) {
      return Mono.fromCallable(() -> (ListLoadRule) loadRule)
          .flatMapIterable(listLoadRule -> listLoadRule.loadRules())
          .flatMap(loadRuleDomain2JsonMapper::map)
          .collectList()
          .map(list -> {
            switch (operator) {
              case AND:
                return loadRuleJson.setAnd(list);
              case OR:
                return loadRuleJson.setOr(list);
              default:
                throw new IllegalArgumentException("List operator accepted: " + operator);
            }
          })
          .map(dto -> dto.setOperator(operator));
    }
  }

  private class ComparisonLoadRuleMapper implements SubLoadRuleMapper {

    private final BooleanOperatorJson operator;

    private ComparisonLoadRuleMapper(BooleanOperatorJson operator) {
      this.operator = operator;
    }

    @Override
    public Mono<LoadRuleJson> map(LoadRule loadRule, LoadRuleJson loadRuleJson) {
      return Mono.fromCallable(() -> (ComparisonLoadRule) loadRule)
          .flatMap(comparisonLoadRule -> Mono.zip(
              dataExtractor2DataValueJsonMapper.map(comparisonLoadRule.leftDataExtractor()),
              dataExtractor2DataValueJsonMapper.map(comparisonLoadRule.rightDataExtractor())
          ))
          .map(tuple -> new LeftRightOperatorJson(tuple.getT1(), tuple.getT2()))
          .doOnNext(leftRightOperatorJson -> {
            switch (operator) {
              case EQUAL:
                loadRuleJson.setEqual(leftRightOperatorJson);
                break;
              case LESS_THAN:
                loadRuleJson.setLessThan(leftRightOperatorJson);
                break;
              case GREATER_THAN:
                loadRuleJson.setGreaterThan(leftRightOperatorJson);
                break;
              default:
                throw new IllegalArgumentException("Comparison operator accepted: " + operator);
            }
          })
          .map(dto -> loadRuleJson.setOperator(operator));
    }
  }

  private class NotLoadRuleMapper implements SubLoadRuleMapper {
    private final LoadRuleDomain2JsonMapper loadRuleDomain2JsonMapper;

    private NotLoadRuleMapper(LoadRuleDomain2JsonMapper loadRuleDomain2JsonMapper) {
      this.loadRuleDomain2JsonMapper = loadRuleDomain2JsonMapper;
    }

    @Override
    public Mono<LoadRuleJson> map(LoadRule loadRule, LoadRuleJson loadRuleJson) {
      return Mono.fromCallable(() -> (NotLoadRule) loadRule)
          .map(notLoadRule -> notLoadRule.loadRule())
          .flatMap(loadRuleDomain2JsonMapper::map)
          .map(loadRuleJson::setNot)
          .map(dto -> loadRuleJson.setOperator(BooleanOperatorJson.NOT));
    }
  }

  private class InLoadRuleMapper implements SubLoadRuleMapper {
    private final LoadRuleDomain2JsonMapper loadRuleDomain2JsonMapper;

    private InLoadRuleMapper(LoadRuleDomain2JsonMapper loadRuleDomain2JsonMapper) {
      this.loadRuleDomain2JsonMapper = loadRuleDomain2JsonMapper;
    }

    @Override
    public Mono<LoadRuleJson> map(LoadRule loadRule, LoadRuleJson loadRuleJson) {
      return Mono.fromCallable(() -> (InLoadRule) loadRule)
          .flatMap(inLoadRule -> Mono.zip(
              dataExtractor2DataValueJsonMapper.map(inLoadRule.dataExtractor()),
              Mono.just(inLoadRule.collection())
          ))
          .map(tuple -> new InOperatorJson(tuple.getT1(), tuple.getT2()))
          .map(loadRuleJson::setIn)
          .map(dto -> loadRuleJson.setOperator(BooleanOperatorJson.IN));
    }
  }
}
