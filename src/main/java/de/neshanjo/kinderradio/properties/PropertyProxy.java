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

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * This property proxy maps {@link java.util.Properties} to an interface implementation to read and store the
 * properties.
 * <p>
 * Three kind of methods are allowed in the interface:
 * <ul>
 * <li>get methods with no parameter and return type {@code t},</li>
 * <li>get methods with a single parameter of type {@code t} and the same return type {@code t},
 * <li>set methods with a single parameter of type {@code t} and void return type,
 * </ul>
 * where {@code t} is one of {@code String}, {@code Integer}, {@code Boolean}, {@code int}, {@code boolean}.
 * <p>
 * Methods <b>get<i>SomeMethodName</i></b>/<b>set<i>SomeMethodName</i></b> are mapped to the property key
 * <b><i><u>s</u>omeMethodName</i></b> in the property file. If the interface is annotated with {@link PropertyConfiguration},
 * whose {@link PropertyConfiguration#prefix()} is non-empty, the property key is prefixed 
 * with {@link PropertyConfiguration#prefix()} {@code + "."}.
 * <p>
 * The one parameter get method is used to provide a default value which is returned if the underlying property
 * has no value, i.e., {@link java.util.Properties#getProperty(java.lang.String)} returns {@code null}.
 * <p>
 * Default values for get methods without parameters can be supplied via a {@code public static final} constant
 * member of the interface. The name of such a constant is "DEFAULT_" followed by the method name after get 
 * where camel case is converted to upper case with underscores (see the example below). 
 * If an interface defines a no parameter getter for a primitive type, it must provide a default value.
 * <p>
 * Code example:
 * <pre>
 * <code>
 * interface Configuration {
 *      public static final String DEFAULT_FIRST_NAME = "Homer";
 *      String getFirstName();
 *      String getFirstName(String defaultFirstName);
 *      void setFirstName(String firstName);
 * }
 * 
 * Configuration c = PropertyProxy.of(Configuration.class, new Properties());
 * System.out.println(c.getFirstName()); // Homer
 * System.out.println(c.getFirstName("Marge")); // Marge
 * c.setFirstName("Bart");
 * System.out.println(c.getFirstName()); // Bart
 * System.out.println(c.getFirstName("Marge")); // Bart
 * </code>
 * </pre>
 *
 * @author Johannes C. Schneider
 */
public class PropertyProxy {

    private static final String DEFAULT_PREFIX = "DEFAULT_";

    /**
     * creates a new PropertyProxy. Is is checked during creation, that the interface adheres to the getter/setter
     * rules as defined in the description of this class.
     * 
     * @param <U> type of the given interface
     * @param iface the interface for accessing the properties
     * @param props the underlying properties that this proxy operates on
     * @return a proxy implementation of the interface
     * @throws IllegalArgumentException if the interface violates the rules defined in the description of this class
     */
    @SuppressWarnings("unchecked")
    public static <U> U of(final Class<U> iface, final Properties props) {
        final Map<Method, Object> defaultValues = checkMethodsAndParseDefaultValues(iface);
        return (U) Proxy.newProxyInstance(PropertyProxy.class.getClassLoader(),
                new Class<?>[]{iface},
                new PropertiesInvocationHandler(getConfiguration(iface), props, defaultValues));
    }

    private static PropertyConfiguration getConfiguration(Class<?> iface) {
        return iface.getAnnotation(PropertyConfiguration.class);
    }

    private static <U> Map<Method, Object> checkMethodsAndParseDefaultValues(Class<U> iface) {
        Map<Method, Object> defaultValues = new HashMap<>();
        for (Method method : iface.getMethods()) {
            final boolean isGetter = method.getName().startsWith("get");
            final boolean isSetter = method.getName().startsWith("set");
            if (!isGetter && !isSetter) {
                throw new IllegalArgumentException("Method name " + method.getName()
                        + " does not start with \"get\" or \"set\".");
            }

            if (isGetter) {
                if (!Converter.availableTypes().contains(method.getReturnType())) {
                    throw new IllegalArgumentException("Getter method " + method.getName()
                            + " has an invalid return type."
                            + " Only types from " + Converter.availableTypes() + " are supported.");
                }
                if (method.getParameterCount() == 0) {
                    loadDefaultValue(method, iface, defaultValues);
                    continue;
                }
                if (method.getParameterCount() > 1) {
                    throw new IllegalArgumentException("Getter method " + method.getName()
                            + " has more than one parameter.");
                }
                // method.getParameterCount() == 1
                if (method.getReturnType() != method.getParameterTypes()[0]) {
                    throw new IllegalArgumentException("Getter method " + method.getName()
                            + " has a parameter whose type is different from the return type.");
                }
            } else {
                /* isSetter */
                if (method.getReturnType() != void.class) {
                    throw new IllegalArgumentException("Setter method " + method.getName()
                            + " has a non-void return type.");
                }
                if (method.getParameterCount() != 1) {
                    throw new IllegalArgumentException("Setter method " + method.getName()
                            + " does not have exactly one parameter.");
                }
                //method.getParameterCount() == 1
                if (!Converter.availableTypes().contains(method.getParameterTypes()[0])) {
                    throw new IllegalArgumentException("Getter method " + method.getName()
                            + " has an invalid parameter type."
                            + " Only types from " + Converter.availableTypes() + " are supported.");
                }
            }

            return defaultValues;
        }
        return defaultValues;
    }

    private static <U> void loadDefaultValue(Method method, Class<U> iface, Map<Method, Object> defaultValues) throws IllegalArgumentException {
        final String defaultFieldName = DEFAULT_PREFIX + Utilities.toUpperUnderscore(method.getName().substring(3));
        try {
            final Field defaultField;
            try {
                defaultField = iface.getField(defaultFieldName);
            } catch (NoSuchFieldException ex) {
                if (method.getReturnType().isPrimitive()) {
                    throw new IllegalArgumentException("No default value field defined for getter method " + method
                            .getName() + " with primitive return type. Define a public static final field "
                            + defaultFieldName, ex);
                } else {
                    return;
                }
            }
            if (!Modifier.isPublic(defaultField.getModifiers()) || !Modifier.isStatic(defaultField.getModifiers())
                    || !Modifier.isFinal(defaultField.getModifiers())) {
                throw new IllegalArgumentException("Default value field " + defaultFieldName + " in " + iface
                        .getSimpleName()
                        + " must be declared public, static and final.");
            }
            Object defaultValue = defaultField.get(null);
            if (defaultValue != null && !firstSubClassOrEqualOfSecondModuloPrimitiveTypes(defaultValue.getClass(),
                    method.getReturnType())) {
                throw new IllegalArgumentException("Type of default field " + defaultFieldName + " must be assignable "
                        + "to the the return type of the corresponding getter method " + method.getName());
            }
            defaultValues.put(method, defaultValue);
        } catch (SecurityException | IllegalAccessException ex) {
            throw new IllegalArgumentException("Error getting default value for no arg getter " + method.getName(), ex);
        }
    }

    @SuppressWarnings("unchecked")
    private static boolean firstSubClassOrEqualOfSecondModuloPrimitiveTypes(Class<?> c1, Class<?> c2) {
        return Utilities.wrapperTypeFor(c2).isAssignableFrom(Utilities.wrapperTypeFor(c1));
    }

}
