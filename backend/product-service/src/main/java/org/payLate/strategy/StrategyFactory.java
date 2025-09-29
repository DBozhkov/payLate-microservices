package org.payLate.strategy;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class StrategyFactory {

    private final Map<String, ProductCreationStrategy> strategyMap;

    public StrategyFactory(List<ProductCreationStrategy> strategies) {
        this.strategyMap = new HashMap<>();
        strategies.forEach(strategy -> strategyMap.put(strategy.getPartnerName().toLowerCase(), strategy));
    }

    public Optional<ProductCreationStrategy> getStrategy(String partnerName) {
        return Optional.ofNullable(strategyMap.get(partnerName.toLowerCase()));
    }
}