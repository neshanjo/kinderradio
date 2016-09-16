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
package de.neshanjo.kinderradio;

import de.neshanjo.kinderradio.callback.ButtonStopCallback;
import de.neshanjo.kinderradio.callback.ButtonNextCallback;
import de.neshanjo.kinderradio.callback.ButtonShutdownCallback;
import de.neshanjo.kinderradio.callback.ButtonPlayAlbumCallback;
import de.neshanjo.kinderradio.callback.ButtonPlayUrlCallback;
import static de.neshanjo.kinderradio.config.Configuration.CONFIG;
import de.neshanjo.kinderradio.gpio.GpioFacade;
import de.neshanjo.kinderradio.squeeze.SqueezeBoxCommander;
import java.util.logging.Logger;

/**
 *
 * @author Johannes C. Schneider
 */
public class Main {

    public static final Logger LOG = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) throws InterruptedException {
        final GpioFacade gpio = GpioFacade.getInstance();
        
        setupButton(gpio, 0, CONFIG.getPlayerConfig().getButton1());
        setupButton(gpio, 1, CONFIG.getPlayerConfig().getButton2());
        setupButton(gpio, 2, CONFIG.getPlayerConfig().getButton3());
        setupButton(gpio, 3, CONFIG.getPlayerConfig().getButton4());
        gpio.addButtonCallback(4, new ButtonStopCallback());
        gpio.addReleaseAfterLongPressButtonCallback(4, new ButtonShutdownCallback());
        
        for (;;) {
            Thread.sleep(5000);
        }

    }

    public static SqueezeBoxCommander newCommander() {
        return new SqueezeBoxCommander(
                CONFIG.getServerConfig().getAddress(),
                CONFIG.getServerConfig().getPort(),
                CONFIG.getPlayerConfig().getId(),
                CONFIG.getServerConfig().getUser(),
                CONFIG.getServerConfig().getPassword());
    }

    private static void setupButton(GpioFacade gpio, int i, String resource) {
        if (resource == null || resource.isEmpty()) {
            return;
        }
        if (resource.startsWith("http")) {
            gpio.addButtonCallback(i, new ButtonPlayUrlCallback(resource));
        } else {
            gpio.setButtonPressAgainCallback(i, new ButtonNextCallback());
            gpio.addButtonCallback(i, new ButtonPlayAlbumCallback(resource));
        }
    }
}
