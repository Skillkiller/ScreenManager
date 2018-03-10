package de.skillkiller.servermanager.core;

import util.Config;
import util.ServerObject;
import util.Utils;

import java.util.Objects;
import java.util.Scanner;
import java.util.UUID;

/**
 * Created by Skillkiller on 09.03.2018.
 */
public class Main {

    //Versionsnummer
    private final static String VERSION = "1.0.1";

    private static Scanner in = new Scanner(System.in);

    public static void main(String[] args) {
        new Config();
        //Zum Testen automatisch ein paar Server zur Verfügung haben
        for (int i = 0; i < 10 - Config.getServers().size(); i++) {
            Config.createServer(UUID.randomUUID().toString(), (Math.random() < 0.5), "", "", "root");
        }

        while (true) {
            printMenue();
        }
    }

    private static void printHeadline() {
        System.out.println("Screen Manager - " + VERSION);
        System.out.println("Created by Skillkiller");
        System.out.println();
    }

    private static void printServer() {
        for (ServerObject serverObject : Config.getServers()) {
            System.out.println(String.format("%s | %s | %s | %s [%s]",
                    Utils.pad(serverObject.getName(), 50), Utils.pad(serverObject.getBenutzer(), 10),
                    serverObject.isRunning() ? "Running" : "Stopped",
                    Utils.pad(serverObject.getStartCMD(), 25), Utils.pad(serverObject.getStopCMD(), 25)));
        }
    }

    private static void printMenue() {
        printHeadline();
        printServer();

        System.out.println();
        System.out.println("Befehle: [start | stop | info | create | delete]");
        System.out.println("Befehl: ");
        String input[] = in.nextLine().toLowerCase().split(" ");
        if(!Config.serverExist(input[1]) && !input[0].equals("create")) return;
        switch (input[0]) {
            case "start":
                //TODO Server starten
                break;

            case "stop":
                //TODO Server stoppen
                break;

            case "info":
                //TODO Server Info ausgeben
                Objects.requireNonNull(Config.getServerObject(input[1])).printStats();
                Utils.UserQuestionOk("Gelesen?");
                break;

            case "create":
                //TODO Server erstellen
                if (Utils.userQuestionBoolean("Du willst den Server \"" + input[1] + "\" erstellen?", true)) {
                    boolean restart = Utils.userQuestionBoolean("Auto-Restart?", false);
                    String benutzer = Utils.userQuestionString("Benutzer?", 2);
                    String startCMD = Utils.userQuestionString("Start-Befehl?", 2);
                    String stopCMD = Utils.userQuestionString("Stop-Befehl?", 2);

                    Config.createServer(input[1], restart, startCMD, stopCMD, benutzer);
                }
                break;

            case "delete":
                if (Utils.userQuestionBoolean("Du willst den Server \"" + input[1] + "\" löschen?", false)) {
                    if(Utils.userQuestionBoolean("Server Ordner auch löschen?")) {
                        Objects.requireNonNull(Config.getServerObject(input[1])).getServerDir().delete();
                    }
                    Config.removeServer(input[1]);
                }
                break;
        }
    }
}
