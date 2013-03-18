package org.Dragonphase.Battlegear.Util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import org.Dragonphase.Battlegear.Battlegear;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

public class FileManager{
	public final Logger logger = Logger.getLogger("Minecraft");
	private File folder;
	private YamlConfiguration fileManager;
	private String fileName;
	
	public FileManager(File instance, String filename){
		folder = instance;
		fileName = filename;
		
		saveFile();
		loadFile();
	}
	
	public void saveFile(){
		try {
			File file = new File(folder, fileName);
			try{
				if (!file.exists()){
					Battlegear.plugin.saveResource(fileName, false);
				}else{
					fileManager.save(file);
				}
			}catch (Exception e){
				fileManager.save(file);
			}
		} catch (Exception ex) {
		}
	}
	
	public void loadFile(){
		fileManager = YamlConfiguration.loadConfiguration(new File(folder, fileName));
	}
	
	public Set<String> getKeys(Boolean deep){
		try{
			return fileManager.getKeys(deep);
		}catch (Exception ex){
			return null;
		}
	}
	
	public Set<String> getKeys(boolean deep, boolean load){
		loadFile();
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
		loadFile();
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
		loadFile();
		return getInt(path);
	}

	public String getString(String path){
		try{
			return fileManager.getString(path);
		}catch (Exception ex){
			return "";
		}
	}
	
	public String getString(String path, boolean load){
		loadFile();
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
		loadFile();
		return getStringList(path);
	}
	
	public ConfigurationSection getConfigSection(String path){
		try{
			return fileManager.getConfigurationSection(path);
		}catch (Exception ex){
			return null;
		}
	}
	
	public ConfigurationSection getConfigSection(String path, boolean load){
		loadFile();
		return getConfigSection(path);
	}
	
	public boolean isConfigurationSection(String path){
		try{
			return fileManager.isConfigurationSection(path);
		}catch (Exception ex){
			return false;
		}
	}
	
	public boolean isConfigurationSection(String path, boolean load){
		loadFile();
		return isConfigurationSection(path);
	}
	
	public void set(String path, Object object, boolean load){
		if (load) loadFile();
		fileManager.set(path, object);
		saveFile();
	}
}
