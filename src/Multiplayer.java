import java.io.*;
import java.net.*;
import java.util.*;

public class Multiplayer {

    public static boolean checkMap (String[] mappa, int NC, int NR){
        boolean valido = true;

        for(int i=0; i<NR;i++){
            for(int j=0;j<NC;j++){
                //Il primo if controlla che non ci troviamo ai bordi della mappa per il controllo della mappa
                if(mappa[i].charAt(j)=='v' && i!=0 && i!=NR-1 && j!=0 && j!=NC-1){
                    //Questo if controlla che in obliquo non ci siano navi
                    if(mappa[i-1].charAt(j-1)=='v' || mappa[i-1].charAt(j+1)=='v' || mappa[i+1].charAt(j-1)=='v' || mappa[i+1].charAt(j+1)=='v')
                        valido = false;
                }
                //Questo if controlla il caso in cui ci troviamo nella prima riga
                else if(mappa[i].charAt(j)=='v' && i==0){
                    if(j==0){
                        if(mappa[i+1].charAt(j+1)=='v')
                            valido = false;
                    }
                    else if(j==NC-1){
                        if(mappa[i+1].charAt(j-1)=='v')
                            valido = false;
                    }
                    else{
                        if(mappa[i+1].charAt(j-1)=='v' || mappa[i+1].charAt(j+1)=='v')
                            valido=false;
                    }

                }
                //Questo if controlla il caso in cui ci troviamo all'ultima riga
                else if(mappa[i].charAt(j)=='v' && i==NR-1) {
                    if(j==0) {
                        if(mappa[i-1].charAt(j+1)=='v')
                            valido = false;
                    }
                    else if(j==NC-1) {
                        if(mappa[i-1].charAt(j-1)=='v')
                            valido = false;
                    }
                    else {
                        if(mappa[i-1].charAt(j+1)=='v' || mappa[i-1].charAt(j+1)=='v')
                            valido = false;
                    }
                }
                //Questo if controlla il caso in cui ci troviamo nella prima colonna
                else if(mappa[i].charAt(j)=='v' && j==0){
                    if(mappa[i-1].charAt(j+1)=='v' || mappa[i+1].charAt(j+1)=='v')
                        valido = false;
                }
                //Questo if controlla il caso in cui ci troviamo nell'ultima colonna
                else if(mappa[i].charAt(j)=='v' && j==NC-1) {
                    if(mappa[i-1].charAt(j-1)=='v' || mappa[i+1].charAt(j-1)=='v')
                        valido = false;
                }
            }
        }
        return valido;
    }

    public static int colpisci(String[] mappa, int x, int y) {

        int colpo = 0;
        StringBuilder ns = new StringBuilder( mappa[x] );
        //StringBuilder serve per agire sulle stringhe singole



        if(mappa[x].charAt(y) == 'o'){
            colpo = 0;
            ns.setCharAt(y, 'i'); //Questo cambia la o in i nello StringBuilder
            mappa[x] = ns.toString(); //Copia all'interno della mappa la stringa modificata
            //mappa[x].charAt(y)='i';
        }

        else if(mappa[x].charAt(y) == 'v') {
            colpo = 1;
            ns.setCharAt(y, 'x');
            mappa[x] = ns.toString();
            //mappa[x].charAt(y)='x';

        }

        else if(mappa[x].charAt(y) == 'i' || mappa[x].charAt(y) == 'x')
            colpo = 2;

        return colpo;
    }

    public static boolean giocoNonTerminato(String[] mappa, int NC, int NR) {
        boolean finish = false;
        for(int i=0; i<NR; i++) {
            for(int j=0; j<NC; j++) {
                if(mappa[i].charAt(j)=='v') {
                    finish = true;
                }
            }
        }
        return finish;
    }

    public static boolean colpitoEAffondato(String[] mappa, int x, int y, int NC, int NR){
        boolean nonAffondatoDx = false;
        boolean nonAffondatoSx = false;
        boolean nonAffondatoUp = false;
        boolean nonAffondatoDown = false;
        boolean orizzontale = false;
        boolean verticale = false;
        if(x>0 && x<NR && (mappa[x+1].charAt(y) == 'o' || mappa[x+1].charAt(y) == 'i') && (mappa[x-1].charAt(y) == 'o' || mappa[x-1].charAt(y) == 'i'))
            orizzontale = true; //controllo se la nave è orizzontale
        else if(x == 0 && (mappa[x+1].charAt(y) == 'o' || mappa[x+1].charAt(y)=='i'))
            orizzontale = true;
        else if(x == NR && (mappa[x-1].charAt(y) == 'o' || mappa[x-1].charAt(y) == 'i'))
            orizzontale = true;
        if(y>0 && y<NC && (mappa[x].charAt(y+1) == 'o' || mappa[x].charAt(y+1) == 'i') && (mappa[x].charAt(y-1) == 'o' || mappa[x].charAt(y-1) == 'i'))
            verticale = true; //controllo se la nave è verticale
        else if(y == 0 && (mappa[x].charAt(y+1) == 'o' || mappa[x].charAt(y+1) == 'i'))
            verticale = true;
        else if(y == NC && (mappa[x].charAt(y-1) == 'o' || mappa[x].charAt(y-1) == 'i'))
            verticale = true;

        //Ora controllo il colpito e affondato

        if(orizzontale) {
            if (y > 0 && y < NC) { //Se non è ai bordi
                for (int i = y + 1; i < NC; i++) { //controllo se da destra è affondata
                    if (mappa[x].charAt(i) == 'o' || mappa[x].charAt(i) == 'i')
                        break;
                    else if (mappa[x].charAt(i) == 'v') {
                        nonAffondatoDx = true;
                        break;
                    }
                }
                for (int i = y - 1; i > 0; i--) { //controllo se da sinistra è affondata
                    if (mappa[x].charAt(i) == 'o' || mappa[x].charAt(i) == 'i')
                        break;
                    else if (mappa[x].charAt(i) == 'v') {
                        nonAffondatoSx = true;
                        break;
                    }
                }
            } else if (y == 0) { //Se è al bordo di sinistra
                for (int i = y + 1; i < NC; i++) {
                    if (mappa[x].charAt(i) == 'o' || mappa[x].charAt(i) == 'i')
                        break;
                    else if (mappa[x].charAt(i) == 'v') {
                        nonAffondatoDx = true;
                        break;
                    }
                }
            } else if (y == NC) { // Se è al bordo di destra
                for (int i = y - 1; i > 0; i--) {
                    if (mappa[x].charAt(i) == 'o' || mappa[x].charAt(i) == 'i')
                        break;
                    else if (mappa[x].charAt(i) == 'v') {
                        nonAffondatoSx = true;
                        break;
                    }
                }
            }
            if(!nonAffondatoDx && !nonAffondatoSx)
                return true;
        }else if(verticale){
            if(x > 0 && x<NR){
                for(int i=x+1;i<NR;i++){ //controllo se dall'alto è affondata
                    if(mappa[i].charAt(y) == 'o' || mappa[i].charAt(y) == 'i')
                        break;
                    else if(mappa[i].charAt(y) == 'v'){
                        nonAffondatoUp = true;
                        break;
                    }
                }
                for(int i=x-1;i>0;i--){ //controllo se dal basso è affondata
                    if(mappa[i].charAt(y) == 'o' || mappa[i].charAt(y) == 'i')
                        break;
                    else if(mappa[i].charAt(y) == 'v'){
                        nonAffondatoDown = true;
                        break;
                    }
                }
            }else if(x == 0){ // Nel caso ci troviamo nella prima riga
                for(int i=x-1;i>0;i--){ //controllo se dal basso è affondata
                    if(mappa[i].charAt(y) == 'o' || mappa[i].charAt(y) == 'i')
                        break;
                    else if(mappa[i].charAt(y) == 'v'){
                        nonAffondatoDown = true;
                        break;
                    }
                }
            }else if(x == NR){ //Nel caso ci troviamo all'ultima riga
                for(int i=x+1;i<NR;i++){ //controllo se dall'alto è affondata
                    if(mappa[i].charAt(y) == 'o' || mappa[i].charAt(y) == 'i')
                        break;
                    else if(mappa[i].charAt(y) == 'v'){
                        nonAffondatoUp = true;
                        break;
                    }
                }
            }
            if(!nonAffondatoUp && !nonAffondatoDown)
                return true;
        }
        return false;
    }

    public static void invia(String s, DataOutputStream to) {
        try {
            to.writeBytes(s + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static String ricevi(BufferedReader from) throws IOException {
        String stringFromClient = from.readLine();
        return stringFromClient;
    }
}
