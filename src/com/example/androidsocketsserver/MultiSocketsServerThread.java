package com.example.androidsocketsserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import android.util.Log;

public class MultiSocketsServerThread extends Thread {
    private Socket socket = null;

    public MultiSocketsServerThread(Socket socket) {
        super("KKMultiServerThread");
        this.socket = socket;
    }

    public void run() {

        try {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String inputLine;

            out.println("connection is setup between server and client");

            while ((inputLine = in.readLine()) != null) {
            	Log.d("haha",inputLine);
                out.println("echo " + inputLine);
                if (inputLine.equals("Bye")){
                    break;
                }
            }
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
