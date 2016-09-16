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

import de.neshanjo.kinderradio.properties.PropertyProxy;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

/**
 *
 * @author Johannes C. Schneider
 */
public enum Configuration {
    
    CONFIG;
    
    public final Logger LOG = Logger.getLogger(Configuration.class.getName());
    private final ServerConfig serverConfig;
    private final PlayerConfig playerConfig;
    
    private Configuration() {
        Properties props = new Properties();
        try {
            LOG.info("Loading config from kinderradio.properties");
            props.load(new FileInputStream("kinderradio.properties"));
        } catch (IOException ex) {
            throw new IllegalStateException(ex);
        }
        serverConfig = PropertyProxy.of(ServerConfig.class, props);
        playerConfig = PropertyProxy.of(PlayerConfig.class, props);
    }

    public ServerConfig getServerConfig() {
        return serverConfig;
    }

    public PlayerConfig getPlayerConfig() {
        return playerConfig;
    }
}
