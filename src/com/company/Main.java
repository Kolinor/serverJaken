package com.company;

import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        Server t = new Server();
        t.connexion(6002, "192.168.0.33");
        t.run();
    }
}
