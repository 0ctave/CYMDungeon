//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package fr.dungeons;

import fr.dungeons.custommob.CustomMob;
import fr.dungeons.dungeon.Dungeon;
import fr.dungeons.dungeon.DungeonInstance;
import fr.dungeons.dungeon.DungeonListener;
import fr.dungeons.dungeon.Messages;
import fr.dungeons.dungeon.actions.DungeonAction;
import fr.dungeons.dungeon.events.DungeonEvent;
import fr.dungeons.dungeon.events.DungeonEventManager;
import fr.dungeons.group.GroupManager;
import fr.tylo.socket.Client;
import java.io.File;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Dungeons extends JavaPlugin {
    public static int currentTick = 0;
    private static final String prefix;

    public Dungeons() {
    }

    public void onEnable() {
        PluginManager manager = Bukkit.getPluginManager();
        File folder = this.getDataFolder();
        if (!folder.exists()) {
            try {
                folder.mkdir();
            } catch (Exception var5) {
                Logger.error("CRITICAL - Unable to create plugin folder " + folder.getName() + " : " + var5.getMessage());
                Logger.error(new String[]{"It needs to be fixed because the plugin won't be able to work without"});
                manager.disablePlugin(getPlugin());
            }
        }

        BukkitRunnable ticks = new BukkitRunnable() {
            public void run() {
                ++Dungeons.currentTick;
            }
        };
        ticks.runTaskTimer(getPlugin(), 0L, 1L);
        CustomMob.enableCustomMob();
        DungeonAction.loadAllFromFiles();
        DungeonEvent.loadAllFromFiles();
        Messages.init();
        Dungeon.loadShematics();
        Dungeon d = Dungeon.loadFromFile(new File(this.getDataFolder().getAbsolutePath() + File.separatorChar + "Dungeon.yml"));
        if (d != null) {
            DungeonInstance.initInstance(d);
            (new BukkitRunnable() {
                public void run() {
                    Client.setAvailable(DungeonInstance.getDungeon().getName());
                }
            }).runTaskLater(getPlugin(), 40L);
        } else {
            System.out.println("No Dungeon.yml found");
            setOffMode();
        }

        Bukkit.getPluginManager().registerEvents(new DungeonListener(), getPlugin());
        Bukkit.getPluginManager().registerEvents(new DungeonEventManager(), getPlugin());
        Client.setConnected();
    }

    public void onDisable() {
        Logger.info("Arret de l'instance");
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.equals(Bukkit.getPluginCommand("test"))) {
        }

        if (command.equals(Bukkit.getPluginCommand("groupe"))) {
            GroupManager.onCommand(sender, args);
        }

        if (command.getName().equalsIgnoreCase("rd")) {
            Bukkit.getPluginManager().disablePlugin(getPlugin());
            Bukkit.getPluginManager().enablePlugin(getPlugin());
        }

        return true;
    }

    public static Plugin getPlugin() {
        return getPlugin(Dungeons.class);
    }

    public static String getPrefix() {
        return prefix;
    }

    public static void setOffMode() {
        HandlerList.unregisterAll(getPlugin());
        Logger.info(new String[]{"Plugin turned into OFF-mode"});
    }

    static {
        prefix = ChatColor.GRAY + "[" + ChatColor.BLUE + "Dungeons" + ChatColor.GRAY + "] " + ChatColor.RESET;
    }
}
