import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private int portServer;
    private Socket client;
    private ServerSocket server;


    public Server(int portServer) {
        this.portServer = portServer;
        try {
            this.server = new ServerSocket(this.portServer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void waitConnection() {
        try {
            System.out.println("Waiting for a connection...");
            client = server.accept();
            System.out.println("Client connected..."+client.getInetAddress()+"...");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public void close() {
        try {
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Socket getSocket(){
        return client;
    }
}