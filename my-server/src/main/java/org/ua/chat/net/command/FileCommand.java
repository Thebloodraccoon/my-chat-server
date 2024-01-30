package org.ua.chat.net.command;

import org.ua.chat.net.connection.ChatConnection;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileCommand implements Command {
    private static final String SERVER_FILES_DIRECTORY = "server-files/";

    @Override
    public void execute(ChatConnection connection, String filePath) {
        try {
            Path path = Paths.get(filePath);
            File outputFile = new File(SERVER_FILES_DIRECTORY + path.getFileName().toString());
            receiveAndSaveFile(connection, outputFile);

        } catch (IOException e) {
            handleFileReceiveError(e);
        }
    }

    private void receiveAndSaveFile(ChatConnection connection, File outputFile) throws IOException {
        try (
                InputStream fileStream = new BufferedInputStream(connection.getSocket().getInputStream());
                BufferedOutputStream fileOutputStream = new BufferedOutputStream(new FileOutputStream(outputFile))
        ) {
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = fileStream.read(buffer)) >= 0) {
                fileOutputStream.write(buffer, 0, bytesRead);
                fileOutputStream.flush();
            }
            System.out.println("File received and saved: " + outputFile.getAbsolutePath());
        }
    }


    private void handleFileReceiveError(IOException e) {
        System.out.println("Error receiving file: " + e.getMessage());
    }
}