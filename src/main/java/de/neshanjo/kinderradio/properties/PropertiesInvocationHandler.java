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

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Properties;

/**
 *
 * @author Johannes C. Schneider
 */
class PropertiesInvocationHandler implements InvocationHandler {

    private final String prefix;
    private final Properties props;
    private final Map<Method, Object> defaultValues;

    PropertiesInvocationHandler(PropertyConfiguration config, Properties props, Map<Method, Object> defaultValues) {
        this.prefix = config == null ? "" : config.prefix() + ".";
        this.props = props;
        this.defaultValues = defaultValues;
    }

    private String prefixedKeyFromGetterOrSetter(final String name) {
        return prefix + Utilities.lowerCaseFirstLetter(name.substring(3));
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        final String name = method.getName();
        try {
            if (name.length() > 3 && name.startsWith("get")) {
                final Class<?> returnType = method.getReturnType();
                Object value = Converter.convertFromString(props.getProperty(prefixedKeyFromGetterOrSetter(name)),
                        returnType);
                if (value == null) {
                    if (method.getParameterCount() == 0) {
                        value = defaultValues.get(method);
                    } else {
                        /* method.getParameterCount() == 1 */
                        value = args[0];
                    }
                }
                return value;
            } else if (name.length() > 3 && name.startsWith("set")) {
                if (method.getParameterCount() == 1) {
                    props.setProperty(prefixedKeyFromGetterOrSetter(name), Converter.convertFromObject(args[0]));
                    return null;
                }
            }
        } catch (Converter.ConverterException ex) {
            throw new IllegalStateException("Invalid method call of " + method, ex);
        }
        throw new IllegalStateException("Invalid method call of " + method);
    }

}
