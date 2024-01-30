package org.ua.chat.net.client;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;


class ChatClientTest {
    private ChatClient client;

    private Socket mockSocket;

    private Scanner mockScanner;

    private PrintWriter mockPrintWriter;

    private BufferedReader mockReader;

    @BeforeEach
    public void setUp() {
        mockSocket = mock(Socket.class);
        mockReader = mock(BufferedReader.class);
        mockScanner = mock(Scanner.class);
        mockPrintWriter = mock(PrintWriter.class);
        client = new ChatClient(mockSocket);
    }

    @Test
    void testHandleUserInput() throws Exception {
        when(mockReader.readLine())
                .thenReturn("Server response");

        when(mockScanner.nextLine())
                .thenReturn("hello");

        client.handleUserInput(mockScanner, mockPrintWriter, mockReader);

        verify(mockPrintWriter).println("hello");
    }
}