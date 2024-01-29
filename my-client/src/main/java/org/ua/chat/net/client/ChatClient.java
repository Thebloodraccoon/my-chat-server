package org.ua.chat.net.client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ChatClient implements Client {
    private final Socket socket;

    public ChatClient(String host, int port) throws IOException {
        this.socket = new Socket(host, port);
    }

    @Override
    public void connect() {
        try (
                InputStream inputStream = socket.getInputStream();
                OutputStream outputStream = socket.getOutputStream();
                PrintWriter writer = new PrintWriter(new OutputStreamWriter(outputStream));
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))
        ) {

            while (true) {
                String s = reader.readLine();
                System.out.println(s);

                final Scanner scanner = new Scanner(System.in);
                final String message = scanner.nextLine();

                if (!message.isEmpty()) {
                    if (message.equals("-exit")) {
                        close();
                        break;
                    }

                    if (message.startsWith("-file ")) {
                        sendFile(message, outputStream);
                    } else {
                        writer.println(message);
                        writer.flush();
                    }
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            close();
        }
    }

    public void sendFile(String userCommand, OutputStream outputStream) {
        String filePath = userCommand.substring("-file ".length()).trim();

        try (BufferedInputStream fileStream = new BufferedInputStream(new FileInputStream(filePath))) {
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = fileStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
                outputStream.flush();
            }
            System.out.println("File was sent!!!");
        } catch (IOException e) {
            System.out.println("Error sending file: " + e.getMessage());
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