package com.deepcloner.Application;

import java.util.*;

import com.deepcloner.Domain.*;
import com.deepcloner.Domain.Annotations.*;
import com.deepcloner.Infrastructure.DefaultInstanceFactory;

public final class ClonerService implements CloneContext {
    private final StrategyRegistry registry;
    private final ReflectionModel model;     // кэш полей/метаданных
    private final InstanceFactory factory;

    public ClonerService(List<CloneStrategy<?>> plugIns, InstanceFactory factory) {
        this.registry = StrategyRegistry.defaultRegistry().with(plugIns);
        this.model = new ReflectionModel(); // обёртка над кэшем
        this.factory = factory != null ? factory : DefaultInstanceFactory.create();
    }

    @Override
    public <T> T deepClone(T source) {
        try {
            return cloneInternal(source, new IdentityHashMap<>());
        } catch (CloneException e) { throw e; }
        catch (Exception e) { throw new CloneException("Clone failed", e); }
    }

    private <T> T cloneInternal(T src, IdentityHashMap<Object,Object> seen) throws Exception {
        if (src == null) return null;
    
        Object cached = seen.get(src);
        if (cached != null) return (T) cached;
    
        var strat = registry.resolve(src.getClass());
        if (strat != null) {
            @SuppressWarnings("unchecked")
            T result = (T) ((CloneStrategy<Object>) strat).cloneValue(src, thisContext(seen));
            return result;
        }

        Class<?> type = src.getClass();
        T dst = (T) factory.newInstance((Class<T>) type);
        seen.put(src, dst);
    
        for (ReflectedField f : model.fieldsOf(type)) {
            if (f.isStatic()) continue;
            var ignore = f.getAnnotation(CloneIgnore.class);
            if (ignore != null) continue;
    
            Object val = f.read(src);
            if (val == null) { f.write(dst, null); continue; }
    
            var cw = f.getAnnotation(CloneWith.class);
            if (cw != null) {
                var fieldStrategy = registry.instantiateFieldStrategy(cw.value());
                Object cloned = ((CloneStrategy<Object>) fieldStrategy).cloneValue(val, thisContext(seen));
                f.write(dst, cloned);
                continue;
            }
            var shallow = f.getAnnotation(ShallowClone.class);
            if (shallow != null) {
                f.write(dst, val);
            } else {
                Object cloned = cloneInternal(val, seen);
                f.write(dst, cloned);
            }
        }
        return dst;
    }

    private CloneContext thisContext(IdentityHashMap<Object,Object> seen) {
        return new CloneContext() {
            @Override public <X> X deepClone(X s) { return cloneInternalSilent(s, seen); }
        };
    }
    
    @SuppressWarnings("unchecked")
    private <X> X cloneInternalSilent(X s, IdentityHashMap<Object,Object> seen) {
        try { return (X) cloneInternal(s, seen); }
        catch (RuntimeException re) { throw re; }
        catch (Exception e) { throw new CloneException("Clone failed", e); }
    }
}