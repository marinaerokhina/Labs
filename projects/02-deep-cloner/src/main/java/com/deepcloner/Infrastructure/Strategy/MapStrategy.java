package com.deepcloner.Infrastructure.Strategy;

import java.lang.reflect.Constructor;
import java.util.*;

import com.deepcloner.Domain.CloneContext;
import com.deepcloner.Domain.CloneStrategy;

@SuppressWarnings({"rawtypes","unchecked"})
public final class MapStrategy implements CloneStrategy<Map<?,?>> 
{
    @Override
    public boolean supports(Class<?> type) {
        return Map.class.isAssignableFrom(type);
    }

    @Override
    public Map<?, ?> cloneValue(Map<?, ?> source, CloneContext ctx) {
        if (source == null) return null;
        Map dst = instantiateLike(source);
        for (Map.Entry<?,?> e : source.entrySet()) {
            Object k = ctx.deepClone(e.getKey());
            Object v = ctx.deepClone(e.getValue());
            dst.put(k, v);
        }
        return dst;
    }

    private Map instantiateLike(Map<?,?> src) {
        Class<?> c = src.getClass();
        try {
            if (c == HashMap.class) return new HashMap<>();
            if (c == LinkedHashMap.class) return new LinkedHashMap<>();
            if (c == TreeMap.class) return new TreeMap<>();
            if (c.getName().startsWith("java.util.ImmutableCollections$")) return new LinkedHashMap<>();
            Constructor<?> ctor = c.getDeclaredConstructor();
            ctor.setAccessible(true);
            return (Map) ctor.newInstance();
        } catch (Exception ignored) {
            return new LinkedHashMap<>();
        }
    }
}