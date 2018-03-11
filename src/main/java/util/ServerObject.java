package util;

import de.skillkiller.servermanager.core.ScreenCommand;
import org.json.simple.JSONObject;

import java.io.File;
import java.util.Objects;

import static util.Config.getServerJSONObject;
import static util.Config.setServerJSONObject;

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

    public void setRestart(boolean restart) {
        JSONObject server = getServerJSONObject(name);
        server.replace("restart", restart);
        setServerJSONObject(server);
    }

    public String getStartCMD() {
        return (String) getServerJSONObject(name).get("startCMD");
    }

    public void setStartCMD(String startCMD) {
        JSONObject server = getServerJSONObject(name);
        server.replace("startCMD", startCMD);
        setServerJSONObject(server);
    }

    public String getStepCMD() {
        return (String) getServerJSONObject(name).get("stepCMD");
    }

    public void setStepCMD(String stepCMD) {
        JSONObject server = getServerJSONObject(name);
        server.replace("stepCMD", stepCMD);
        setServerJSONObject(server);
    }

    public String getBenutzer() {
        return (String) getServerJSONObject(name).get("benutzer");
    }

    public void setBenutzer(String benutzer) {
        JSONObject server = getServerJSONObject(name);
        server.replace("benutzer", benutzer);
        setServerJSONObject(server);
    }

    public File getServerDir() {
        return new File((String) getServerJSONObject(name).get("serverDir"));
    }

    public void setServerDir(File serverDir) {
        JSONObject server = getServerJSONObject(name);
        server.replace("serverDir", serverDir.getAbsolutePath() + "/");
        setServerJSONObject(server);
    }

    public boolean isRunning() {
        File screens = new File("/var/run/screen/S-root/");
        boolean gefunden = false;
        if (!screens.isDirectory()) return false;
        for (File f : Objects.requireNonNull(screens.listFiles())) {
            String name[] = f.getName().split("\\.");
            if (name[1].equals(getName())) {
                gefunden = true;
                break;
            }
        }
        return gefunden;
    }

    public void starten() {
        if (!isRunning()) {
            new ScreenCommand().startCommand(this);
        }
    }

    public void stoppen() {
        if (isRunning()) {
            new ScreenCommand().stopCommand(this);
        }
    }

    public void printStats() {
        System.out.println("Name: " + getName());
        System.out.println("Restart: " + getRestart());
        System.out.println("StartCMD: " + getStartCMD());
        System.out.println("StepCMD: " + getStepCMD());
        System.out.println("Benutzer: " + getBenutzer());
        System.out.println("Server Ordner: " + getServerDir().getAbsolutePath() + "[" + getServerDir().isDirectory() + "]");
    }

}
