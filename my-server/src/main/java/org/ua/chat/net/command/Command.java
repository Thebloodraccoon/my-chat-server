package org.ua.chat.net.command;

import org.ua.chat.net.connection.ChatConnection;

public interface Command {
    void execute(ChatConnection connection, String arg);
}
