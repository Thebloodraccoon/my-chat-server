package org.ua.chat.net.connection;


import lombok.EqualsAndHashCode;
import org.ua.chat.net.server.ChatHandler;

import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.Random;


@EqualsAndHashCode
public class ThreadChatConnection implements ChatConnection, Runnable {
    private final Socket socket;
    private final ChatHandler handler;
    private final LocalDateTime connectionTime;
    private String name;
    private BufferedReader reader;
    private PrintWriter writer;

    public ThreadChatConnection(Socket socket, ChatHandler handler) {
        this.socket = socket;
        this.handler = handler;
        this.name = generateRandomName();
        this.connectionTime = LocalDateTime.now();
    }

    private String generateRandomName() {
        Random random = new Random();
        return "client-" + random.nextInt(1000);
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
    public Socket getSocket() {
        return socket;
    }
    @Override
    public void run() {
        try (socket;
             final InputStream inputStream = socket.getInputStream();
             final OutputStream outputStream = socket.getOutputStream();
        ) {
            reader = new BufferedReader(new InputStreamReader(inputStream));
            writer = new PrintWriter(new OutputStreamWriter(outputStream), true);

            writer.println("Welcome " + getName() + " to the chat. ");

            this.handler.onConnect(this);

            while (!socket.isClosed()) {
                final String message = reader.readLine();
                if (message != null && !message.isEmpty()) {
                    if (isCommand(message)) {
                        handler.onCommand(this, message);
                    } else {
                        handler.onMessage(this, message);
                    }
                }
            }


        } catch (IOException e) {
            handler.onError(this, e);
        }
    }

    private boolean isCommand(String message) {
        return message.startsWith("-");
    }

    @Override
    public void close() {
        try {
            handler.onDisconnect(this);
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}