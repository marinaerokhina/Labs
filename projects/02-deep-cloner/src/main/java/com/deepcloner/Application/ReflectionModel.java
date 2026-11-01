package com.deepcloner.Application;

import java.lang.reflect.Modifier;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.*;

public final class ReflectionModel {
    private final Map<Class<?>, List<ReflectedField>> cache = new ConcurrentHashMap<>();

    public List<ReflectedField> fieldsOf(Class<?> type) {
        return cache.computeIfAbsent(type, this::scanFields);
    }

    private List<ReflectedField> scanFields(Class<?> type) {
        List<ReflectedField> res = new ArrayList<>();
        Class<?> cur = type;
        while (cur != null && cur != Object.class) {
            for (Field f : cur.getDeclaredFields()) {
                if ((f.getModifiers() & Modifier.STATIC) != 0) continue;
                f.setAccessible(true);
                res.add(new ReflectedField(f));
            }
            cur = cur.getSuperclass();
        }
        return res;
    }
}
