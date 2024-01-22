package org.ua.chat.net.connection;

import java.io.PrintWriter;

public interface ChatConnection extends AutoCloseable{
    void sendMessage(String message);
    void close();
    String getName();
}