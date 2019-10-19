package com.github.alextremp.additionalservices.application.mapper;

import com.github.alextremp.additionalservices.application.dto.InOperatorDTO;
import com.github.alextremp.additionalservices.application.dto.LeftRightOperatorDTO;
import com.github.alextremp.additionalservices.application.dto.LoadRuleDTO;
import com.github.alextremp.additionalservices.domain.additionalservice.dataextractor.DataExtractor;
import com.github.alextremp.additionalservices.domain.additionalservice.loadrule.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.util.List;
import java.util.Objects;

public class LoadRuleMapper {

    private final DataExtractorMapper dataExtractorMapper;

    public LoadRuleMapper(DataExtractorMapper dataExtractorMapper) {
        this.dataExtractorMapper = dataExtractorMapper;
    }

    public Mono<LoadRule> from(LoadRuleDTO dto) {
        return Mono.defer(() -> {
            switch (dto.getOperator()) {
                case AND:
                    if (dto.getAnd() == null || dto.getAnd().isEmpty()) {
                        throw new IllegalArgumentException("'and' node is required with 'and' operator");
                    }
                    return mapAnd(dto.getAnd());
                case OR:
                    if (dto.getOr() == null || dto.getOr().isEmpty()) {
                        throw new IllegalArgumentException("'or' node is required with 'or' operator");
                    }
                    return mapOr(dto.getAnd());
                case NOT:
                    Objects.requireNonNull(dto.getNot(), "'not' node is required with 'not' operator");
                    return mapNot(dto.getNot());
                case IN:
                    Objects.requireNonNull(dto.getIn(), "'in' node is required with 'in' operator");
                    return mapIn(dto.getIn());
                case EQUAL:
                    Objects.requireNonNull(dto.getEqual(), "'equal' node is required with 'equal' operator");
                    return mapEqual(dto.getEqual());
                case LESS_THAN:
                    Objects.requireNonNull(dto.getLessThan(), "'lessThan' node is required with 'lessThan' operator");
                    return mapLessThan(dto.getLessThan());
                case GREATER_THAN:
                    Objects.requireNonNull(dto.getGreaterThan(), "'greaterThan' node is required with 'greaterThan' operator");
                    return mapGreaterThan(dto.getGreaterThan());
                default:
                    throw new IllegalArgumentException("Operator not supported: " + dto.getOperator());
            }
        });
    }

    private Mono<LoadRule> mapAnd(List<LoadRuleDTO> list) {
        return Flux.fromIterable(list)
                .flatMap(this::from)
                .collectList()
                .map(AndLoadRule::new);
    }

    private Mono<LoadRule> mapOr(List<LoadRuleDTO> list) {
        return Flux.fromIterable(list)
                .flatMap(this::from)
                .collectList()
                .map(OrLoadRule::new);
    }

    private Mono<LoadRule> mapNot(LoadRuleDTO dto) {
        return from(dto)
                .map(NotLoadRule::new);
    }

    private Mono<LoadRule> mapIn(InOperatorDTO dto) {
        return dataExtractorMapper.from(dto.getData())
                .map(dataExtractor -> new InComparisonLoadRule(dataExtractor, dto.getCollection()));
    }

    private Mono<LoadRule> mapEqual(LeftRightOperatorDTO dto) {
        return mapLeftRightOperator(dto)
                .map(lr -> new EqualComparisonLoadRule(lr.getT1(), lr.getT2()));
    }

    private Mono<LoadRule> mapLessThan(LeftRightOperatorDTO dto) {
        return mapLeftRightOperator(dto)
                .map(lr -> new LessThanComparisonLoadRule(lr.getT1(), lr.getT2()));
    }

    private Mono<LoadRule> mapGreaterThan(LeftRightOperatorDTO dto) {
        return mapLeftRightOperator(dto)
                .map(lr -> new GreaterThanComparisonLoadRule(lr.getT1(), lr.getT2()));

    }

    private Mono<Tuple2<DataExtractor, DataExtractor>> mapLeftRightOperator(LeftRightOperatorDTO dto) {
        return Mono.zip(
                dataExtractorMapper.from(dto.getLeft()),
                dataExtractorMapper.from(dto.getRight())
        );
    }
}
