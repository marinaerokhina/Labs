package com.deepcloner.Api;

import com.deepcloner.Application.ClonerService;

public final class Cloner {
    private static final Cloner DEFAULT = new ClonerBuilder().build();

    public static <T> T deepClone(T obj) {
        return DEFAULT.deepClonee(obj);
    }

    // Экземплярный вариант (для кастомной конфигурации)
    private final ClonerService service;
    Cloner(ClonerService service) { this.service = service; }

    public <T> T deepClonee(T obj) { return service.deepClone(obj); }
}