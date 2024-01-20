package org.ua.chat.net;


import org.ua.chat.net.server.Server;
import org.ua.chat.net.server.ChatServer;

import java.io.IOException;

public class AppServer {
    public static void main(String[] args) throws Exception {
        try (Server server = new ChatServer(8000, 10)) {
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
