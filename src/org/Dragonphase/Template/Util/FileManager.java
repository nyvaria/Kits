package org.Dragonphase.Template.Util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.Dragonphase.Template.Template;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

public class FileManager{
    public final Logger logger = Logger.getLogger("Minecraft");
    private Template plugin;
    private YamlConfiguration fileManager;
    private String fileName;
    
    public FileManager(Template instance, String filename){
        plugin = instance;
        fileName = filename;
        saveFile();
        loadFile();
    }
    
    public void saveFile(){
        try {
            File file = new File(plugin.getDataFolder(), fileName);
            try{
                if (!file.exists()){
                    plugin.saveResource(fileName, false);
                }else{
                    fileManager.save(file);
                }
            }catch (Exception e){
                fileManager.save(file);
            }
        } catch (Exception ex) {}
    }
    
    public void loadFile(){
        fileManager = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), fileName));
    }
    
    public Set<String> getKeys(boolean deep){
        try{
            return fileManager.getKeys(deep);
        }catch (Exception ex){
            return null;
        }
    }
    
    public Set<String> getKeys(boolean deep, boolean load){
        if (load) loadFile();
        return getKeys(deep);
    }
    
    public boolean getBoolean(String path){
        try{
            return fileManager.getBoolean(path);
        }catch (Exception ex){
            return false;
        }
    }
    
    public boolean getBoolean(String path, boolean load){
        if (load) loadFile();
        return getBoolean(path);
    }

    public int getInt(String path){
        try{
            return fileManager.getInt(path);
        }catch (Exception ex){
            return 0;
        }
    }
    
    public int getInt(String path, boolean load){
        if (load) loadFile();
        return getInt(path);
    }
    
    public double getDouble(String path){
        try{
            return fileManager.getDouble(path);
        }catch (Exception ex){
            return 0.0;
        }
    }
    
    public double getDouble(String path, boolean load){
        if (load) loadFile();
        return getDouble(path);
    }

    public String getString(String path){
        try{
            return fileManager.getString(path);
        }catch (Exception ex){
            return "";
        }
    }
    
    public String getString(String path, boolean load){
        if (load) loadFile();
        return getString(path);
    }
    
    public ArrayList<String> getStringList(String path){
        try{
            return (ArrayList<String>) fileManager.getStringList(path);
        }catch (Exception ex){
            return new ArrayList<String>();
        }
    }
    
    public List<String> getStringList(String path, boolean load){
        if (load) loadFile();
        return getStringList(path);
    }
    
    public ConfigurationSection getConfigurationSection(String path){
        try{
            return fileManager.getConfigurationSection(path);
        }catch (Exception ex){
            return null;
        }
    }
    
    public ConfigurationSection getConfigurationSection(String path, boolean load){
        if (load) loadFile();
        return getConfigurationSection(path);
    }
    
    public void createSection(String path, Map<String, Object> map) {
        try {
            fileManager.createSection(path, map);
        } catch (Exception ex) {}
    }
    
    public void createSection(String path, Map<String, Object> map, boolean load) {
        if (load) loadFile();
        createSection(path, map);
    }
    
    public boolean isConfigurationSection(String path){
        try{
            return fileManager.isConfigurationSection(path);
        }catch (Exception ex){
            return false;
        }
    }
    
    public boolean isConfigurationSection(String path, boolean load){
        if (load) loadFile();
        return isConfigurationSection(path);
    }
    
    public void set(String path, Object object, boolean load){
        if (load) loadFile();
        fileManager.set(path, object);
        saveFile();
    }
    
    public List<Map<?,?>> getMapList(String path, boolean load){
        if (load) loadFile();
        return fileManager.getMapList(path);
    }
}
