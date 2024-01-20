package org.ua.chat.net.command;

import org.ua.chat.net.connection.ChatConnection;

public class ExitCommand implements Command {
    @Override
    public void execute(ChatConnection connection, String[] args) {
        System.out.println("Connection " + connection.getName() + " was close");
        connection.close();
    }
}
