package de.skillkiller.servermanager.core;

import util.ServerObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

/**
 * Created by Skillkiller on 10.03.2018.
 */
public class ConsoleApi {
    public void startServer(ServerObject serverObject) {

        String command;
        if(serverObject.getRestart()) {
            command = String.format("bash -c 'while true; do %s; %s echo \"Beendet - Restart folgt\"; sleep 5; done'", serverObject.getStartCMD(),
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

    public void stopServer(ServerObject serverObject) {
        ProcessBuilder processBuilder = new ProcessBuilder("screen", "-S", serverObject.getName(), "-X", "quit");
        processBuilder.directory(serverObject.getServerDir());

        System.out.println(processBuilder.command());
        try {
            Process process = processBuilder.start();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static boolean userExist(String username) {
        //grep -c '^test:' /etc/passwd
        ProcessBuilder processBuilder = new ProcessBuilder("grep","-c", String.format("^%s:", username), "/etc/passwd");
        Process process;
        try {
            process = processBuilder.start();
            process.waitFor();
            String result = new BufferedReader(new InputStreamReader(process.getInputStream())).lines().collect(Collectors.joining("\n"));

            int number = Integer.parseInt(result.trim());
            if (number == 1) {
                return true;
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }
}
