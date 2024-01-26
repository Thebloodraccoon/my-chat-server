# My-chat-server

Server is represented by a separate maven module. 
Server can accept incoming connections from clients in an unlimited number.
Server supports a set of special commands to clients:

    -exit : Disconnects the client from the connection.

    -file file_path_here - Sends a file to the server.

The "client" is represented by a separate maven module.
The client is able to connect to the server, receive messages from the server and transmit


