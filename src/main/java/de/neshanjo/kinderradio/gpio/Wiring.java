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
package de.neshanjo.kinderradio.gpio;

import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiPin;

/**
 *
 * @author Johannes C. Schneider
 */
public interface Wiring {
    public static final Pin BUTTON_ONE = RaspiPin.GPIO_01;
    public static final Pin BUTTON_TWO = RaspiPin.GPIO_02;
    public static final Pin BUTTON_THREE = RaspiPin.GPIO_03;
    public static final Pin BUTTON_FOUR = RaspiPin.GPIO_04;
    public static final Pin BUTTON_FIVE = RaspiPin.GPIO_05;
    
    public static final Pin LED_GREEN = RaspiPin.GPIO_21;
    public static final Pin LED_YELLOW = RaspiPin.GPIO_22;
    public static final Pin LED_RED = RaspiPin.GPIO_26;
}
