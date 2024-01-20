package org.ua.chat.net.server;


public interface Server extends AutoCloseable {
    void start() throws Exception;
}