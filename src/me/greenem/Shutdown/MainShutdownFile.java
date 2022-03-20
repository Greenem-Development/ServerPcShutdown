package me.greenem.Shutdown;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import org.bukkit.event.Listener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;


public class MainShutdownFile extends JavaPlugin implements Listener {
	
	Logger log = getLogger();
	public Properties props = new Properties();
	
	@Override
	public void onDisable() {
		log.info(ChatColor.RED + "Shutdown plugin has been disabled.");
	}

	@Override
	public void onEnable() {
		log.info(ChatColor.GREEN + "Shutdown plugin has been enabled!");
		getServer().getPluginManager().registerEvents(this, this);
	}
	
	//for(Map.Entry<String, PlayerData>entity : players.entrySet()) {
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		Player p = getServer().getPlayer(sender.getName());
		
		if(cmd.getName().equalsIgnoreCase("shutdown")){
			if(p.isOp()) {
				Shutdown(p);
			}
			else {
				p.sendMessage(ChatColor.RED + "You are not an operator.");
			}
		}
		return false;
	}
	
	public void Shutdown(Player p) {
		FileReader reader = null;
		props.clear();
		File f = new File(getServer().getWorldContainer(), "plugins\\shutdown.properties");
		reader = null;
		props.clear();
		boolean Allowed = false;
		if (f.exists()) {
			try {
				reader = new FileReader(f);
				try {
					props.load(reader);
					reader.close();
					String s = props.getProperty("shutdownAllowed");
					if(s.equalsIgnoreCase("1") || s.equalsIgnoreCase("true") || s.equalsIgnoreCase("yes")) {
						Allowed = true;
					}
					props.setProperty("shutdownAllowed", "0");
					FileOutputStream fos = new FileOutputStream(f);
					props.store(fos, "");
					fos.close();
				} catch (IOException e1) {
					e1.printStackTrace();
					p.sendMessage(ChatColor.DARK_RED + "IOException has been catched.");
				}
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
				p.sendMessage(ChatColor.DARK_RED + "FileNotFoundException has been catched.");
			}
		}
		else {
			try {
				FileOutputStream fos = new FileOutputStream(f);
				props.store(fos, "");
				fos.close();
			}
			catch(IOException e) {
				e.printStackTrace();
				p.sendMessage(ChatColor.DARK_RED + "IOException has been catched.");
			}
		}
		if(Allowed == true) {
			Bukkit.getWorlds().forEach(w -> {
				w.save();
			});
			try {
				Runtime.getRuntime().exec("G:\\for everything\\Programming\\Golang\\NMS Shutdown\\runShutdown.bat", null, new File("G:\\for everything\\Programming\\Golang\\NMS Shutdown\\"));
			} catch (IOException e) {
				e.printStackTrace();
				p.sendMessage(ChatColor.DARK_RED + "IOException has been catched.");
			}
			Bukkit.shutdown();
		}
		else {
			if(p!=null) {
				p.sendMessage(ChatColor.RED + "You are not given permission to do this. Ask the owner about this.");
			}
		}
	}
}