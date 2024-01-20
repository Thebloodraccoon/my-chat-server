package org.ua.chat.net.client;

public interface Client extends AutoCloseable {
    void connect();
}
