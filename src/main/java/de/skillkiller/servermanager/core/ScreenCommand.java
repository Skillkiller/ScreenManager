package de.skillkiller.servermanager.core;

import util.ServerObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

/**
 * Created by Skillkiller on 10.03.2018.
 */
public class ScreenCommand {
    public void startCommand(ServerObject serverObject) {

        String command;
        if(serverObject.getRestart()) {
            command = String.format("bash -c 'while true; do %s; %s echo \\\"Beendet - Restart folgt\\\"; sleep 5; done'", serverObject.getStartCMD(),
                    serverObject.getStepCMD().length() > 0 ? serverObject.getStepCMD() + "; " : "");
        } else {
            command = String.format("bash -c '%s; %s'", serverObject.getStartCMD(), serverObject.getStepCMD());
        }

        ProcessBuilder processBuilder = new ProcessBuilder("screen", "-dmS", serverObject.getName(), "su", "-c", command, serverObject.getBenutzer() );
        processBuilder.directory(serverObject.getServerDir());

        System.out.println("Starte: " + processBuilder.command());
        try {
            processBuilder.start().waitFor();
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }

    public void stopCommand(ServerObject serverObject) {
        ProcessBuilder processBuilder = new ProcessBuilder("screen", "-S", serverObject.getName(), "-X", "quit");
        processBuilder.directory(serverObject.getServerDir());

        System.out.println(processBuilder.command());
        try {
            Process process = processBuilder.start();
            new BufferedReader(new InputStreamReader(process.getInputStream())).lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
