package org.ua.chat.net.connection;

import java.net.Socket;

public interface ChatConnection extends AutoCloseable{
    void sendMessage(String message);
    void close();
    String getName();
    Socket getSocket();
}