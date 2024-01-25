package org.ua.chat.net.server;

import lombok.Getter;
import org.ua.chat.net.command.Command;
import org.ua.chat.net.command.CommandManager;
import org.ua.chat.net.command.ExitCommand;
import org.ua.chat.net.command.FileCommand;
import org.ua.chat.net.connection.ChatConnection;
import org.ua.chat.net.connection.ThreadChatConnection;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatServer implements Server, ChatHandler, AutoCloseable {
    private final ServerSocket serverSocket;
    @Getter
    private final List<ChatConnection> connections = new ArrayList<>();
    private final CommandManager commandManager = new CommandManager();
    private final ExecutorService executorService;


    public ChatServer(int port, int threads) throws IOException {
        this.serverSocket = new ServerSocket(port);
        this.executorService = Executors.newFixedThreadPool(threads);

        initializeCommandManager();
    }

    private void initializeCommandManager() {
        Command exit = new ExitCommand();
        Command file = new FileCommand();

        commandManager.registerCommand("-exit", exit);
        commandManager.registerCommand("-file", file);
    }

    @Override
    public void start() throws IOException {
        while (!serverSocket.isClosed()) {
            final Socket socket = serverSocket.accept();

            Runnable connection = new ThreadChatConnection(socket, this);
            executorService.submit(connection);
        }
    }

    @Override
    public void onConnect(ChatConnection connection) {
        connections.forEach(c -> c.sendMessage("[SERVER] " + connection.getName() + " entered the chat."));
        connections.add(connection);
    }

    @Override
    public void onMessage(ChatConnection connection, String message) {
        for (ChatConnection conn :
                connections) {
            conn.sendMessage("[" + connection.getName() + "]: " + message);
        }
    }

    @Override
    public void onCommand(ChatConnection connection, String commandMessage) {
        try {
            String[] parts = commandMessage.split(" ", 2);
            String commandName = parts[0];
            String arg = parts[1];

            commandManager.executeCommand(connection, commandName, arg);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onDisconnect(ChatConnection connection) {
        connections.remove(connection);
        connections.forEach(c -> c.sendMessage(connection.getName() + " left the chat"));
    }

    @Override
    public void onError(ChatConnection connection, Exception e) {
        connection.sendMessage("Error occurred: " + e.getMessage());
    }

    @Override
    public void close() throws Exception {
        if (!serverSocket.isClosed()) {
            serverSocket.close();
        }

        executorService.shutdownNow();
    }
}