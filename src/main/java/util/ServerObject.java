package util;

import java.io.File;

import static util.Config.getServerJSONObject;

/**
 * Created by Skillkiller on 09.03.2018.
 */
public class ServerObject {

    private String name;

    ServerObject(String name) {
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

    public File getServerDir() {
        return new File((String) getServerJSONObject(name).get("serverDir"));
    }

    public boolean isRunning() {
        //TODO Richtige Prüfung!
        return false;
    }

    public void printStats() {
        System.out.println("Name: " + getName());
        System.out.println("Restart: " + getRestart());
        System.out.println("StartCMD: " + getStartCMD());
        System.out.println("StopCMD: " + getStopCMD());
        System.out.println("Benutzer: " + getBenutzer());
        System.out.println("Server Ordner: " + getServerDir().getAbsolutePath());
    }

}
