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

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Configure how the {@link PropertyProxy} reads the properties by annotating the interface.
 * 
 * @author Johannes C. Schneider
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface PropertyConfiguration {
    
    /**
     * Adds a prefix to the property keys. If the returned string is non-empty, {@code prefix() + "."} is 
     * prepended to the property key.
     * 
     * @return  By default, the empty string is returned.
     */
    String prefix() default "";
}
