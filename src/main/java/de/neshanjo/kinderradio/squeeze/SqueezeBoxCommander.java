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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Johannes C. Schneider
 */
public class SqueezeBoxCommander {
    
    public static final int DEFAULT_PORT = 9090;
    private static final Logger LOG = Logger.getLogger(SqueezeBoxCommander.class.getName());

    private final String server;
    private final int port;
    private final String playerId;
    private final String userName;
    private final String password;
    private Socket socket = null;
    private PrintWriter out = null;
    private BufferedReader in = null;

    public SqueezeBoxCommander(String server, int port, String playerId, String userName, String password) {
        this.server = server;
        this.port = port;
        this.playerId = playerId;
        this.userName = userName;
        this.password = password;
    }
    
    public void connect() throws IOException {
        try {
            LOG.log(Level.FINE, "Connecting to {0}:{1}", new Object[]{server, "" + port});
            socket = new Socket(server, port);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException initEx) {
            try {
                closeSocketAndStreams();
            } catch (IOException closeEx) {
                //do nothing, closing is only for safety
            }
            //throw to outside that the user knows what failed during connecting
            throw initEx;
        }
        
        if (userName != null) {
            LOG.log(Level.FINE, "Login with user {0}", userName);
            sendCommand("login", userName, password);
        }
    }
    
       
    public void disconnect() throws IOException {
        LOG.fine("Disconnecting from server");
        try { 
            if (out != null) {
                sendCommand("exit");
            }
        } finally {
            closeSocketAndStreams();
        }
    }
    
    public void powerOnPlayer() throws IOException {
        sendCommand("power", "1");
    }
    
    public void powerOffPlayer() throws IOException {
        sendCommand("power", "0");
    }
    
    public void playUrl(String url) throws IOException {
        sendCommand("playlist", "play", url);
    }
    
    public void playTracksFromArtist(String artist) throws IOException {
        sendCommand("playlist", "loadtracks", String.format("contributor.namesearch=%s", URLEncoder.encode(artist, "UTF-8")));
    }
    
    public void playAlbum(String albumTitle) throws IOException {
        sendCommand("playlist", "loadtracks", String.format("album.titlesearch=%s", URLEncoder.encode(albumTitle, "UTF-8")));
    }
    
    public void nextTrack() throws IOException {
        sendCommand("playlist", "index +1");
    }
    
    private void sendCommand(String command, String... args) throws IOException {
        StringBuilder sb = new StringBuilder(playerId);
        sb.append(' ').append(command);
        if (args != null) {
            for (String arg : args) {
                sb.append(" ").append(arg);
            }
        }
        final String concatCommand = sb.toString();
        out.println(concatCommand);
        final String response = in.readLine();
        final String logMessage = response != null && !response.isEmpty() ? "[OK]" : "[NOT OK]"; 
        LOG.log(Level.FINER, "{0} {1}", new Object[] { concatCommand, logMessage});
    }
    
    private void closeSocketAndStreams() throws IOException {
        try {
            if (out!=null) {
                out.close();
            }
            if (in != null) {
                in.close();
            }
        } finally {
            LOG.fine("Closing connection");
            socket.close();
        }
    }
    
}
