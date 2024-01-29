package org.ua.chat.net.client;


class ChatClientTest {
//    @@Test
//    void testSendMessage() throws Exception {
//        Socket mockSocket = mock(Socket.class);
//        OutputStream mockOutputStream = mock(OutputStream.class);
//        PrintWriter mockPrintWriter = mock(PrintWriter.class);
//
//        when(mockSocket.getOutputStream()).thenReturn(mockOutputStream);
//        whenNew(PrintWriter.class).withArguments(mockOutputStream, true).thenReturn(mockPrintWriter);
//
//        ChatClient chatClient = new ChatClient(mockSocket);
//
//        chatClient.sendMessage("Hello");
//
//        verify(mockPrintWriter).println("Hello");
//        verify(mockPrintWriter).flush();
//    }
//
//    @Test
//    void testReceiveMessage() throws IOException {
//        Socket mockSocket = mock(Socket.class);
//        InputStream mockInputStream = mock(InputStream.class);
//        BufferedReader mockBufferedReader = mock(BufferedReader.class);
//
//        when(mockSocket.getInputStream()).thenReturn(mockInputStream);
//        whenNew(BufferedReader.class).withArguments(new InputStreamReader(mockInputStream)).thenReturn(mockBufferedReader);
//
//        when(mockBufferedReader.readLine()).thenReturn("Server response");
//
//        ChatClient chatClient = new ChatClient(mockSocket);
//
//        String message = chatClient.receiveMessage();
//
//        assertEquals("Server response", message);
//    }
//
//    @Test
//    void testSendFile() throws IOException {
//        Socket mockSocket = mock(Socket.class);
//        OutputStream mockOutputStream = mock(OutputStream.class);
//
//        when(mockSocket.getOutputStream()).thenReturn(mockOutputStream);
//
//        ChatClient chatClient = new ChatClient(mockSocket);
//
//        chatClient.sendFile("test-file.txt");
//
//        // Add verification based on your implementation
//    }
}