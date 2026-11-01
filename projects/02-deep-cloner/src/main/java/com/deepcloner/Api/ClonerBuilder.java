package com.deepcloner.Api;

import java.util.*;

import com.deepcloner.Application.ClonerService;
import com.deepcloner.Domain.CloneStrategy;
import com.deepcloner.Domain.InstanceFactory;

public final class ClonerBuilder {
    private final List<CloneStrategy<?>> strategies = new ArrayList<>();
    private InstanceFactory instanceFactory;

    public ClonerBuilder addStrategy(CloneStrategy<?> s) { strategies.add(s); return this; }
    public ClonerBuilder instanceFactory(InstanceFactory f) { this.instanceFactory = f; return this; }

    public Cloner build() {
        return new Cloner(new ClonerService(strategies, instanceFactory));
    }
}