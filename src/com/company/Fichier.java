package com.company;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Fichier {
    private File fichier;
    private FileReader reader;
    private String path;

    public Fichier(String path) throws FileNotFoundException {
        fichier = null;
        this.path = path;
        reader = new FileReader(this.path);
    }

    public String lire() throws IOException {

        int i;
        String str = "";
        while((i=reader.read())!=-1) {
            str += (char)i;
        }
        return str;
    }

    public void ecrire(String detailUtilisateur, String message, Date date, String login) throws IOException {
        try(FileWriter fileWriter = new FileWriter(this.path, true)) {
            fileWriter.write(date.toString() + " " + "(" + detailUtilisateur + ") | " + login + ": " + message + "\n");
        } catch (IOException e) {
            // exception handling
        }

    }

    public void writeSave(String saveName, int difficulty, ArrayList<String> code) {
        try(FileWriter fileWriter = new FileWriter(this.path, true)) {
            String str = "";

            for (String s : code) str += "|" + s;

            fileWriter.write(saveName + "|" + difficulty + str + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void ls() {
        String adr = "C:\\Users\\Niloc\\IdeaProjects\\clientTelnet\\src\\com\\company";
        System.out.println("Repertoire courant : " + adr);

        try (Stream<Path> walk = Files.walk(Paths.get(adr))) {
            List<String> result = walk.filter(Files::isRegularFile)
                    .map(x -> x.toString()).collect(Collectors.toList());
            result.forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
