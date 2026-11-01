package com.deepcloner.Infrastructure.Strategy;

import java.lang.reflect.Constructor;
import java.util.*;

import com.deepcloner.Domain.CloneContext;
import com.deepcloner.Domain.CloneStrategy;

@SuppressWarnings({"rawtypes","unchecked"})
public final class CollectionStrategy implements CloneStrategy<Collection<?>>
{
    @Override
    public boolean supports(Class<?> type) {
        return Collection.class.isAssignableFrom(type) && !Map.class.isAssignableFrom(type);
    }

    @Override
    public Collection<?> cloneValue(Collection<?> source, CloneContext ctx) {
        if (source == null) return null;
        Collection dst = instantiateLike(source);
        for (Object e : source) dst.add(ctx.deepClone(e));
        return dst;
    }

    private Collection instantiateLike(Collection<?> src) {
        Class<?> c = src.getClass();
        try {
            if (c == ArrayList.class) return new ArrayList<>();
            if (c == LinkedList.class) return new LinkedList<>();
            if (c == HashSet.class) return new HashSet<>();
            if (c == LinkedHashSet.class) return new LinkedHashSet<>();
            if (c.getName().equals("java.util.ImmutableCollections$ListN")) return new ArrayList<>();
            Constructor<?> ctor = c.getDeclaredConstructor();
            ctor.setAccessible(true);
            return (Collection) ctor.newInstance();
        } catch (Exception ignored) {
            return new ArrayList<>();
        }
    }
}
