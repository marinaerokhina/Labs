package com.deepcloner.Domain.Annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.deepcloner.Domain.CloneStrategy;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.ElementType;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface CloneWith {
    Class<? extends CloneStrategy<?>> value();
}
