package common;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;

public class XPSteal extends JavaPlugin implements Listener {
	private Logger log;
	
	
	// Fired when plugin is first enabled
    @Override
    public void onEnable() {
    	log = getLogger();
    	Bukkit.getPluginManager().registerEvents(this, this);
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
		int xp = (int) (getXPFromLevel(dier.getLevel()) + dier.getExp()*dier.getExpToLevel());
		killer.giveExp(xp);
		
		String msg = ChatColor.RED + killer.getDisplayName() + " bested " + dier.getDisplayName() + " and recieves " + ChatColor.DARK_RED + ChatColor.BOLD + xp + ChatColor.RESET +  ChatColor.RED + " XP!" + ChatColor.RESET;
		getServer().broadcastMessage(msg);
    }
    
    public int getXPFromLevel(int level)
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
