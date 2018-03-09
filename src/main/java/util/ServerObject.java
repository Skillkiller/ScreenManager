package util;

import static util.Config.getServerJSONObject;

/**
 * Created by Skillkiller on 09.03.2018.
 */
public class ServerObject {

    private String name;

    public ServerObject(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public boolean getRestart() {
        return (boolean) getServerJSONObject(name).get("restart");
    }

    public String getStartCMD() {
        return (String) getServerJSONObject(name).get("startCMD");
    }

    public String getStopCMD() {
        return (String) getServerJSONObject(name).get("stopCMD");
    }

    public String getBenutzer() {
        return (String) getServerJSONObject(name).get("benutzer");
    }

    public void printStats() {
        System.out.println("Name: " + getName());
        System.out.println("Restart: " + getRestart());
        System.out.println("StartCMD: " + getStartCMD());
        System.out.println("StopCMD: " + getStopCMD());
        System.out.println("Benutzer: " + getBenutzer());
    }

}
