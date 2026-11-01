package com.deepcloner.Infrastructure.Strategy;

import java.math.*;
import java.time.*;
import java.util.*;

import com.deepcloner.Domain.CloneContext;
import com.deepcloner.Domain.CloneStrategy;

public final class IdentityStrategy implements CloneStrategy<Object> {
    private static final Set<Class<?>> KNOWN_IMMUTABLES = Set.of(
            String.class, Integer.class, Long.class, Short.class, Byte.class,
            Boolean.class, Character.class, Double.class, Float.class,
            BigInteger.class, BigDecimal.class,
            LocalDate.class, LocalTime.class, LocalDateTime.class,
            OffsetDateTime.class, ZonedDateTime.class, Instant.class,
            UUID.class
    );

    @Override
    public boolean supports(Class<?> type) {
        return type.isEnum() || KNOWN_IMMUTABLES.contains(type);
    }

    @Override
    public Object cloneValue(Object source, CloneContext ctx) {
        return source; // иммутабельные + enum
    }
}