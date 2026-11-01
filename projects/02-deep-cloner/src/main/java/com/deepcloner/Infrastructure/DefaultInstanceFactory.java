package com.deepcloner.Infrastructure;

import java.lang.reflect.Constructor;
import java.lang.reflect.RecordComponent;
import java.util.*;

import com.deepcloner.Domain.*;

@SuppressWarnings({"unchecked"})
public final class DefaultInstanceFactory implements InstanceFactory {

    public static DefaultInstanceFactory create() { return new DefaultInstanceFactory(); }

    @Override
    public <T> T newInstance(Class<T> type) throws CloneException {
        try {
            // records: создаём «пустой» через бэкап — не всегда возможно без данных
            if (type.isRecord()) {
                return instantiateRecord(type);
            }

            // коллекции
            if (Collection.class.isAssignableFrom(type)) {
                if (type == List.class || type == Collection.class) return (T) new ArrayList<>();
                if (type == Set.class) return (T) new LinkedHashSet<>();
                if (type == SortedSet.class || type == NavigableSet.class) return (T) new TreeSet<>();
            }

            // мапы
            if (Map.class.isAssignableFrom(type)) {
                if (type == Map.class) return (T) new LinkedHashMap<>();
                if (type == SortedMap.class || type.getName().contains("NavigableMap")) return (T) new TreeMap<>();
            }

            // стандартный путь
            Constructor<T> ctor = type.getDeclaredConstructor();
            ctor.setAccessible(true);
            return ctor.newInstance();

        } catch (NoSuchMethodException e) {
            // нет no-arg конструктора
            throw new CloneException("No default constructor for " + type.getName() +
                    " (provide InstanceFactory with Objenesis or custom)", e);
        } catch (Exception e) {
            throw new CloneException("Failed to instantiate " + type.getName(), e);
        }
    }

    private <T> T instantiateRecord(Class<T> type) throws Exception {
        
        RecordComponent[] comps = type.getRecordComponents();
        if (comps == null || comps.length == 0) {
            
            Constructor<T> ctor = type.getDeclaredConstructor();
            ctor.setAccessible(true);
            return ctor.newInstance();
        }
        throw new CloneException("Record " + type.getName() + " requires value-aware factory. " +
                "Provide custom InstanceFactory that can supply component defaults.");
    }
}