package com.github.alextremp.additionalservices.application.mapper;

import com.github.alextremp.additionalservices.application.dto.BooleanOperatorJson;
import com.github.alextremp.additionalservices.application.dto.LoadRuleJson;
import com.github.alextremp.additionalservices.domain.additionalservice.loadrule.AndLoadRule;
import com.github.alextremp.additionalservices.domain.additionalservice.loadrule.LoadRule;
import com.github.alextremp.additionalservices.domain.additionalservice.loadrule.NotLoadRule;
import com.github.alextremp.additionalservices.domain.additionalservice.loadrule.OrLoadRule;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public class LoadRuleDomain2JsonMapper implements Mapper<LoadRule, LoadRuleJson> {

    private final FluentMap<String, SubLoadRuleMapper> subClassMappers;

    public LoadRuleDomain2JsonMapper() {
        final LoadRuleDomain2JsonMapper refThis = this;
        this.subClassMappers = new FluentMap<String, SubLoadRuleMapper>() {{
            put(AndLoadRule.class.getName(), new AndLoadRuleMapper(refThis));
            put(OrLoadRule.class.getName(), new OrLoadRuleMapper(refThis));
            put(NotLoadRule.class.getName(), new NotLoadRuleMapper(refThis));
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

    private class AndLoadRuleMapper implements SubLoadRuleMapper {
        private final LoadRuleDomain2JsonMapper loadRuleDomain2JsonMapper;

        private AndLoadRuleMapper(LoadRuleDomain2JsonMapper loadRuleDomain2JsonMapper) {
            this.loadRuleDomain2JsonMapper = loadRuleDomain2JsonMapper;
        }

        @Override
        public Mono<LoadRuleJson> map(LoadRule loadRule, LoadRuleJson loadRuleJson) {
            return Mono.just((AndLoadRule) loadRule)
                    .flatMap(andLoadRule -> map(andLoadRule.loadRules()))
                    .map(loadRuleJson::setAnd)
                    .map(dto -> dto.setOperator(BooleanOperatorJson.AND));
        }

        private Mono<List<LoadRuleJson>> map(List<LoadRule> loadRules) {
            return Flux.fromIterable(loadRules)
                    .flatMap(loadRuleDomain2JsonMapper::map)
                    .collectList();
        }
    }

    private class OrLoadRuleMapper implements SubLoadRuleMapper {
        private final LoadRuleDomain2JsonMapper loadRuleDomain2JsonMapper;

        private OrLoadRuleMapper(LoadRuleDomain2JsonMapper loadRuleDomain2JsonMapper) {
            this.loadRuleDomain2JsonMapper = loadRuleDomain2JsonMapper;
        }

        @Override
        public Mono<LoadRuleJson> map(LoadRule loadRule, LoadRuleJson loadRuleJson) {
            return Mono.just((OrLoadRule) loadRule)
                    .flatMap(orLoadRule -> map(orLoadRule.loadRules()))
                    .map(loadRuleJson::setOr)
                    .map(dto -> dto.setOperator(BooleanOperatorJson.OR));
        }

        private Mono<List<LoadRuleJson>> map(List<LoadRule> loadRules) {
            return Flux.fromIterable(loadRules)
                    .flatMap(loadRuleDomain2JsonMapper::map)
                    .collectList();
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
                    .flatMap(loadRuleDomain2JsonMapper::map)
                    .map(loadRuleJson::setNot)
                    .map(dto -> loadRuleJson.setOperator(BooleanOperatorJson.NOT));
        }
    }
}