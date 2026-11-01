package com.deepcloner.Domain;

public interface CloneContext {
    <T> T deepClone(T source);           // повторное использование из стратегий
}