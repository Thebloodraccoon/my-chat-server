package org.ua.chat.net.client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ChatClient implements Client {
    private final Socket socket;


    public ChatClient(String host, int port) throws IOException {
        this.socket = new Socket(host, port);
    }

    public ChatClient(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void connect() {
        try (
                InputStream inputStream = socket.getInputStream();
                OutputStream outputStream = socket.getOutputStream();
                Scanner scanner = new Scanner(System.in);
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                PrintWriter writer = new PrintWriter(new OutputStreamWriter(outputStream), true);
        ) {
            handleUserInput(scanner, writer, reader);
        } catch (IOException e) {
            System.err.println("Error during communication: " + e.getMessage());
            throw new RuntimeException(e);
        } finally {
            close();
        }
    }

    public void handleUserInput(Scanner scanner, PrintWriter writer, BufferedReader reader) throws IOException {
        while (true) {
            String receivedMessage = reader.readLine();
            System.out.println(receivedMessage);

            String userMessage = scanner.nextLine();

            if (!userMessage.isEmpty()) {
                if (userMessage.equals("-exit")) {
                    close();
                    break;
                }

                if (userMessage.startsWith("-file ")) {
                    sendFile(userMessage.substring("-file ".length()).trim(), writer);
                } else {
                    writer.println(userMessage);
                }
            }
        }
    }

    private void sendFile(String filePath, PrintWriter writer) {
        try (BufferedInputStream fileStream = new BufferedInputStream(new FileInputStream(filePath))) {
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = fileStream.read(buffer)) != -1) {
                writer.println("Sending file...");
                writer.flush();
                socket.getOutputStream().write(buffer, 0, bytesRead);
                socket.getOutputStream().flush();
            }
            System.out.println("File was sent!!!");
        } catch (IOException e) {
            System.err.println("Error sending file: " + e.getMessage());
        }
    }

    @Override
    public void close()  {
        try {
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}