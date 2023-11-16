package sdf;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Program {
    public static final int DEFAULT_PORT = 3000;
    public static final String DEFAULT_NAME = "localhost";
    public static void main(String[] args) throws Exception {
        
        // no parameter given
        if (args.length == 0){
            System.out.printf("Attempting to connect to 127.0.0.1 on port %d\n", DEFAULT_PORT);
            Socket socket = new Socket (DEFAULT_NAME, DEFAULT_PORT);
            System.out.println("Connected to server");
        }
        // one parameter
        else if (args.length == 1){
            int port = Integer.parseInt(args[0]);
            System.out.printf("Attempting to connect to 127.0.0.1 on port %d\n", port);
            Socket socket = new Socket (DEFAULT_NAME, port);
            System.out.println("Connected to server");
        }
        // two parameter
        else if (args.length == 2){
            System.out.printf("Attempting to connect to 127.0.0.1 on port %d\n", args[1]);
            Socket socket = new Socket (args[0], Integer.parseInt(args[1]));
            socket.getInetAddress();
            System.out.println("Connected to server");
        }


        

    }
}
