package org.ua.chat.net.server;

import org.junit.jupiter.api.*;

import java.io.*;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;

//
//public class ChatServerTest {
//    private ChatServer chatServer;
//
//    @BeforeEach
//    public void setup() {
//        try {
//            chatServer = new ChatServer(8081, 3);
//            Thread serverThread = new Thread(() -> {
//                try {
//                    chatServer.start();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            });
//            serverThread.start();
//
//            chatServer.start();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @AfterEach
//    public void teardown() {
//        try {
//            chatServer.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//
//
//    @Test
//    public void testOnConnect() {
//        try (Socket сlientSocket = new Socket("localhost", 8081)) {
//            assertFalse(chatServer.getConnections().isEmpty(), "Connection should be established");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Test
//    public void testOnMessage() {
//        try (
//                Socket receiverSocket = new Socket("localhost", 8081);
//                BufferedReader receiverReader = new BufferedReader(new InputStreamReader(receiverSocket.getInputStream()))
//        ){
//            Thread.sleep(1000);
//            String sentMessage = "Hello, Server!";
//            testOnMessageSender(sentMessage);
//
//            Thread.sleep(1000);
//
//            String receivedMessage = receiverReader.readLine();
//            assertTrue(chatServer.getConnections().size() > 1, "At least two connections should be established");
//
//            assertEquals(sentMessage, receivedMessage, "Sent and received messages should match");
//        } catch (IOException | InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//
//    }
//
//    private void testOnMessageSender(String message) {
//        try (Socket senderSocket = new Socket("localhost", 8081);
//             OutputStream senderOutputStream = senderSocket.getOutputStream();
//             PrintWriter senderWriter = new PrintWriter(senderOutputStream, true))
//        {
//            senderWriter.println(message);
//
//            Thread.sleep(1000);
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//}