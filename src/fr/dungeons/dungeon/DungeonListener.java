//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package fr.dungeons.dungeon;

import fr.dungeons.Dungeons;
import fr.tylo.socket.Client;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class DungeonListener implements Listener {
    private static HashMap<String, Integer> lastMessage = new HashMap();
    private static boolean starting = false;

    public DungeonListener() {
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.setJoinMessage(Messages.DUNGEON_PREFIX + Messages.JOIN.replace("{playername}", event.getPlayer().getName()));
        if (!starting) {
            (new BukkitRunnable() {
                public void run() {
                    Client.setUnAvailable();
                }
            }).runTaskLater(Dungeons.getPlugin(), 200L);
            starting = true;
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (!event.getFrom().getBlock().equals(event.getTo().getBlock())) {
            Player p = event.getPlayer();
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        boolean allDead = true;
        Iterator var3 = DungeonInstance.getOnlinePlayers().iterator();

        while(var3.hasNext()) {
            Player pla = (Player)var3.next();
            if (!pla.equals(event.getEntity()) && !DungeonSpectator.isDead(pla)) {
                allDead = false;
                break;
            }
        }

        if (allDead) {
            DungeonInstance.loose(new String[]{"Tout les joueurs sont morts"});
        } else {
            DungeonInstance.setRemainingHealth(DungeonInstance.getRemainingHealth() - 1);
            if (DungeonInstance.getRemainingHealth() < 0) {
                DungeonInstance.loose(new String[]{"Plus de vie"});
            } else {
                DungeonSpectator.onPlayerDeath(event.getEntity());
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        event.setQuitMessage(Messages.DUNGEON_PREFIX + Messages.QUIT.replace("{playername}", event.getPlayer().getName()));
        if (DungeonInstance.getOnlinePlayers().size() == 0) {
            DungeonInstance.loose(new String[]{"Tout les joueurs se sont déconnecté"});
        } else {
            Bukkit.getPluginManager().callEvent(new PlayerDeathEvent(event.getPlayer(), new ArrayList(), 0, "Déconnecté"));
            DungeonSpectator.playerQuit(event.getPlayer());
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (!event.isCancelled()) {
            if (!DungeonInstance.getDungeon().getPlacableBlocks().contains(event.getBlockPlaced().getType())) {
                event.setCancelled(true);
                Player p = event.getPlayer();
                p.playSound(p.getEyeLocation(), Sound.ENTITY_VILLAGER_NO, 1.0F, 1.0F);
                p.sendMessage(Messages.DUNGEON_PREFIX + Messages.PAS_PLACER);
                p.updateInventory();
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (!event.isCancelled()) {
            if (!DungeonInstance.getDungeon().getBreakableBlocks().contains(event.getBlock().getType())) {
                event.setCancelled(true);
                Player p = event.getPlayer();
                p.playSound(p.getEyeLocation(), Sound.ENTITY_VILLAGER_NO, 1.0F, 1.0F);
                p.sendMessage(Messages.DUNGEON_PREFIX + Messages.PAS_CASSER);
            }
        }
    }
}
