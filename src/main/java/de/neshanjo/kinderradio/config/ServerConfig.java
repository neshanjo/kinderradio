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
import de.neshanjo.kinderradio.squeeze.SqueezeBoxCommander;

/**
 *
 * @author Johannes C. Schneider
 */
@PropertyConfiguration(prefix = "server")
public interface ServerConfig {
    
    public static final int DEFAULT_PORT = SqueezeBoxCommander.DEFAULT_PORT;

    String getAddress();
    
    int getPort();

    String getUser();

    String getPassword();
}
