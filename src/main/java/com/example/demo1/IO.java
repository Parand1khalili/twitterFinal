package com.example.demo1;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class IO {
    static Socket socket;
    static ObjectInputStream in;
    static ObjectOutputStream out;

    public static void IoSetter() throws IOException {
            socket = new Socket("localhost", 9898);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
    }
}
