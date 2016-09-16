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
package de.neshanjo.kinderradio.callback;

import java.util.concurrent.Callable;
import java.util.logging.Logger;

/**
 *
 * @author Johannes C. Schneider
 */
public class ButtonShutdownCallback implements Callable<Void> {

    private static final Logger LOG = Logger.getLogger(ButtonShutdownCallback.class.getName());
    
    @Override
    public Void call() throws Exception {
        LOG.info("SHUTDOWN");
        Runtime.getRuntime().exec("shutdown -h now");
        return null;
    }
    
}
