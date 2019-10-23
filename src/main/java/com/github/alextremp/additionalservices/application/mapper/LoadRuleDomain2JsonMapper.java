package com.github.alextremp.additionalservices.application.mapper;

import com.github.alextremp.additionalservices.application.dto.BooleanOperatorJson;
import com.github.alextremp.additionalservices.application.dto.LeftRightOperatorJson;
import com.github.alextremp.additionalservices.application.dto.LoadRuleJson;
import com.github.alextremp.additionalservices.domain.additionalservice.loadrule.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public class LoadRuleDomain2JsonMapper implements Mapper<LoadRule, LoadRuleJson> {

  private final FluentMap<String, SubLoadRuleMapper> subClassMappers;
  private final DataExtractor2DataValueJsonMapper dataExtractor2DataValueJsonMapper;

  public LoadRuleDomain2JsonMapper(DataExtractor2DataValueJsonMapper dataExtractor2DataValueJsonMapper) {
    final LoadRuleDomain2JsonMapper refThis = this;
    this.dataExtractor2DataValueJsonMapper = dataExtractor2DataValueJsonMapper;
    this.subClassMappers = new FluentMap<String, SubLoadRuleMapper>() {{
      put(AndLoadRule.class.getName(), new ListLoadRuleMapper(refThis, BooleanOperatorJson.AND));
      put(OrLoadRule.class.getName(), new ListLoadRuleMapper(refThis, BooleanOperatorJson.OR));
      put(EqualLoadRule.class.getName(), new ComparisonLoadRuleMapper(BooleanOperatorJson.EQUAL));
      put(LessThanLoadRule.class.getName(), new ComparisonLoadRuleMapper(BooleanOperatorJson.LESS_THAN));
      put(GreaterThanLoadRule.class.getName(), new ComparisonLoadRuleMapper(BooleanOperatorJson.GREATER_THAN));
      put(NotLoadRule.class.getName(), new NotLoadRuleMapper(refThis));
      put(InLoadRule.class.getName(), new InLoadRuleMapper(refThis));
    }};
  }

  @Override
  public Mono<LoadRuleJson> map(LoadRule loadRule) {
    return Mono.fromCallable(() -> new LoadRuleJson())
        .flatMap(dto -> map(loadRule, dto));
  }

  private Mono<LoadRuleJson> map(LoadRule loadRule, LoadRuleJson loadRuleJson) {
    return Mono.just(loadRuleJson)
        .map(dto -> subClassMappers.getNotNull(loadRule.getClass().getName()))
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
      return Mono.just((ListLoadRule) loadRule)
          .map(listLoadRule -> listLoadRule.loadRules())
          .flatMap(this::toLoadRuleJsonList)
          .doOnNext(list -> {
            switch (operator) {
              case AND:
                loadRuleJson.setAnd(list);
                break;
              case OR:
                loadRuleJson.setOr(list);
                break;
              default:
                throw new IllegalArgumentException("List operator accepted: " + operator);
            }
          })
          .map(loadRuleJson::setOr)
          .map(dto -> dto.setOperator(operator));
    }

    private Mono<List<LoadRuleJson>> toLoadRuleJsonList(List<LoadRule> loadRules) {
      return Flux.fromIterable(loadRules)
          .flatMap(loadRuleDomain2JsonMapper::map)
          .collectList();
    }
  }

  private class ComparisonLoadRuleMapper implements SubLoadRuleMapper {

    private final BooleanOperatorJson operator;

    private ComparisonLoadRuleMapper(BooleanOperatorJson operator) {
      this.operator = operator;
    }

    @Override
    public Mono<LoadRuleJson> map(LoadRule loadRule, LoadRuleJson loadRuleJson) {
      return Mono.just((ComparisonLoadRule) loadRule)
          .flatMap(this::toLeftRightOperatorJson)
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

    private Mono<LeftRightOperatorJson> toLeftRightOperatorJson(ComparisonLoadRule comparisonLoadRule) {
      return Mono.zip(
          dataExtractor2DataValueJsonMapper.map(comparisonLoadRule.leftDataExtractor()),
          dataExtractor2DataValueJsonMapper.map(comparisonLoadRule.rightDataExtractor())
      ).map(tuple -> new LeftRightOperatorJson(tuple.getT1(), tuple.getT2()));
    }
  }

  private class NotLoadRuleMapper implements SubLoadRuleMapper {
    private final LoadRuleDomain2JsonMapper loadRuleDomain2JsonMapper;

    private NotLoadRuleMapper(LoadRuleDomain2JsonMapper loadRuleDomain2JsonMapper) {
      this.loadRuleDomain2JsonMapper = loadRuleDomain2JsonMapper;
    }

    @Override
    public Mono<LoadRuleJson> map(LoadRule loadRule, LoadRuleJson loadRuleJson) {
      return Mono.just((NotLoadRule) loadRule)
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
      return Mono.just((InLoadRule) loadRule)
          // TODO: missing data extractor mapper
          .map(dto -> loadRuleJson.setOperator(BooleanOperatorJson.IN));
    }
  }
}
