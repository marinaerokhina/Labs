package com.deepcloner.Application;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public final class ReflectedField {
    private final Field f;
    ReflectedField(Field f) { this.f = f; }

    public boolean isStatic() { return (f.getModifiers() & java.lang.reflect.Modifier.STATIC) != 0; }

    public <A extends Annotation> A getAnnotation(Class<A> c) { return f.getAnnotation(c); }
    public boolean hasAnnotation(Class<? extends Annotation> c) { return f.isAnnotationPresent(c); }

    public Object read(Object o) {
        try { return f.get(o); } catch (IllegalAccessException e) { throw new RuntimeException(e); }
    }
    public void write(Object o, Object v) {
        try { f.set(o, v); } catch (IllegalAccessException e) { throw new RuntimeException(e); }
    }
}