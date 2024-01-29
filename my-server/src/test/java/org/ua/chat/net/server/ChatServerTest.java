package org.ua.chat.net.server;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.ua.chat.net.connection.ChatConnection;
import org.ua.chat.net.connection.ThreadChatConnection;


import java.io.IOException;
import java.net.Socket;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;


public class ChatServerTest {
    public ChatServer chatServer;
    @Mock
    private ChatConnection connection;

    @BeforeEach
    public void setUp() {
        try {
            this.chatServer = new ChatServer(8080, 5);
            this.connection = mock(ChatConnection.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @AfterEach
    public void tearDown() {
        try {
            chatServer.close();
            Mockito.reset(connection);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testOnConnect() {
        chatServer.onConnect(connection);

        assertEquals(1, chatServer.getConnections().size());
    }


    @Test
    public void testOnCommand() {
        ChatConnection connection1 = new ThreadChatConnection(new Socket(), chatServer);
        chatServer.onConnect(connection1);

        chatServer.onCommand(connection1, "-exit ");

        assertEquals(0, chatServer.getConnections().size());
    }

    @Test
    public void testOnDisconnect() {
        chatServer.onConnect(connection);

        assertEquals(1, chatServer.getConnections().size());


        chatServer.onDisconnect(connection);

        assertEquals(0, chatServer.getConnections().size());
    }

    @Test
    public void testOnError() {
        chatServer.onConnect(connection);

        chatServer.onError(connection, new IOException("Test error"));

        verify(connection, times(1)).sendMessage(anyString());
    }

    @Test
    public void testClose() throws Exception {
        chatServer.close();

        assertTrue(chatServer.isClosed());
    }
}