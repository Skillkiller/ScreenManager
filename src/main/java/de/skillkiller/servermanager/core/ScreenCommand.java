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
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.directory(serverObject.getServerDir());
        String aussen = String.format("screen -dmS %s %s",
                serverObject.getName(),
                serverObject.getRestart() ? String.format("su -c \"bash -c 'while true; do %s; echo \\\"Beendet - Restart folgt\\\"; sleep 5; done'\" %s", serverObject.getStartCMD(),
                        serverObject.getBenutzer()) :
                String.format("su -c '%s' %s", serverObject.getStartCMD(), serverObject.getBenutzer()));
        processBuilder.command(aussen);

        System.out.println("Starte: " + aussen);
        try {
            processBuilder.start().waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopCommand(ServerObject serverObject) {
        //TODO Überarbeiten!
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.directory(serverObject.getServerDir());
        processBuilder.command(String.format("screen -S %s -X %s",
                serverObject.getName(), serverObject.getStopCMD()));
        processBuilder.command(String.format("screen -S %s -X quit", serverObject.getName()));

        System.out.println(processBuilder.command());
        try {
            Process process = processBuilder.start();
            new BufferedReader(new InputStreamReader(process.getInputStream())).lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
