package com.aqupd.namebros;

import com.google.gson.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Config {

  private Config() {}
  public static final Config INSTANCE = new Config();
  private final File confFile = new File("./config/AqMods/NameBros.json");
  Gson gson = new GsonBuilder().setPrettyPrinting().create();

  private boolean ENABLE = true;
  private boolean DEBUG = false;
  private List<String> BLACKLIST = new ArrayList<>() {{ add("minecraft:warden"); add("minecraft:wither"); }};

  public boolean isDebug() { return DEBUG; }
  public boolean enabled() { return ENABLE; }
  public List<String> getBlacklist() { return BLACKLIST; }

  public boolean addBlacklist(String blacklistEntity) {
    if(!BLACKLIST.contains(blacklistEntity.toLowerCase())) {
      BLACKLIST.add(blacklistEntity.toLowerCase());
      save();
      return true;
    }
    return false;
  }
  public boolean removeBlackList(String blacklistEntity) {
    if(BLACKLIST.contains(blacklistEntity.toLowerCase())) {
      BLACKLIST.remove(blacklistEntity.toLowerCase());
      save();
      return true;
    }
    return false;
  }

  public void toggleEnable() {
    ENABLE = !ENABLE;
    save();
  }
  public void toggleDebug() {
    DEBUG = !DEBUG;
    save();
  }

  public void load() {
    if (!confFile.exists() || confFile.length() == 0) save();
    try {
      JsonObject jo = gson.fromJson(new FileReader(confFile), JsonObject.class);
      JsonElement jE;

      if ((jE = jo.get("enable")) != null) ENABLE = jE.getAsBoolean();
      if ((jE = jo.get("debug")) != null) DEBUG = jE.getAsBoolean();
      if ((jE = jo.get("blacklist")) != null) {
        List<String> newBL = new ArrayList<>();
        jE.getAsJsonArray().forEach(bl -> newBL.add(bl.getAsString().toLowerCase()));
        BLACKLIST = newBL;
      }
      save();
    } catch (FileNotFoundException ex) {
      Main.LOGGER.trace("Couldn't load configuration file", ex);
    }
  }

  public void save() {
    try {
      if (!confFile.exists()) { confFile.getParentFile().mkdirs(); confFile.createNewFile(); }
      if (confFile.exists()) {
        JsonObject jo = new JsonObject();
        jo.add("enable", new JsonPrimitive(ENABLE));
        jo.add("debug", new JsonPrimitive(DEBUG));

        JsonArray blacklist = new JsonArray();
        for(String bl: BLACKLIST) { blacklist.add(bl.toLowerCase()); }
        jo.add("blacklist", blacklist);

        PrintWriter printwriter = new PrintWriter(new FileWriter(confFile));
        printwriter.print(gson.toJson(jo));
        printwriter.close();
      }
    } catch (IOException ex) {
      Main.LOGGER.trace("Couldn't save configuration file", ex);
    }
  }
}
