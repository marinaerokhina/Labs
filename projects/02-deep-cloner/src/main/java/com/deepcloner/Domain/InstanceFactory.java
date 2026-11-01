package com.deepcloner.Domain;

public interface InstanceFactory {
    <T> T newInstance(Class<T> type) throws CloneException;
}