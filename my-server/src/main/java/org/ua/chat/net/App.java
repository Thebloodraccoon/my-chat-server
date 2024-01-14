package org.ua.chat.net;

import org.ua.chat.net.server.ChatServer;
import org.ua.chat.net.server.Server;

import java.io.IOException;

public class App {
    public static void main(String[] args) throws Exception {
        try (Server server = new ChatServer(8000)) {
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
