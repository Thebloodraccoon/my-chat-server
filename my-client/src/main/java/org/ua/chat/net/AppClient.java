package org.ua.chat.net;

import org.ua.chat.net.client.ChatClient;

import java.io.IOException;

public class AppClient {
    public static void main(String[] args) {
        try (ChatClient chatClient = new ChatClient("localhost", 8000)) {
            chatClient.connect();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
