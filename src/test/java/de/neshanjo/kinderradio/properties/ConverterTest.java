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

import de.neshanjo.kinderradio.properties.Converter.ConverterException;
import static de.neshanjo.kinderradio.properties.Converter.convertFromObject;
import static de.neshanjo.kinderradio.properties.Converter.convertFromString;
import java.util.Date;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Johannes C. Schneider
 */
public class ConverterTest {

    @Test
    public void testFromNull() throws ConverterException {
        assertNull(convertFromObject(null));
    }
    
    @Test
    public void testFromString() throws ConverterException {
        assertEquals("test", convertFromObject("test"));
    }
    
    @Test
    public void testFromBoolean() throws ConverterException {
        assertEquals("true", convertFromObject(true));
        assertEquals("false", convertFromObject(false));
        assertEquals("true", convertFromObject(Boolean.TRUE));
        assertEquals("false", convertFromObject(Boolean.FALSE));
    }
    
    @Test
    public void testFromInt() throws ConverterException {
        assertEquals("41239", convertFromObject(41239));
        assertEquals("-3100", convertFromObject(-3100));
        assertEquals("0", convertFromObject(0));
    }
    
    @Test(expected = ConverterException.class)
    public void testFromOther() throws ConverterException {
        convertFromObject(new Date());
    }
    
    @Test
    public void testToNull() throws ConverterException {
        assertNull(convertFromString(null, null));
    }
    
    @Test
    public void testToString() throws ConverterException {
        assertEquals("test", convertFromString("test", String.class));
    }
    
    @Test
    public void testToBoolean() throws ConverterException {
        assertEquals(true, convertFromString("true", Boolean.class));
        assertEquals(false, convertFromString("false", Boolean.class));
        assertEquals(true, convertFromString("true", boolean.class));
        assertEquals(false, convertFromString("false", boolean.class));
    }
    
    @Test
    public void testToInt() throws ConverterException {
        assertTrue(41239 == convertFromString("41239", Integer.class));
        assertTrue(-3100 == convertFromString("-3100", Integer.class));
        assertTrue(0 == convertFromString("0", Integer.class));
        assertTrue(41239 == convertFromString("41239", int.class));
        assertTrue(-3100 == convertFromString("-3100", int.class));
        assertTrue(0 == convertFromString("0", int.class));
    }
    
    @Test(expected = ConverterException.class)
    public void testToOther() throws ConverterException {
        convertFromString("abc", Date.class);
    }

}