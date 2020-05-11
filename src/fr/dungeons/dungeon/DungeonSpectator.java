//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package fr.dungeons.dungeon;

import java.util.HashMap;
import java.util.Iterator;
import net.minecraft.server.v1_15_R1.PacketPlayOutCamera;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class DungeonSpectator {
    private static HashMap<String, String> playerInPlayer = new HashMap();

    public DungeonSpectator() {
    }

    private static void setPlayerInPlayer(Player spec, Player spectated) {
        playerInPlayer.put(spec.getName(), spectated.getName());
        spec.setGameMode(GameMode.SPECTATOR);
        if (spec.isDead()) {
            spec.spigot().respawn();
        }

        PacketPlayOutCamera packet = new PacketPlayOutCamera(((CraftPlayer)spectated).getHandle());
        ((CraftPlayer)spec).getHandle().playerConnection.sendPacket(packet);
    }

    public static void onPlayerDeath(Player p) {
        if (playerInPlayer.containsValue(p.getName())) {
            Iterator var1 = playerInPlayer.keySet().iterator();

            label41:
            while(true) {
                Player spec;
                do {
                    String spectator;
                    do {
                        if (!var1.hasNext()) {
                            break label41;
                        }

                        spectator = (String)var1.next();
                    } while(!((String)playerInPlayer.get(spectator)).equals(p.getName()));

                    spec = Bukkit.getPlayer(spectator);
                } while(spec == null);

                int index = 0;

                Player toSpec;
                do {
                    toSpec = (Player)DungeonInstance.getOnlinePlayers().get(index);
                    ++index;
                } while((toSpec.equals(p) || toSpec.equals(spec)) && index < DungeonInstance.getOnlinePlayers().size());

                setPlayerInPlayer(spec, toSpec);
            }
        }

        Player toSpec;
        do {
            toSpec = (Player)DungeonInstance.getOnlinePlayers().get(0);
        } while(toSpec.equals(p));

        setPlayerInPlayer(p, toSpec);
    }

    public static boolean isDead(Player p) {
        return p.getGameMode() == GameMode.SPECTATOR;
    }

    public static void respawnAll() {
        Iterator var0 = DungeonInstance.getOnlinePlayers().iterator();

        while(var0.hasNext()) {
            Player pla = (Player)var0.next();
            if (isDead(pla)) {
                pla.setGameMode(GameMode.SURVIVAL);
                PacketPlayOutCamera packet = new PacketPlayOutCamera(((CraftPlayer)pla).getHandle());
                ((CraftPlayer)pla).getHandle().playerConnection.sendPacket(packet);
            }
        }

    }

    public static void playerQuit(Player p) {
        if (playerInPlayer.containsValue(p.getName())) {
            Iterator var1 = playerInPlayer.keySet().iterator();

            while(true) {
                Player spec;
                do {
                    String spectator;
                    do {
                        if (!var1.hasNext()) {
                            return;
                        }

                        spectator = (String)var1.next();
                    } while(!((String)playerInPlayer.get(spectator)).equals(p.getName()));

                    spec = Bukkit.getPlayer(spectator);
                } while(spec == null);

                int index = 0;

                Player toSpec;
                do {
                    toSpec = (Player)DungeonInstance.getOnlinePlayers().get(index);
                    ++index;
                } while((toSpec.equals(p) || toSpec.equals(spec)) && index < DungeonInstance.getOnlinePlayers().size());

                setPlayerInPlayer(spec, toSpec);
            }
        } else if (playerInPlayer.containsKey(p.getName())) {
            playerInPlayer.remove(p.getName());
        }

    }
}
