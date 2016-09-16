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

import static de.neshanjo.kinderradio.Main.newCommander;
import de.neshanjo.kinderradio.squeeze.SqueezeBoxCommander;
import java.util.concurrent.Callable;

/**
 *
 * @author Johannes C. Schneider
 */
public class ButtonPlayAlbumCallback implements Callable<Void> {
    
    private final String album;

    public ButtonPlayAlbumCallback(String album) {
        this.album = album;
    }
    
    @Override
    public Void call() throws Exception {
        SqueezeBoxCommander commander = newCommander();
        try {
            commander.connect();
            commander.powerOnPlayer();
            commander.playAlbum(album);
        } finally {
            commander.disconnect();
        }
        return null;
    }
    
}
