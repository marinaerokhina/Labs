package com.deepcloner.Infrastructure.Strategy;

import java.lang.reflect.Array;

import com.deepcloner.Domain.CloneContext;
import com.deepcloner.Domain.CloneStrategy;

public final class ArrayStrategy implements CloneStrategy<Object> 
{  
    @Override
    public boolean supports(Class<?> type) {
        return type.isArray();
    }

    @Override
    public Object cloneValue(Object source, CloneContext ctx) {
        if (source == null) return null;
        int len = Array.getLength(source);
        Class<?> comp = source.getClass().getComponentType();
        Object dst = Array.newInstance(comp, len);
        for (int i = 0; i < len; i++) {
            Object v = Array.get(source, i);
            Array.set(dst, i, ctx.deepClone(v));
        }
        return dst;
    }
}

