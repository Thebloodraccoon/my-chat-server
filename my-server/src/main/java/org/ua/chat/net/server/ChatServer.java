package org.ua.chat.net.server;

import org.ua.chat.net.connection.ChatConnection;
import org.ua.chat.net.connection.ThreadChatConnection;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolChatServer implements Server, ChatHandler, AutoCloseable {
    private static final int DEFAULT_THREAD_COUNT = 2;
    private static final int DEFAULT_PORT = 8080;

    private final ServerSocket serverSocket;
    private final List<ChatConnection> connections = new ArrayList<>();
    private final ExecutorService executorService;


    public ThreadPoolChatServer(int port, int threads) throws IOException {
        serverSocket = new ServerSocket(port);
        executorService = Executors.newFixedThreadPool(threads);
    }

    public ThreadPoolChatServer(int port) throws IOException {
        this(port, DEFAULT_THREAD_COUNT);
    }

    public ThreadPoolChatServer() throws IOException {
        this(DEFAULT_PORT, DEFAULT_THREAD_COUNT);
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
        connections.forEach(c -> c.sendMessage(connection.getName() + " entered the chat"));
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