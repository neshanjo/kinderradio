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

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Johannes C. Schneider
 */
class Utilities {
    
    private Utilities() {
        //static utility class
    }
    
    static String lowerCaseFirstLetter(String s) {
        return s.substring(0, 1).toLowerCase() + s.substring(1);
    }

    static String toUpperUnderscore(String s) {
        if (s == null || s.isEmpty()) {
            return s;
        }
        boolean first = true;
        StringBuilder sb = new StringBuilder();
        for (char c : s.toCharArray()) {
            if (first) {
                sb.append(Character.toUpperCase(c));
                first = false;
                continue;
            }
            if (Character.isUpperCase(c)) {
                sb.append("_");
            }
            sb.append(Character.toUpperCase(c));
        }
        return sb.toString();
    }
    
    private static final Map<Class<?>, Class<?>> WRAPPER_TYPES = new HashMap<>();

    static {
        WRAPPER_TYPES.put(boolean.class, Boolean.class);
        WRAPPER_TYPES.put(byte.class, Byte.class);
        WRAPPER_TYPES.put(char.class, Character.class);
        WRAPPER_TYPES.put(short.class, Short.class);
        WRAPPER_TYPES.put(int.class, Integer.class);
        WRAPPER_TYPES.put(long.class, Long.class);
        WRAPPER_TYPES.put(float.class, Float.class);
        WRAPPER_TYPES.put(double.class, Double.class);
        WRAPPER_TYPES.put(void.class, Void.class);
    }

    static Class<?> wrapperTypeFor(Class<?> type) {
        Class<?> wrapperType = WRAPPER_TYPES.get(type);
        return wrapperType == null ? type : wrapperType;
    }
    
}
