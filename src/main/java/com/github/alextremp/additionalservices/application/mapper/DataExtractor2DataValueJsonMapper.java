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
    final DataExtractor2DataValueJsonMapper refThis = this;
    this.subDataExtractorMappers = new FluentMap<Class<? extends DataExtractor>, SubDataExtractorMapper>() {{
      put(AddDataExtractor.class, new CalcDataExtractorMapper(refThis, CalcOperatorJson.ADD));
      put(SubstractDataExtractor.class, new CalcDataExtractorMapper(refThis, CalcOperatorJson.SUB));
      put(MultiplyDataExtractor.class, new CalcDataExtractorMapper(refThis, CalcOperatorJson.MUL));
      put(DivideDataExtractor.class, new CalcDataExtractorMapper(refThis, CalcOperatorJson.DIV));
      put(FixedValueDataExtractor.class, new FixedValueDataExtractorMapper());
      put(DatalayerDataExtractor.class, new DatalayerValueDataExtractorMapper());
      put(PlatformYearDataExtractor.class, new PlatformYearDataExtractorMapper());
    }};
  }

  @Override
  public Mono<DataValueJson> map(DataExtractor dataExtractor) {
    return Mono.fromCallable(() -> new DataValueJson())
        .flatMap(dataValueJson -> map(dataExtractor, dataValueJson));
  }

  private Mono<DataValueJson> map(DataExtractor dataExtractor, DataValueJson dataValueJson) {
    return Mono.fromCallable(() -> subDataExtractorMappers.getNotNull(dataExtractor.getClass()))
        .flatMap(mapper -> mapper.map(dataExtractor, dataValueJson));
  }

  private interface SubDataExtractorMapper {
    Mono<DataValueJson> map(DataExtractor dataExtractor, DataValueJson dto);
  }

  private class CalcDataExtractorMapper implements SubDataExtractorMapper {

    private final DataExtractor2DataValueJsonMapper dataExtractor2DataValueJsonMapper;
    private final CalcOperatorJson operator;

    public CalcDataExtractorMapper(DataExtractor2DataValueJsonMapper dataExtractor2DataValueJsonMapper, CalcOperatorJson operator) {
      this.dataExtractor2DataValueJsonMapper = dataExtractor2DataValueJsonMapper;
      this.operator = operator;
    }

    @Override
    public Mono<DataValueJson> map(DataExtractor dataExtractor, DataValueJson dataValueJson) {
      return Mono.just((CalcDataExtractor) dataExtractor)
          .flatMap(this::toCalcJson)
          .map(dataValueJson::setCalc)
          .map(dto -> dto.setSource(SourceJson.CALC));
    }

    private Mono<CalcJson> toCalcJson(CalcDataExtractor calcDataExtractor) {
      return Mono.zip(dataExtractor2DataValueJsonMapper.map(calcDataExtractor.left()), dataExtractor2DataValueJsonMapper.map(calcDataExtractor.right()))
          .map(tuple -> new CalcJson(operator, tuple.getT1(), tuple.getT2()));
    }
  }

  private class FixedValueDataExtractorMapper implements SubDataExtractorMapper {
    @Override
    public Mono<DataValueJson> map(DataExtractor dataExtractor, DataValueJson dataValueJson) {
      return Mono.just((FixedValueDataExtractor) dataExtractor)
          .map(fixedValueDataExtractor -> fixedValueDataExtractor.value())
          .map(dataValueJson::setValue)
          .map(dto -> dataValueJson.setSource(SourceJson.VALUE));
    }
  }

  private class DatalayerValueDataExtractorMapper implements SubDataExtractorMapper {
    @Override
    public Mono<DataValueJson> map(DataExtractor dataExtractor, DataValueJson dataValueJson) {
      return Mono.just((DatalayerDataExtractor) dataExtractor)
          .map(datalayerDataExtractor -> datalayerDataExtractor.key())
          .map(dataValueJson::setDatalayer)
          .map(dto -> dataValueJson.setSource(SourceJson.DATALAYER));
    }
  }

  private class PlatformYearDataExtractorMapper implements SubDataExtractorMapper {
    @Override
    public Mono<DataValueJson> map(DataExtractor dataExtractor, DataValueJson dataValueJson) {
      return Mono.just((PlatformYearDataExtractor) dataExtractor)
          .map(platformYearDataExtractor -> dataValueJson.setPlatform("year"))
          .map(dto -> dataValueJson.setSource(SourceJson.PLATFORM));
    }
  }
}
