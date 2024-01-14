package org.ua.chat.net.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatServer implements Server {
    private final ServerSocket serverSocket;
    private final ExecutorService executorService;
    private final List<ClientConnection> connections = new ArrayList<>();

    public ChatServer(int port) throws IOException {
        this.serverSocket = new ServerSocket(port);
        this.executorService = Executors.newCachedThreadPool();
    }

    @Override
    public void start() throws IOException {
        System.out.println("Server started. Waiting for clients...");

        while (!serverSocket.isClosed()) {
            Socket clientSocket = serverSocket.accept();
            ClientConnection clientConnection = new ClientConnection(clientSocket);
            connections.add(clientConnection);
            executorService.submit(() -> handleClient(clientConnection));
        }

    }

    @Override
    public void close() throws Exception {
        if (!serverSocket.isClosed()) {
            serverSocket.close();
            executorService.shutdown();
            System.out.println("Server closed");
        }
    }

    private void handleClient(ClientConnection clientConnection) {
        try (
                BufferedReader reader = new BufferedReader(new InputStreamReader(clientConnection.getClientSocket().getInputStream()));
                PrintWriter writer = new PrintWriter(clientConnection.getClientSocket().getOutputStream(), true))
        {

            writer.println("Welcome to the Chat Server!");
            writer.println("Your ID is: " + clientConnection.getId());
            writer.println("Connection time: " + clientConnection.getConnectionTime());

            String clientMessage;
            while ((clientMessage = reader.readLine()) != null) {
                System.out.println("Received message from " + clientConnection.getName() + ": " + clientMessage);

                writer.println("Server: Message received - " + clientMessage);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            connections.remove(clientConnection);
            System.out.println("Client disconnected: " + clientConnection.getName());
        }
    }
}
