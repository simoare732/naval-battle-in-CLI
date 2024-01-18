import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Client {

    private String nameServer;
    private int portServer;
    private Socket mySocket;



    public Client(String nomeServer, int portaServer) {
        this.nameServer = nomeServer;
        this.portServer = portaServer;
    }

    public void connect() {
        try {

            mySocket = new Socket(nameServer, portServer);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public void close() {
        try {
            mySocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Socket getSocket() {
        return mySocket;
    }

}