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
package de.neshanjo.kinderradio.config;

import de.neshanjo.kinderradio.properties.PropertyConfiguration;

/**
 *
 * @author Johannes C. Schneider
 */
@PropertyConfiguration(prefix = "player")
public interface PlayerConfig {
    
    public static final int DEFAULT_BUSY_TIME_IN_MS = 1000;
    public static final int DEFAULT_LONG_PRESS_TIME_IN_MS = 5000;

    String getId();
    
    String getButton1();
    
    String getButton2();
    
    String getButton3();
    
    String getButton4();
    
    int getBusyTimeInMs();
    
    int getLongPressTimeInMs();
}
