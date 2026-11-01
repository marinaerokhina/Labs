package com.deepcloner.Infrastructure.Strategy;

import java.util.*;

import com.deepcloner.Domain.CloneContext;
import com.deepcloner.Domain.CloneStrategy;

public class DateTimeStrategy implements CloneStrategy<Object>
{
    @Override
    public boolean supports(Class<?> type) {
        return type == Date.class || Calendar.class.isAssignableFrom(type);
    }

    @Override
    public Object cloneValue(Object src, CloneContext ctx) {
        if (src == null) return null;
        if (src instanceof Date d) return new Date(d.getTime());
        if (src instanceof GregorianCalendar gc) return (GregorianCalendar) gc.clone();
        if (src instanceof Calendar c) {
            Calendar copy = (Calendar) c.clone();
            copy.setTimeInMillis(c.getTimeInMillis());
            return copy;
        }
        return src;
    }
}
