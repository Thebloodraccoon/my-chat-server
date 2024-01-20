package org.ua.chat.net.connection;


import org.ua.chat.net.server.ChatHandler;

import java.io.*;
import java.net.Socket;
import java.util.Objects;


public class ThreadPoolChatConnection implements ChatConnection, Runnable {
    private final Socket socket;
    private final ChatHandler handler;
    private BufferedReader reader;
    private PrintWriter writer;
    private String name;

    public ThreadPoolChatConnection(Socket socket, ChatHandler handler) {
        this.socket = socket;
        this.handler = handler;
    }

    @Override
    public void sendMessage(String message) {
        writer.println(message);
        writer.flush();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        ThreadPoolChatConnection that = (ThreadPoolChatConnection) object;
        return Objects.equals(socket, that.socket) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(socket, name);
    }

    @Override
    public void run() {
        try (socket) {
            final InputStream inputStream = socket.getInputStream();
            final OutputStream outputStream = socket.getOutputStream();

            reader = new BufferedReader(new InputStreamReader(inputStream));
            writer = new PrintWriter(new OutputStreamWriter(outputStream));

            writer.println("Welcome to the chat. Enter your name:");
            writer.flush();

            name = reader.readLine();

            this.handler.onConnect(this);

            while (!socket.isClosed()) {
                final String message = reader.readLine();
                if (message != null) {
                    handler.onMessage(this, message);
                } else break;
            }


        } catch (IOException e) {
            handler.onError(this, e);
        } finally {
            handler.onDisconnect(this);
        }
    }
}