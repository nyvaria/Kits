package org.Dragonphase.Template.Util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import org.Dragonphase.Template.Template;
import org.bukkit.configuration.file.YamlConfiguration;

public class FileManager{
	public final Logger logger = Logger.getLogger("Minecraft");
	public static Template plugin;
	public YamlConfiguration fileManager;
	
	public FileManager(Template instance, String fileName){
		plugin = instance;
		saveFile(fileName);
		loadFile(fileName);
	}
	
	public void saveFile(String fileName){
		try {
			File file = new File(plugin.getDataFolder(), fileName);
			fileManager.save(file);
		} catch (Exception ex) {
		}
	}
	
	public void loadFile(String fileName){
		fileManager = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), fileName));
	}
	
	public Set<String> getKeys(Boolean deep){
		try{
			return fileManager.getKeys(deep);
		}catch (Exception ex){
			return null;
		}
	}
	
	public Set<String> getKeys(Boolean deep, String fileName){
		loadFile(fileName);
		return getKeys(deep);
	}
	
	public Boolean getBoolean(String path){
		try{
			return fileManager.getBoolean(path);
		}catch (Exception ex){
			return null;
		}
	}
	
	public Boolean getBoolean(String path, String fileName){
		loadFile(fileName);
		return getBoolean(path);
	}

	public int getInt(String path){
		try{
			return fileManager.getInt(path);
		}catch (Exception ex){
			return 0;
		}
	}
	
	public int getInt(String path, String fileName){
		loadFile(fileName);
		return getInt(path);
	}

	public String getString(String path){
		try{
			return fileManager.getString(path);
		}catch (Exception ex){
			return null;
		}
	}
	
	public String getString(String path, String fileName){
		loadFile(fileName);
		return getString(path);
	}
	
	public ArrayList<String> getStringList(String path){
		try{
			return (ArrayList<String>) fileManager.getStringList(path);
		}catch (Exception ex){
			return new ArrayList<String>();
		}
	}
	
	public List<String> getStringList(String path, String fileName){
		loadFile(fileName);
		return getStringList(path);
	}
	
	public void set(String path, Object object, String fileName, boolean load){
		if (load) loadFile(fileName);
		fileManager.set(path, object);
		saveFile(fileName);
	}
}
