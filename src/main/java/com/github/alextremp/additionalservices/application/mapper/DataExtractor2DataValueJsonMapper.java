package com.github.alextremp.additionalservices.application.mapper;

import com.github.alextremp.additionalservices.application.dto.CalcJson;
import com.github.alextremp.additionalservices.application.dto.CalcOperatorJson;
import com.github.alextremp.additionalservices.application.dto.DataValueJson;
import com.github.alextremp.additionalservices.application.dto.SourceJson;
import com.github.alextremp.additionalservices.domain.additionalservice.dataextractor.*;
import reactor.core.publisher.Mono;

public class DataExtractor2DataValueJsonMapper implements Mapper<DataExtractor, DataValueJson> {

  private final FluentMap<Class<? extends DataExtractor>, SubDataExtractorMapper> subDataExtractorMappers;

  public DataExtractor2DataValueJsonMapper() {
    this.subDataExtractorMappers = new FluentMap<Class<? extends DataExtractor>, SubDataExtractorMapper>()
        .fluentPut(AddDataExtractor.class, new CalcDataExtractorMapper(this, CalcOperatorJson.ADD))
        .fluentPut(SubstractDataExtractor.class, new CalcDataExtractorMapper(this, CalcOperatorJson.SUB))
        .fluentPut(MultiplyDataExtractor.class, new CalcDataExtractorMapper(this, CalcOperatorJson.MUL))
        .fluentPut(DivideDataExtractor.class, new CalcDataExtractorMapper(this, CalcOperatorJson.DIV))
        .fluentPut(FixedValueDataExtractor.class, new FixedValueDataExtractorMapper())
        .fluentPut(DatalayerDataExtractor.class, new DatalayerValueDataExtractorMapper())
        .fluentPut(PlatformYearDataExtractor.class, new PlatformYearDataExtractorMapper());
  }

  @Override
  public Mono<DataValueJson> map(DataExtractor dataExtractor) {
    return Mono.fromCallable(() -> subDataExtractorMappers.getNotNull(dataExtractor.getClass()))
        .flatMap(mapper -> mapper.map(dataExtractor));
  }

  private interface SubDataExtractorMapper {
    Mono<DataValueJson> map(DataExtractor dataExtractor);
  }

  private class CalcDataExtractorMapper implements SubDataExtractorMapper {

    private final DataExtractor2DataValueJsonMapper dataExtractor2DataValueJsonMapper;
    private final CalcOperatorJson operator;

    public CalcDataExtractorMapper(DataExtractor2DataValueJsonMapper dataExtractor2DataValueJsonMapper, CalcOperatorJson operator) {
      this.dataExtractor2DataValueJsonMapper = dataExtractor2DataValueJsonMapper;
      this.operator = operator;
    }

    @Override
    public Mono<DataValueJson> map(DataExtractor dataExtractor) {
      return Mono.just((CalcDataExtractor) dataExtractor)
          .flatMap(calcDataExtractor -> Mono.zip(
              dataExtractor2DataValueJsonMapper.map(calcDataExtractor.left()),
              dataExtractor2DataValueJsonMapper.map(calcDataExtractor.right()))
          )
          .map(tuple -> new CalcJson(operator, tuple.getT1(), tuple.getT2()))
          .map(calcJson -> new DataValueJson()
              .setSource(SourceJson.CALC)
              .setCalc(calcJson)
          );
    }
  }

  private class FixedValueDataExtractorMapper implements SubDataExtractorMapper {
    @Override
    public Mono<DataValueJson> map(DataExtractor dataExtractor) {
      return Mono.just((FixedValueDataExtractor) dataExtractor)
          .map(fixedValueDataExtractor -> new DataValueJson()
              .setSource(SourceJson.VALUE)
              .setValue(fixedValueDataExtractor.value())
          );
    }
  }

  private class DatalayerValueDataExtractorMapper implements SubDataExtractorMapper {
    @Override
    public Mono<DataValueJson> map(DataExtractor dataExtractor) {
      return Mono.just((DatalayerDataExtractor) dataExtractor)
          .map(datalayerDataExtractor -> new DataValueJson()
              .setSource(SourceJson.DATALAYER)
              .setDatalayer(datalayerDataExtractor.key())
          );
    }
  }

  private class PlatformYearDataExtractorMapper implements SubDataExtractorMapper {
    @Override
    public Mono<DataValueJson> map(DataExtractor dataExtractor) {
      return Mono.just((PlatformYearDataExtractor) dataExtractor)
          .map(platformYearDataExtractor -> new DataValueJson()
              .setSource(SourceJson.PLATFORM)
              .setPlatform(PlatformYearDataExtractor.PLATFORM_KEY)
          );
    }
  }
}
