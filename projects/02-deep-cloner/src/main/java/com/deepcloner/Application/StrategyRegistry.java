package com.deepcloner.Application;

import java.util.*;

import com.deepcloner.Domain.*;
import com.deepcloner.Infrastructure.Strategy.*;

public final class StrategyRegistry {
    private final List<CloneStrategy<?>> chain;

    private StrategyRegistry(List<CloneStrategy<?>> chain) {
        this.chain = List.copyOf(chain);
    }

    public static StrategyRegistry defaultRegistry() {
        List<CloneStrategy<?>> base = new ArrayList<>();
        
        base.add(new ArrayStrategy());
        base.add(new CollectionStrategy());
        base.add(new MapStrategy());
        base.add(new DateTimeStrategy());
        base.add(new IdentityStrategy());
        
        return new StrategyRegistry(base);
    }

    public StrategyRegistry with(List<CloneStrategy<?>> extra) {
        List<CloneStrategy<?>> merged = new ArrayList<>(this.chain);
        merged.addAll(extra);
        return new StrategyRegistry(merged);
    }

    public CloneStrategy<?> resolve(Class<?> type) {
        for (CloneStrategy<?> s : chain) {
            if (s.supports(type)) return s;
        }
        return null;
    }

    public CloneStrategy<?> instantiateFieldStrategy(Class<? extends CloneStrategy<?>> c) {
        try { return c.getDeclaredConstructor().newInstance(); }
        catch (Exception e) { throw new IllegalStateException("Can't create field strategy " + c, e); }
    }
}