package org.ua.chat.net.client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ChatClient implements Client {
    private final Socket socket;

    public ChatClient(String host, int port) throws IOException {
        this.socket = new Socket(host, port);
    }

    public void connect() {
        try (
                InputStream inputStream = socket.getInputStream();
                OutputStream outputStream = socket.getOutputStream();
                PrintWriter writer = new PrintWriter(new OutputStreamWriter(outputStream));
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))
        ) {
            while (true) {
                final String s = reader.readLine();
                System.out.println(s);

                final Scanner scanner = new Scanner(System.in);
                final String userMessage = scanner.nextLine();

                if (!(s.isEmpty())) {
                    writer.println(userMessage);
                    writer.flush();

                    if (s.equals("-exit")) close();
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void close() throws Exception {
        socket.close();
    }
}