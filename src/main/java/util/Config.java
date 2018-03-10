package util;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Skillkiller on 09.03.2018.
 */
public class Config {

    private static File configFile = new File("Config.json");
    private static File workingDir;
    private static JSONParser parser = new JSONParser();
    private static JSONObject configJsonObject;

    public Config() {
        if (!configFile.exists()) {
            try {
                configFile.createNewFile();
                configJsonObject = new JSONObject();

                saveJSON();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                configJsonObject = (JSONObject) parser.parse(new FileReader(configFile));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if(!configJsonObject.containsKey("servers")) {
            configJsonObject.put("servers", new JSONArray());
            saveJSON();
        }

        if (!configJsonObject.containsKey("workingDir")) {
            configJsonObject.put("workingDir", "server/");
            saveJSON();
        }
        workingDir = new File((String) configJsonObject.get("workingDir"));
        if (!workingDir.exists()) {
            workingDir.mkdirs();
        }
        if (!workingDir.isDirectory()) {
            configJsonObject.replace("workingDir", "server/");
            workingDir = new File("server/");
            workingDir.mkdirs();
        }

    }

    private static JSONArray getServersArray() {
        return (JSONArray) configJsonObject.getOrDefault("servers", new JSONArray());
    }

    public static ArrayList<ServerObject> getServers() {
        ArrayList<ServerObject> servers = new ArrayList<>();
        JSONArray jsonArray = getServersArray();

        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = (JSONObject) jsonArray.get(i);
            servers.add(getServerObject((String) jsonObject.get("name")));
        }
        return servers;
    }

    public static boolean serverExist(String name) {
        boolean exist = false;
        JSONArray jsonArray = getServersArray();

        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = (JSONObject) jsonArray.get(i);
            if(jsonObject.containsKey("name")) {
                if (jsonObject.get("name").toString().equals(name)) {
                    exist = true;
                    break;
                }
            }
        }

        return exist;
    }

    public static File getWorkingDir() {
        return workingDir;
    }

    public static ServerObject getServerObject(String name) {
        if (serverExist(name)) {
            return new ServerObject(name);
        } else {
            return null;
        }
    }

    protected static JSONObject getServerJSONObject(String name) {
        if (serverExist(name)) {
            JSONArray jsonArray = getServersArray();
            JSONObject server = null;

            while (server == null) {
                for (int i = 0; i < jsonArray.size(); i++) {
                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                    if(jsonObject.containsKey("name")) {
                        if (jsonObject.get("name").toString().equals(name)) {
                            server = jsonObject;
                        }
                    }
                }
            }
            return server;
        }
        return null;
    }

    public static void createServer(String name, boolean restart, String startCMD, String stopCMD, String benutzer) {
        JSONObject server = new JSONObject();
        server.put("name", name);
        server.put("restart", restart);
        server.put("startCMD", startCMD);
        server.put("stopCMD", stopCMD);
        server.put("benutzer", benutzer);
        server.put("serverDir", workingDir.getAbsolutePath() + "/" + name + "/");
        new File(workingDir.getAbsolutePath() + "/" + name + "/").mkdirs();

        addServer(server);
    }

    private static void addServer(JSONObject server) {
        JSONArray jsonArray = getServersArray();
        jsonArray.add(server);
        configJsonObject.replace("servers", jsonArray);
        saveJSON();
    }

    public static void removeServer(String name) {
        if (serverExist(name)) {
            JSONArray jsonArray = getServersArray();
            jsonArray.remove(getServerJSONObject(name));
            configJsonObject.replace("servers", jsonArray);
            saveJSON();
        }
    }

    private static void saveJSON() {
        try (FileWriter file = new FileWriter(configFile)) {

            file.write(configJsonObject.toJSONString());
            file.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
