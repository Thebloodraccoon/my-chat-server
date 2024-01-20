package org.ua.chat.net;


import org.ua.chat.net.server.Server;
import org.ua.chat.net.server.ThreadPoolChatServer;

import java.io.IOException;

public class AppServer {
    public static void main(String[] args) throws Exception {
        try (Server server = new ThreadPoolChatServer(8000)) {
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
