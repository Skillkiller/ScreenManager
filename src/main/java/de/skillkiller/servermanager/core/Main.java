package de.skillkiller.servermanager.core;

import util.Config;
import util.ServerObject;

import java.util.UUID;

/**
 * Created by Skillkiller on 09.03.2018.
 */
public class Main {


    public static void main(String[] args) {
        new Config();
        for (int i = 0; i < 100 - Config.getServers().size(); i++) {
            Config.createServer(UUID.randomUUID().toString(), (Math.random() < 0.5), "", "", "root");
        }

        for (ServerObject serverObject : Config.getServers()) {
            serverObject.printStats();
        }
    }
}
