package org.ua.chat.net.server;

import lombok.Getter;

import java.net.Socket;
import java.time.LocalDateTime;

@Getter
public class ClientConnection {
    private static int nextId = 1;

    private final int id;
    private final String name;
    private final LocalDateTime connectionTime;
    private final Socket clientSocket;

    public ClientConnection(Socket clientSocket) {
        this.id = nextId++;
        this.name = "client-" + id;
        this.connectionTime = LocalDateTime.now();
        this.clientSocket = clientSocket;
    }
}
