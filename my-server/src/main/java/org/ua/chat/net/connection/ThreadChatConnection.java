package org.ua.chat.net.connection;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.ua.chat.net.server.ChatHandler;

import java.io.*;
import java.net.Socket;



@EqualsAndHashCode
public class ThreadChatConnection implements ChatConnection, Runnable {
    private final Socket socket;
    private final ChatHandler handler;
    private BufferedReader reader;
    private PrintWriter writer;
    private String name;

    public ThreadChatConnection(Socket socket, ChatHandler handler) {
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
    public void run() {
        try (socket;
             final InputStream inputStream = socket.getInputStream();
             final OutputStream outputStream = socket.getOutputStream();
        ) {
            reader = new BufferedReader(new InputStreamReader(inputStream));
            writer = new PrintWriter(new OutputStreamWriter(outputStream), true);

            writer.println("Welcome to the chat. Enter your name:");

            name = reader.readLine();

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