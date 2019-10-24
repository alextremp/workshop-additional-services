package com.github.alextremp.additionalservices.application.mapper;

import com.github.alextremp.additionalservices.application.dto.BooleanOperatorJson;
import com.github.alextremp.additionalservices.application.dto.LoadRuleJson;
import com.github.alextremp.additionalservices.domain.additionalservice.loadrule.*;
import reactor.core.publisher.Mono;

import static com.github.alextremp.additionalservices.application.dto.BooleanOperatorJson.*;

public class LoadRuleJson2DomainMapper implements Mapper<LoadRuleJson, LoadRule> {

  private final DataValueJson2DataExtractorMapper dataValueJson2DataExtractorMapper;
  private final FluentMap<BooleanOperatorJson, OperatorMapper> operatorMappers;

  public LoadRuleJson2DomainMapper(DataValueJson2DataExtractorMapper dataValueJson2DataExtractorMapper) {
    this.dataValueJson2DataExtractorMapper = dataValueJson2DataExtractorMapper;
    this.operatorMappers = new FluentMap<BooleanOperatorJson, OperatorMapper>()
        .fluentPut(AND, new AndOperatorMapper(this))
        .fluentPut(OR, new OrOperatorMapper(this))
        .fluentPut(NOT, new NotOperatorMapper(this))
        .fluentPut(IN, new InOperatorMapper())
        .fluentPut(EQUAL, new EqualOperatorMapper())
        .fluentPut(LESS_THAN, new LessThanOperatorMapper())
        .fluentPut(GREATER_THAN, new GreaterThanOperatorMapper());
  }

  @Override
  public Mono<LoadRule> map(LoadRuleJson loadRuleJson) {
    return Mono.fromCallable(() -> operatorMappers.getNotNull(loadRuleJson.getOperator()))
        .flatMap(operatorMapper -> operatorMapper.map(loadRuleJson));
  }

  private interface OperatorMapper extends Mapper<LoadRuleJson, LoadRule> {
  }

  private class AndOperatorMapper implements OperatorMapper {
    private final LoadRuleJson2DomainMapper loadRuleJson2DomainMapper;

    public AndOperatorMapper(LoadRuleJson2DomainMapper loadRuleJson2DomainMapper) {
      this.loadRuleJson2DomainMapper = loadRuleJson2DomainMapper;
    }

    @Override
    public Mono<LoadRule> map(LoadRuleJson loadRuleJson) {
      return Mono.fromCallable(() -> loadRuleJson.getAnd())
          .flatMapIterable(loadRuleJsons -> loadRuleJsons)
          .flatMap(loadRuleJson2DomainMapper::map)
          .collectList()
          .map(AndLoadRule::new);
    }
  }

  private class OrOperatorMapper implements OperatorMapper {
    private final LoadRuleJson2DomainMapper loadRuleJson2DomainMapper;

    public OrOperatorMapper(LoadRuleJson2DomainMapper loadRuleJson2DomainMapper) {
      this.loadRuleJson2DomainMapper = loadRuleJson2DomainMapper;
    }

    @Override
    public Mono<LoadRule> map(LoadRuleJson loadRuleJson) {
      return Mono.fromCallable(() -> loadRuleJson.getOr())
          .flatMapIterable(loadRuleJsons -> loadRuleJsons)
          .flatMap(loadRuleJson2DomainMapper::map)
          .collectList()
          .map(OrLoadRule::new);
    }
  }

  private class NotOperatorMapper implements OperatorMapper {
    private final LoadRuleJson2DomainMapper loadRuleJson2DomainMapper;

    public NotOperatorMapper(LoadRuleJson2DomainMapper loadRuleJson2DomainMapper) {
      this.loadRuleJson2DomainMapper = loadRuleJson2DomainMapper;
    }

    @Override
    public Mono<LoadRule> map(LoadRuleJson loadRuleJson) {
      return Mono.fromCallable(() -> loadRuleJson.getNot())
          .flatMap(loadRuleJson2DomainMapper::map)
          .map(NotLoadRule::new);
    }
  }

  private class InOperatorMapper implements OperatorMapper {
    @Override
    public Mono<LoadRule> map(LoadRuleJson loadRuleJson) {
      return Mono.fromCallable(() -> loadRuleJson.getIn())
          .flatMap(inOperatorJson -> Mono.zip(
              dataValueJson2DataExtractorMapper.map(inOperatorJson.getData()),
              Mono.just(inOperatorJson.getCollection())
          ))
          .map(tuple -> new InLoadRule(tuple.getT1(), tuple.getT2()));
    }
  }

  private class EqualOperatorMapper implements OperatorMapper {
    @Override
    public Mono<LoadRule> map(LoadRuleJson loadRuleJson) {
      return Mono.fromCallable(() -> loadRuleJson.getEqual())
          .flatMap(operator -> Mono.zip(
              dataValueJson2DataExtractorMapper.map(operator.getLeft()),
              dataValueJson2DataExtractorMapper.map(operator.getRight())
          ))
          .map(tuple -> new EqualLoadRule(tuple.getT1(), tuple.getT2()));
    }
  }

  private class LessThanOperatorMapper implements OperatorMapper {
    @Override
    public Mono<LoadRule> map(LoadRuleJson loadRuleJson) {
      return Mono.fromCallable(() -> loadRuleJson.getLessThan())
          .flatMap(operator -> Mono.zip(
              dataValueJson2DataExtractorMapper.map(operator.getLeft()),
              dataValueJson2DataExtractorMapper.map(operator.getRight())
          ))
          .map(tuple -> new LessThanLoadRule(tuple.getT1(), tuple.getT2()));
    }
  }

  private class GreaterThanOperatorMapper implements OperatorMapper {
    @Override
    public Mono<LoadRule> map(LoadRuleJson loadRuleJson) {
      return Mono.fromCallable(() -> loadRuleJson.getGreaterThan())
          .flatMap(operator -> Mono.zip(
              dataValueJson2DataExtractorMapper.map(operator.getLeft()),
              dataValueJson2DataExtractorMapper.map(operator.getRight())
          ))
          .map(tuple -> new GreaterThanLoadRule(tuple.getT1(), tuple.getT2()));
    }
  }
}
