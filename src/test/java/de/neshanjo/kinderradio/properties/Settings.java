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

/**
 *
 * @author Johannes C. Schneider
 */
public interface Settings {
 
    public static final int DEFAULT_ID = 0;
    public static final boolean DEFAULT_CONFIG_EXISTS = false;
    public static final String DEFAULT_VALUE = "Test";
    
    int getId();
    void setId(int id);
    
    String getName();
    void setName(String id);
    
    String getVersion();
    String getVersion(String defaultVersion);
    
    boolean getConfigExists();
    void setConfigExists(boolean b);

    String getValue();
}
