package org.ua.chat.net.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ChatServer implements Server{
    private ServerSocket serverSocket;

    public ChatServer(int port) throws IOException {
        this.serverSocket = new ServerSocket(port);
    }

    @Override
    public void start() throws IOException {
        if(!serverSocket.isClosed()) {
            Socket socket = serverSocket.accept();
        }
    }

    @Override
    public void close() throws Exception {
        if(!serverSocket.isClosed()) {
            serverSocket.close();
        }
    }
}
