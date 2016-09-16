/* 
 * Copyright (C) 2016 Johannes C. Schneider
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.neshanjo.kinderradio.properties;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Johannes C. Schneider
 */
abstract class Converter<T> {
    
    abstract String fromObject(T object);
    
    abstract T fromString(String s);

    private static final Map<Class<?>, Converter<?>> REGISTRY;
    static {
        Map<Class<?>, Converter<?>> tmp = new HashMap<>();
        tmp.put(String.class, new Converter<String>() {
            @Override
            String fromObject(String object) {
                return object;
            }

            @Override
            String fromString(String s) {
                return s;
            }
            
        });
        Converter<Integer> integerConverter = new Converter<Integer>() {
            @Override
            String fromObject(Integer object) {
                return object.toString();
            }

            @Override
            Integer fromString(String s) {
                return new Integer(s);
            }
        };
        tmp.put(int.class, integerConverter);
        tmp.put(Integer.class, integerConverter);
        Converter<Boolean> booleanConverter = new Converter<Boolean>() {
            @Override
            String fromObject(Boolean object) {
                return object.toString();
            }

            @Override
            Boolean fromString(String s) {
                return Boolean.valueOf(s);
            }
        };
        tmp.put(boolean.class, booleanConverter);
        tmp.put(Boolean.class, booleanConverter);
        REGISTRY = Collections.unmodifiableMap(tmp);
    }
    
    static class ConverterException extends Exception {

        public ConverterException(Class<?> type) {
            super("Type " + type.getClass() + " is not supported");
        }
    }
    
    
    @SuppressWarnings("unchecked")
    public static <U> String convertFromObject(U o) throws ConverterException {
        if (o == null) {
            return null;
        }
        return getConverter((Class<U>)o.getClass()).fromObject(o);
    }
    
    public static <U> U convertFromString(String s, Class<U> targetType) throws ConverterException {
        if (s == null) {
            return null;
        }
        return getConverter(targetType).fromString(s);
    }
    
    public static Set<Class<?>> availableTypes() {
        return REGISTRY.keySet();
    } 
    
    /**
     * return a converter between type U and String
     * 
     * @param <U> 
     * @param type 
     * @return never @{code null}
     * @throws de.neshanjo.kinderradio.properties.Converter.ConverterException if no converter for the type has been
     *          registered
     */
    private static <U> Converter<U> getConverter(Class<U> type) throws ConverterException {
        Converter<?> converter = REGISTRY.get(type);
        if (converter == null) {
            throw new ConverterException(type);
        }
        @SuppressWarnings("unchecked")
        final Converter<U> typedConverter = (Converter<U>) converter;
        return typedConverter;
    }
    
}
