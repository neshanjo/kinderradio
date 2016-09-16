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
package de.neshanjo.kinderradio.squeeze;

import de.neshanjo.kinderradio.callback.ButtonNextCallback;
import de.neshanjo.kinderradio.Main;
import static de.neshanjo.kinderradio.config.Configuration.CONFIG;
import java.io.IOException;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author Johannes C. Schneider
 */
@Ignore(value = "only for manual testing")
public class SqueezeBoxCommanderTest {
    
    private SqueezeBoxCommander commander;
    
    @Before
    public void setup() {
        commander = Main.newCommander();
    }
    
    @Test
    public void testPlay() throws IOException {
        try {
            commander.connect();
            commander.powerOnPlayer();
            commander.playUrl(CONFIG.getPlayerConfig().getButton4());
        } finally {
            commander.disconnect();
        }
    }
    
    @Test
    public void testNext() throws Exception {
        new ButtonNextCallback().call();
    }
}