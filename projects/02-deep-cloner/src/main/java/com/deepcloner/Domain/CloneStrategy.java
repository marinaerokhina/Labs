package com.deepcloner.Domain;

public interface CloneStrategy<T> {
    boolean supports(Class<?> type);
    T cloneValue(T src, CloneContext ctx) throws CloneException;
}