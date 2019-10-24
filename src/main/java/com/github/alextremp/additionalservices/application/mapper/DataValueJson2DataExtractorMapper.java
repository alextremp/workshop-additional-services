package com.github.alextremp.additionalservices.application.mapper;

import com.github.alextremp.additionalservices.application.dto.CalcJson;
import com.github.alextremp.additionalservices.application.dto.CalcOperatorJson;
import com.github.alextremp.additionalservices.application.dto.DataValueJson;
import com.github.alextremp.additionalservices.application.dto.SourceJson;
import com.github.alextremp.additionalservices.domain.additionalservice.dataextractor.*;
import reactor.core.publisher.Mono;

import java.util.Objects;

import static com.github.alextremp.additionalservices.application.dto.CalcOperatorJson.*;
import static com.github.alextremp.additionalservices.application.dto.SourceJson.*;

public class DataValueJson2DataExtractorMapper implements Mapper<DataValueJson, DataExtractor> {

  private final FluentMap<SourceJson, SourceMapper> sourceMappers;
  private final FluentMap<String, PlatformDataExtractorFactory> platformDataExtractorFactories;
  private final FluentMap<CalcOperatorJson, CalcDataExtractorFactory> calcDataExtractorFactories;

  public DataValueJson2DataExtractorMapper() {
    this.sourceMappers = new FluentMap<SourceJson, SourceMapper>()
        .fluentPut(DATALAYER, new DatalayerMapper())
        .fluentPut(VALUE, new ValueMapper())
        .fluentPut(PLATFORM, new PlatformMapper())
        .fluentPut(CALC, new CalcMapper(this));

    this.platformDataExtractorFactories = new FluentMap<String, PlatformDataExtractorFactory>()
        .fluentPut(PlatformYearDataExtractor.PLATFORM_KEY, new PlatformYearFactory());

    this.calcDataExtractorFactories = new FluentMap<CalcOperatorJson, CalcDataExtractorFactory>()
        .fluentPut(ADD, new AddCalcDataExtractorFactory())
        .fluentPut(SUB, new SubstractCalcDataExtractorFactory())
        .fluentPut(MUL, new MultiplyCalcDataExtractorFactory())
        .fluentPut(DIV, new DivideCalcDataExtractorFactory());
  }

  @Override
  public Mono<DataExtractor> map(DataValueJson dataValueJson) {
    return Mono.fromCallable(() -> sourceMappers.getNotNull(dataValueJson.getSource()))
        .flatMap(sourceMapper -> sourceMapper.map(dataValueJson));
  }

  private interface SourceMapper extends Mapper<DataValueJson, DataExtractor> {
  }

  private interface PlatformDataExtractorFactory {
    PlatformDataExtractor create();
  }

  private interface CalcDataExtractorFactory {
    CalcDataExtractor create(DataExtractor left, DataExtractor right);
  }


  private class DatalayerMapper implements SourceMapper {
    @Override
    public Mono<DataExtractor> map(DataValueJson dataValueJson) {
      return Mono.fromCallable(() -> dataValueJson.getDatalayer())
          .map(DatalayerDataExtractor::new);
    }
  }

  private class ValueMapper implements SourceMapper {
    @Override
    public Mono<DataExtractor> map(DataValueJson dataValueJson) {
      return Mono.fromCallable(() -> dataValueJson.getValue())
          .map(FixedValueDataExtractor::new);
    }
  }

  private class PlatformMapper implements SourceMapper {
    @Override
    public Mono<DataExtractor> map(DataValueJson dataValueJson) {
      return Mono.fromCallable(() -> platformDataExtractorFactories.getNotNull(dataValueJson.getPlatform()))
          .map(PlatformDataExtractorFactory::create);
    }
  }

  private class CalcMapper implements SourceMapper {
    private final DataValueJson2DataExtractorMapper dataValueJson2DataExtractorMapper;

    private CalcMapper(DataValueJson2DataExtractorMapper dataValueJson2DataExtractorMapper) {
      this.dataValueJson2DataExtractorMapper = dataValueJson2DataExtractorMapper;
    }

    @Override
    public Mono<DataExtractor> map(DataValueJson dataValueJson) {
      return Mono.fromCallable(() -> validatedCalc(dataValueJson))
          .flatMap(calcJson -> Mono.zip(
              Mono.just(calcJson.getOperator()),
              dataValueJson2DataExtractorMapper.map(calcJson.getLeft()),
              dataValueJson2DataExtractorMapper.map(calcJson.getRight())
          ))
          .map(tuple -> calcDataExtractorFactories.getNotNull(tuple.getT1())
              .create(tuple.getT2(), tuple.getT3())
          );
    }

    private CalcJson validatedCalc(DataValueJson dataValueJson) {
      Objects.requireNonNull(dataValueJson.getCalc(), "Calc cannot be null");
      Objects.requireNonNull(dataValueJson.getCalc().getOperator(), "Operator is required");
      Objects.requireNonNull(dataValueJson.getCalc().getLeft(), "Left is required");
      Objects.requireNonNull(dataValueJson.getCalc().getRight(), "Right is required");
      return dataValueJson.getCalc();
    }
  }

  private class PlatformYearFactory implements PlatformDataExtractorFactory {
    @Override
    public PlatformDataExtractor create() {
      return new PlatformYearDataExtractor();
    }
  }

  private class AddCalcDataExtractorFactory implements CalcDataExtractorFactory {
    @Override
    public CalcDataExtractor create(DataExtractor left, DataExtractor right) {
      return new AddDataExtractor(left, right);
    }
  }

  private class SubstractCalcDataExtractorFactory implements CalcDataExtractorFactory {
    @Override
    public CalcDataExtractor create(DataExtractor left, DataExtractor right) {
      return new SubstractDataExtractor(left, right);
    }
  }

  private class MultiplyCalcDataExtractorFactory implements CalcDataExtractorFactory {
    @Override
    public CalcDataExtractor create(DataExtractor left, DataExtractor right) {
      return new MultiplyDataExtractor(left, right);
    }
  }

  private class DivideCalcDataExtractorFactory implements CalcDataExtractorFactory {
    @Override
    public CalcDataExtractor create(DataExtractor left, DataExtractor right) {
      return new DivideDataExtractor(left, right);
    }
  }
}
