package de.skillkiller.servermanager.core;

import util.Config;
import util.ConsoleColors;
import util.ServerObject;
import util.Utils;

import java.util.Objects;
import java.util.Scanner;

/**
 * Created by Skillkiller on 09.03.2018.
 */
public class Main {

    //Versionsnummer
    private final static String VERSION = "1.0.1";

    private static Scanner in = new Scanner(System.in);
    private static boolean exit = false;

    public static void main(String[] args) {
        new Config();

        if(!System.getProperty("os.name").contains("nux")) {
            System.out.println("OS: " + System.getProperty("os.name"));
            System.err.println("Dieses Tool ist nur für Linux");
            System.exit(1);
        }

        if (!System.getProperty("user.name").equals("root")) {
            System.out.println("Benutzer: " + System.getProperty("user.name"));
            System.err.println("Dieses Tool muss als Root ausgführt werden");
            System.exit(2);
        }


        //Zum Testen automatisch ein paar Server zur Verfügung haben
        /*for (int i = 0; i < 10 - Config.getServers().size(); i++) {
            Config.createServer(UUID.randomUUID().toString(), (Math.random() < 0.5), "", "", "root");
        }*/

        while (!exit) {
            menue();
        }
        System.exit(0);
    }

    private static void printHeadline() {
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println("###############################");
        System.out.println();
        System.out.println("Screen Manager - " + VERSION);
        System.out.println("Created by Skillkiller");
        System.out.println();
        System.out.println("###############################");
    }

    private static void printServer() {
        for (ServerObject serverObject : Config.getServers()) {
            System.out.println(String.format("%s | %s | %s | %s [%s]",
                    Utils.pad(serverObject.getName(), 50), Utils.pad(serverObject.getBenutzer(), 10),
                    serverObject.isRunning() ?
                            (serverObject.getRestart() ? ConsoleColors.GREEN.print(Utils.pad("Running Loop", 13)) : ConsoleColors.GREEN.print(Utils.pad("Running", 13))) :
                            ConsoleColors.RED.print(Utils.pad("Stopped", 13)),
                    Utils.pad(serverObject.getStartCMD(), 25), Utils.pad(serverObject.getStopCMD(), 25)));
        }
    }

    private static void menue() {
        printHeadline();
        printServer();
        System.out.println();
        System.out.println("Befehle: " + ConsoleColors.PURPLE.print("[start | stop | info | create | delete | exit]"));
        System.out.print("Befehl: ");
        String input = in.nextLine().toLowerCase();
        if (input.contains(" ")) {
            String args[] = input.split(" ");
            if (!args[0].equals("create") && !Config.serverExist(args[1])) return;
            switch (args[0]) {
                case "start":
                    //TODO Server starten
                    Objects.requireNonNull(Config.getServerObject(args[1])).starten();
                    break;

                case "stop":
                    //TODO Server stoppen
                    Objects.requireNonNull(Config.getServerObject(args[1])).stoppen();
                    break;

                case "info":
                    //TODO Server Info ausgeben
                    Objects.requireNonNull(Config.getServerObject(args[1])).printStats();
                    Utils.UserQuestionOk("Gelesen?");
                    break;

                case "create":
                    //TODO Server erstellen
                    if (Utils.userQuestionBoolean("Du willst den Server \"" + args[1] + "\" erstellen?", true)) {
                        boolean restart = Utils.userQuestionBoolean("Auto-Restart?", false);
                        String benutzer = Utils.userQuestionString("Benutzer?", 2);
                        String startCMD = Utils.userQuestionString("Start-Befehl?", 2);
                        String stopCMD = Utils.userQuestionString("Stop-Befehl?");

                        Config.createServer(args[1], restart, startCMD, stopCMD, benutzer);
                    }
                    break;

                case "delete":
                    if (Utils.userQuestionBoolean("Du willst den Server \"" + args[1] + "\" löschen?", false)) {
                        if(Utils.userQuestionBoolean("Server Ordner auch löschen?")) {
                            Objects.requireNonNull(Config.getServerObject(args[1])).getServerDir().delete();
                        }
                        Config.removeServer(args[1]);
                    }
                    break;
            }
        } else {
            if (input.equals("exit")) {
                System.exit(0);
            }
        }
    }
}
