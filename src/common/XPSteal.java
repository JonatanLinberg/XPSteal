package common;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;

public class XPSteal extends JavaPlugin implements Listener, CommandExecutor {
	private Logger log;
	final private String chatPrefix = ChatColor.RED + "[" + ChatColor.DARK_RED + ChatColor.BOLD + ChatColor.UNDERLINE + "XPSteal" + ChatColor.RESET + ChatColor.RED + "]" + ChatColor.DARK_RED + " - ";  
	
	// Fired when plugin is first enabled
    @Override
    public void onEnable() {
    	log = getLogger();
    	Bukkit.getPluginManager().registerEvents(this, this);
    	this.getCommand("XPSteal").setExecutor(this);
    }
    
    // Fired when plugin is disabled
    @Override
    public void onDisable() {
    	
    }
    
    @EventHandler
	public void onDeath(PlayerDeathEvent e) {
    	if (!(e.getEntity() instanceof Player)) {
			return;
		}
		Player dier = (Player)e.getEntity();
		Player killer = dier.getKiller();
		if (killer == null) // no killer
			return;
		
		e.setDroppedExp(0);
		int xp = calcExp(dier);
		killer.giveExp(xp);
		
		String msg = ChatColor.RED + killer.getDisplayName() + " bested " + dier.getDisplayName() + " and recieves " + ChatColor.DARK_RED + ChatColor.BOLD + xp + ChatColor.RESET +  ChatColor.RED + " XP!" + ChatColor.RESET;
		getServer().broadcastMessage(chatPrefix + msg);
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    	if (!(sender instanceof Player))
    		return true;
    	
    	if (args.length == 0 || args[0].equals("xp") || args[0].equals("balance")) {
	    	String msg = ChatColor.RED + "You currently have " + calcExp((Player)sender) + " XP." + ChatColor.RESET;
	    	sender.sendMessage(chatPrefix + msg);
	        
	    	return true;
    	}
    	
    	return false;
    }
    
    private int calcExp(Player p) {
		return (int) (getXPFromLevel(p.getLevel()) + p.getExp()*p.getExpToLevel());
    }
    
    private int getXPFromLevel(int level)
    {
    	if(level >= 1 && level <= 16) {
    		return (int)(Math.pow(level, 2) + 6 * level);
    	}
    	else if(level >= 17 && level <= 31) {
    		return (int)( 2.5 * Math.pow(level, 2) - 40.5 * level + 360);
    	}
    	else if(level >= 32) {
    		return (int)(4.5 * Math.pow(level, 2) - 162.5 * level + 2220);
    	}
    	else {
    		return 0;
    	}
    }
}
