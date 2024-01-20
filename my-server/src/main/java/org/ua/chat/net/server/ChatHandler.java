package org.ua.chat.net.server;

import org.ua.chat.net.connection.ChatConnection;

public interface ChatHandler {
    void onConnect(ChatConnection connection);
    void onMessage(ChatConnection connection, String message);
    void onDisconnect(ChatConnection connection);
    void onError(ChatConnection connection, Exception e);
    void onCommand(ChatConnection connection, String commandMessage);
}