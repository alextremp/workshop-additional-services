package com.github.alextremp.additionalservices.application.mapper;

import com.github.alextremp.additionalservices.application.dto.CalcDTO;
import com.github.alextremp.additionalservices.application.dto.DataValueDTO;
import com.github.alextremp.additionalservices.domain.additionalservice.dataextractor.*;
import reactor.core.publisher.Mono;

import java.util.Objects;

public class DataExtractorMapper {

    public Mono<DataExtractor> from(DataValueDTO dto) {
        return Mono.defer(() -> {
            Objects.requireNonNull(dto.getSource(), "Source is required");
            switch (dto.getSource()) {
                case DATALAYER:
                    Objects.requireNonNull(dto.getDatalayer(), "'datalayer' node is required with 'datalayer' operator");
                    return mapDatalayer(dto.getDatalayer());
                case VALUE:
                    Objects.requireNonNull(dto.getValue(), "'value' node is required with 'value' operator");
                    return mapValue(dto.getValue());
                case PLATFORM:
                    Objects.requireNonNull(dto.getPlatform(), "'platform' node is required with 'platform' operator");
                    return mapPlatform(dto.getPlatform());
                case CALC:
                    Objects.requireNonNull(dto.getCalc(), "'calc' node is required with 'calc' operator");
                    return mapCalc(dto.getCalc());
                default:
                    throw new IllegalArgumentException("Source not supported: " + dto.getSource());
            }
        });
    }

    private Mono<DataExtractor> mapDatalayer(String datalayer) {
        return Mono.fromCallable(() -> new DatalayerDataExtractor(datalayer));
    }

    private Mono<DataExtractor> mapValue(String value) {
        return Mono.fromCallable(() -> new FixedValueDataExtractor(value));
    }

    private Mono<DataExtractor> mapPlatform(String platform) {
        return Mono.fromCallable(() -> {
            switch (platform) {
                case "year":
                    return new PlatformYearDataExtractor();
                default:
                    throw new IllegalArgumentException("platform not accepted: " + platform);

            }
        });
    }

    private Mono<DataExtractor> mapCalc(CalcDTO dto) {
        return Mono.fromCallable(() -> {
            Objects.requireNonNull(dto.getOperator(), "Operator is required");
            Objects.requireNonNull(dto.getLeft(), "Left is required");
            Objects.requireNonNull(dto.getRight(), "Right is required");
            return dto;
        })
                .flatMap(calcDTO -> Mono.zip(
                        from(calcDTO.getLeft()),
                        from(calcDTO.getRight())
                ))
                .map(lr -> {
                    switch (dto.getOperator()) {
                        case ADD:
                            return new AddCalcDataExtractor(lr.getT1(), lr.getT2());
                        case SUB:
                            return new SubstractCalcDataExtractor(lr.getT1(), lr.getT2());
                        case MUL:
                            return new MultiplyCalcDataExtractor(lr.getT1(), lr.getT2());
                        case DIV:
                            return new DivideCalcDataExtractor(lr.getT1(), lr.getT2());
                        default:
                            throw new IllegalArgumentException("Operator not accepted: " + dto.getOperator());
                    }
                });
    }
}
