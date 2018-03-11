package de.skillkiller.servermanager.core;

import util.Config;
import util.ConsoleColors;
import util.ServerObject;
import util.Utils;

import java.io.File;
import java.util.Objects;
import java.util.Scanner;

/**
 * Created by Skillkiller on 09.03.2018.
 */
public class Main {

    //Versionsnummer
    private final static String VERSION = "1.5.2";

    private static Scanner in = new Scanner(System.in);

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

        int deleted = 0;
        int added = 0;

        for (ServerObject server : Config.getServers()) {
            if(!server.getServerDir().exists() || !server.getServerDir().isDirectory()) {
                Config.removeServer(server.getName());
                deleted++;
            }
        }

        for (File file : Objects.requireNonNull(Config.getWorkingDir().listFiles())) {
            if(file.isDirectory() && !Config.serverExist(file.getName())) {
                Config.createServer(file.getName(), false, "./start.sh", "", "root");
                added++;
            }
        }

        if(added > 0) System.out.println(ConsoleColors.GREEN.bold(String.valueOf(added)) + ConsoleColors.GREEN.print(" Server wurden hinzugefügt"));
        if(deleted > 0) System.out.println(ConsoleColors.RED.bold(String.valueOf(deleted)) + ConsoleColors.RED.print(" Server wurden gelöscht"));

        boolean exit = false;
        while (!exit) {
            printMainMenue();
        }
        System.exit(0);
    }

    private static void printHeadline() {
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
                    Utils.pad(serverObject.getStartCMD(), 25), Utils.pad(serverObject.getRestart() ? "Auto Restart" : "No Restart", 10)));
        }
    }

    private static void printMainMenue() {
        printHeadline();
        printServer();
        System.out.println();
        System.out.println("Befehle: " + ConsoleColors.PURPLE.print("[start | stop | info | create | delete | modify | exit]"));
        System.out.print("Befehl: ");
        String input = in.nextLine().toLowerCase();
        System.out.println();
        if (input.contains(" ")) {
            String args[] = input.split(" ");
            if (!args[0].equals("create") && !Config.serverExist(args[1])) return;
            switch (args[0]) {
                case "start":
                    Objects.requireNonNull(Config.getServerObject(args[1])).starten();
                    System.out.println(ConsoleColors.GREEN.print("Starte den ausgwählten Server"));
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ignored) { }
                    break;

                case "stop":
                    Objects.requireNonNull(Config.getServerObject(args[1])).stoppen();
                    System.out.println(ConsoleColors.GREEN.print("Stoppe den ausgwählten Server"));
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ignored) { }
                    break;

                case "info":
                    Objects.requireNonNull(Config.getServerObject(args[1])).printStats();
                    Utils.UserQuestionOk("Gelesen?");
                    break;

                case "create":
                    if (Utils.userQuestionBoolean("Du willst den Server \"" + args[1] + "\" erstellen?", true)) {
                        boolean restart = Utils.userQuestionBoolean("Auto-Restart?", false);
                        String benutzer = Utils.userQuestionString("Benutzer?", 2);
                        String startCMD = Utils.userQuestionString("Start-Befehl?", 2);
                        String stepCMD = Utils.userQuestionString("Zwischen-Befehl?");

                        Config.createServer(args[1], restart, startCMD, stepCMD, benutzer);
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

                    //TODO Modify Befehl buggy
                /*case "modify":
                    printModifyMenue(args[1]);
                    break;*/
            }
        } else {
            if (input.equals("exit") || input.equals("e")) {
                System.exit(0);
            }
        }
    }

    private static void printModifyMenue(String name) {
        System.out.println();
        System.out.println("Modifizierung von " + name);
        System.out.println("Befehle: " + ConsoleColors.PURPLE.print("[restart | benutzer | startCMD | stopCMD | serverDir]"));
        System.out.print("Befehl: ");
        String input = in.nextLine().toLowerCase();
        String[] args;
        if (input.contains(" ")) {
            args = input.split(" ");
        } else {
            System.out.println(ConsoleColors.RED.print("Zu wenig Argumente!"));
            printModifyMenue(name);
            return;
        }

        StringBuilder stringBuilder;
        switch (args[0]) {
            case "restart":
                try {
                    Objects.requireNonNull(Config.getServerObject(name)).setRestart(Boolean.valueOf(args[1]));
                    System.out.println(ConsoleColors.GREEN.print("Restart erfolgreich gesetzt"));
                } catch (Exception ignore) {
                    System.out.println(ConsoleColors.RED.bold("Nur \"true\" oder \"false\""));
                    printModifyMenue(name);
                }
                break;

            case "benutzer":
                Objects.requireNonNull(Config.getServerObject(name)).setBenutzer(args[1]);
                System.out.println(ConsoleColors.GREEN.print("Benutzer erfolgreich gesetzt"));
                break;

            case "startCMD":
                stringBuilder = new StringBuilder();
                for (int i = 1; i < args.length; i++) {
                    stringBuilder.append(args[i] + " ");
                }
                Objects.requireNonNull(Config.getServerObject(name)).setStartCMD(stringBuilder.toString());
                System.out.println(ConsoleColors.GREEN.print("StartCMD erfolgreich gesetzt"));
                break;

            case "stepCMD":
                stringBuilder = new StringBuilder();
                for (int i = 1; i < args.length; i++) {
                    stringBuilder.append(args[i]).append(" ");
                }
                Objects.requireNonNull(Config.getServerObject(name)).setStepCMD(args[1]);
                System.out.println(ConsoleColors.GREEN.print("StepCMD erfolgreich gesetzt"));
                break;

                //TODO ServerDir Argument einarbeiten

                default:
                    System.out.println(ConsoleColors.RED.print("Befehl nicht erkannt"));
        }
    }
}
