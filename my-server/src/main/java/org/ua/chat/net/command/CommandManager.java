package org.ua.chat.net.command;

import org.ua.chat.net.connection.ChatConnection;

import java.util.HashMap;
import java.util.Map;

public class CommandManager {
    private final Map<String, Command> commands = new HashMap<>();

    public void registerCommand(String commandName, Command command) {
        commands.put(commandName, command);
    }

    public void executeCommand(ChatConnection connection, String commandName, String arg) throws Exception {
        Command command = commands.get(commandName);
        if (command != null) {
            command.execute(connection, arg);
        }
    }
}
