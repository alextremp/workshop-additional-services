package com.github.alextremp.additionalservices.domain.additionalservice.loadrule;

import com.github.alextremp.additionalservices.domain.additionalservice.dataextractor.DataExtractor;
import com.github.alextremp.additionalservices.domain.additionalservice.datalayer.Datalayer;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Objects;

public class InLoadRule implements LoadRule {

    private final DataExtractor dataExtractor;
    private final List<String> collection;

    public InLoadRule(DataExtractor dataExtractor, List<String> collection) {
        Objects.requireNonNull(dataExtractor, "Data Extractor cannot be null");
        Objects.requireNonNull(collection, "Collection cannot be null");
        if (collection.isEmpty()) {
            throw new IllegalArgumentException("Collection cannot be empty");
        }
        this.dataExtractor = dataExtractor;
        this.collection = collection;
    }

    @Override
    public boolean evaluate(Datalayer datalayer) {
        String value = dataExtractor.value(datalayer);
        return collection.stream().anyMatch(data -> StringUtils.equals(value, data));
    }
}
