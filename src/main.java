import java.io.*;
import java.net.Socket;
import java.util.Random;
import java.util.Scanner;

public class main {
    public static void main(String[] args) throws IOException {
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader tas = new BufferedReader(input);
        Scanner tastiera = new Scanner(System.in);

        Socket socket = null;

        FileReader fr = new FileReader("mappa.txt");
        BufferedReader in = new BufferedReader(fr);

        String HoC = "";
        boolean valida = false;
        while (!valida) {
            System.out.println("Vuoi hostare o connetterti ad un host?, inserire Host o Connettimi");
            HoC = tas.readLine().toLowerCase();

            if (HoC.equals("host")) {
                Server s = new Server(7000);
                s.waitConnection();
                socket = s.getSocket();
                valida = true;
            } else if (HoC.equals("connettimi")) {
                Client c = new Client("127.0.0.1", 7000);
                c.connect();
                socket = c.getSocket();
                valida = true;
            } else {
                System.err.println("Comando non riconosciuto");
            }
        }

        DataOutputStream to = new DataOutputStream(socket.getOutputStream());
        BufferedReader from = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        Random random = new Random();
        int turno = 0;

        if (HoC.equals("host")) {
            turno = random.nextInt(1 + 0) + 0;
            if(turno == 0)
                Multiplayer.invia("1", to);
            else
                Multiplayer.invia("0", to);
        } else if (HoC.equals("connettimi")) {
            turno = Integer.parseInt(Multiplayer.ricevi(from));
        }

        String x; //La prima stringa che conterrà il numero di righe e colonne
        String [] x1 = new String [2]; //Un array di 2 stringhe
        int [] n = new int [2]; //Un array di 2 interi che conterrà righe e colonne

        x = in.readLine(); //Legge la prima riga
        x1 = x.split(" "); //Divide la prima riga in 2 parti

        //Lettura grandezza righe e colonne
        for(int i=0; i<2; i++) {
            n[i] = Integer.parseInt(x1[i]); //Trasforma la prima e seconda stringa in interi
        }

        int NR = n[0]; //Numero di righe totali
        int NC = n[1]; //Numero di colonne totali

        //Instanzio array di stringhe che conterrà la mappa
        String [] mappa = new String [NR];

        //Carico l'array di Stringhe
        for(int i=0;i<NR;i++)
            mappa[i] = in.readLine();

        //Carico l'array della mappa del client
        String [] mappa2 = new String [NR];
        StringBuilder l = new StringBuilder(NC);
        for(int i=0;i<NC;i++){
            l.append('?');
        }
        //Carico l'array di Stringhe
        for(int i=0;i<NR;i++) {
            mappa2[i] = l.toString();
        }

        if(Multiplayer.checkMap(mappa, NC, NR) == false)
            System.out.println("La mappa non e' valida");

        int R, C = 0;
        int colpo, colpoA = 0;
        boolean giocoAvversario = true;

        System.out.println("Mappa personale");
        for(int i=0;i<NR;i++)
            System.out.println(mappa[i]);
        System.out.println("Mappa avversario");
        for(int i=0;i<NR;i++)
            System.out.println(mappa2[i]);

        //Inizia il gioco
        while(Multiplayer.giocoNonTerminato(mappa,NC,NR) && giocoAvversario){



            if(turno==0){
                System.out.println("Inserisci le coordinate del colpo nel formato R C: ");
                R = tastiera.nextInt();
                C = tastiera.nextInt();

                Multiplayer.invia(R+"", to);
                Multiplayer.invia(C+"", to);

                colpo = Integer.parseInt(Multiplayer.ricevi(from));

                StringBuilder ns = new StringBuilder(mappa2[R]);

                if(colpo == 0){
                    ns.setCharAt(C, 'o');
                    mappa2[R] = ns.toString();
                    System.out.println("mare");
                    turno = 1;
                }
                else if(colpo == 1){
                    ns.setCharAt(C, 'v');
                    mappa2[R] = ns.toString();
                    boolean Affondato = Boolean.valueOf(Multiplayer.ricevi(from));
                    if(Affondato)
                        System.out.println("Nave colpita e affondata");
                    else
                        System.out.println("Nave colpita");
                }

                System.out.println("Mappa avversario");
                for(int i=0;i<NR;i++)
                    System.out.println(mappa2[i]);

                giocoAvversario = Boolean.valueOf(Multiplayer.ricevi(from));
            }
            else if(turno==1){
                System.out.println("è il turno dell'avversario");
                R = Integer.parseInt(Multiplayer.ricevi(from));
                C = Integer.parseInt(Multiplayer.ricevi(from));


                colpoA = Multiplayer.colpisci(mappa,R,C);
                Multiplayer.invia(colpoA+"", to);
                if(colpoA == 1){
                    if(Multiplayer.colpitoEAffondato(mappa, R, C, NC-1, NR-1))
                        Multiplayer.invia(String.valueOf(true), to);
                    else
                        Multiplayer.invia(String.valueOf(false), to);
                }
                if(colpoA == 0)
                    turno = 0;

                System.out.println("Mappa personale");
                for(int i=0;i<NR;i++)
                    System.out.println(mappa[i]);

                Multiplayer.invia(String.valueOf(Multiplayer.giocoNonTerminato(mappa,NC,NR)), to);
            }
        }

        tastiera.close();
        if(!giocoAvversario)
            System.out.println("Tutte le navi avversarie sono state colpite, gioco terminato");
        else
            System.out.println("Tutte le tue navi sono state colpite, gioco terminato");
    }
}
