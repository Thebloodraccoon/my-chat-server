package org.ua.chat.net.server;

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
    private static final int DEFAULT_THREAD_COUNT = 2;
    private static final int DEFAULT_PORT = 8080;

    private final ServerSocket serverSocket;
    private final List<ChatConnection> connections = new ArrayList<>();
    private final CommandManager commandManager = new CommandManager();

    private final ExecutorService executorService;


    public ChatServer(int port, int threads) throws IOException {
        serverSocket = new ServerSocket(port);
        executorService = Executors.newFixedThreadPool(threads);

        commandManager.registerCommand("-exit", new ExitCommand());
        commandManager.registerCommand("-file", new FileCommand());
    }

    public ChatServer(int port) throws IOException {
        this(port, DEFAULT_THREAD_COUNT);

        commandManager.registerCommand("-exit", new ExitCommand());
        commandManager.registerCommand("-file", new FileCommand());
    }

    public ChatServer() throws IOException {
        this(DEFAULT_PORT, DEFAULT_THREAD_COUNT);

        commandManager.registerCommand("-exit", new ExitCommand());
        commandManager.registerCommand("-file", new FileCommand());
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

            commandManager.executeCommand(connection, commandName,  parts.length > 1 ? parts[1].split(" ") : new String[0]);

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