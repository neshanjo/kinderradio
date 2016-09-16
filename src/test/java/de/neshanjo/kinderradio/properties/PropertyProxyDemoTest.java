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

import java.io.IOException;
import java.io.StringReader;
import java.util.Properties;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author Johannes C. Schneider
 */
public class PropertyProxyDemoTest {
    
    @PropertyConfiguration(prefix = "server")
    static interface ServerSettings {
        public static final int DEFAULT_PORT = 8080;
        String getName();
        int getPort();
    }
    
    @Test
    public void test() throws IOException {
        Properties props = new Properties();
        props.load(new StringReader("server.name=example.org"));
        ServerSettings serverSettings = PropertyProxy.of(ServerSettings.class, props);
        assertThat(serverSettings.getName(), is("example.org"));
        assertThat(serverSettings.getPort(), is(8080));
    }    
}
