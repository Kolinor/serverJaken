package com.company;

import java.io.*;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Date;

import static com.company.Server.*;

public class ClientProcessor implements Runnable {

    private Socket sock;
    private BufferedInputStream reader;
    public String login;
    public int difficulty;
    private PrintStream ecritureEcran;
    static public String toMessage;
    static public String toLogin;
    private ArrayList<String> retourClient;
    boolean selectDifficulty;
    ArrayList<String[]> arrSave;

    public ClientProcessor(Socket pSock){
        sock = pSock;
        reader = null;
    }

    //Le traitement lancé dans un thread séparé
    public void run(){
        System.err.println("Traitement du client");
        boolean fisrtConnexion = true;
        boolean closeConnexion = false;

        while(!sock.isClosed()){

            try {
                ecritureEcran = new PrintStream(sock.getOutputStream(), true);
                reader = new BufferedInputStream(sock.getInputStream());
                InputStream inputStream = sock.getInputStream();
                if(fisrtConnexion) {
                    login = read();
                    logins.add(login);
                    fisrtConnexion = false;
                }

                String response = read();

                InetSocketAddress remote = (InetSocketAddress)sock.getRemoteSocketAddress();

                String console = "";
                console = "/" + remote.getAddress().getHostAddress() + " ("+ this.login + ")" +  ">" + response;

                System.out.println("\n" + console);
                send(response);

                if(response.equals("quit".toLowerCase())){
                    send("Connexion closed");
                    System.err.println("Connexion closed ");
                    deconnexionLogin();
                    reader = null;
                    sock.close();
                    break;
                }
            } catch(SocketException e){
                deconnexionLogin();
                System.err.println("Connexion interrompu ! ");
                break;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String[] parseResponse(String response) {
        return response.split(" ");
    }

    private String read() throws IOException{
        String response = "";
        InetSocketAddress remote = (InetSocketAddress)sock.getRemoteSocketAddress();
        String host = remote.getAddress().getHostAddress();

        Date date = new Date();
        int stream;
        byte[] b = new byte[4096];
        stream = reader.read(b);
        response = new String(b, 0, stream);
        Fichier file = new Fichier("C:\\jakenServer\\src\\com\\company\\log.txt");
        file.ecrire(host, response, date, this.login);
        return response;
    }

    private void send(String message) {
        ecritureEcran.println(message);
        ecritureEcran.flush();
    }

    private void deconnexionLogin() {
        for (int i = 0; i < logins.size(); i++) {
            if (logins.get(i).equals(login))
                logins.remove(i);
        }
    }
}