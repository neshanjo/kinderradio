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

import java.util.Properties;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Johannes C. Schneider
 */
public class PropertyProxyTest {
    
    private Properties props;
    private Settings settings;
    private ServerSettings sset;
    
    @Before
    public void beforeTest() {
        props = new Properties();
        props.put("id", "5");
        props.put("name", "Johannes");
        props.put("server.url", "example.org");
        props.put("server.connectOnStartup", "true");
        props.put("birthday", "1.1.11");
        settings = PropertyProxy.of(Settings.class, props);
        sset = PropertyProxy.of(ServerSettings.class, props);
    }
    
    @Test
    public void gettingAndSetting() {
        assertThat(settings.getName(), is("Johannes"));
        assertEquals("Johannes", settings.getName());
        assertEquals(5, settings.getId());
        settings.setName("Test");
        assertEquals("Test", props.getProperty("name"));
        settings.setId(42);
        assertEquals("42", props.getProperty("id"));
    }
    
    @Test
    public void gettingWithDefault() {
        assertNull(settings.getVersion());
        assertEquals("1.0", settings.getVersion("1.0"));
        props.setProperty("version", "2.0");
        assertEquals("2.0", settings.getVersion());
        assertEquals("2.0", settings.getVersion("1.0"));
    }

    @Test
    public void noArgGetterWithDefault() {
        assertEquals(Settings.DEFAULT_VALUE, settings.getValue());
        props.setProperty("value", "other");
        assertEquals("other", settings.getValue());
    }

    @Test
    public void gettingWithPrefix() {
        assertEquals("example.org", sset.getUrl());
        assertEquals(true, sset.getConnectOnStartup());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void unsupported() {
        PropertyProxy.of(Unsupported.class, props);
    }
    
}
