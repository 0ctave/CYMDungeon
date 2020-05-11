//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package fr.dungeons.dungeon.events;

import fr.dungeons.dungeon.DungeonInstance;
import fr.dungeons.dungeon.actions.DungeonAction;
import fr.dungeons.dungeon.events.data.AreaDetectionData;
import fr.dungeons.dungeon.events.data.BlockPlaceData;
import fr.dungeons.dungeon.events.data.InteractData;
import fr.dungeons.dungeon.events.data.MobWaveEndedData;
import fr.dungeons.dungeon.events.data.PutItemData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import org.bukkit.block.Chest;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.util.Vector;

public class DungeonEventManager implements Listener {
    private static HashMap<List<LivingEntity>, DungeonAction> entitiesFromAction = new HashMap();
    private static List<DungeonAction> endedActions = new ArrayList();

    public DungeonEventManager() {
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        Entity e = event.getEntity();
        if (e instanceof LivingEntity) {
            Iterator entities = entitiesFromAction.entrySet().iterator();

            while(entities.hasNext()) {
                Entry<List<LivingEntity>, DungeonAction> ent = (Entry)entities.next();
                List<LivingEntity> le = (List)ent.getKey();
                if (le.remove(e) && le.size() == 0) {
                    endedActions.add(ent.getValue());
                    entities.remove();
                }
            }

            Iterator var6 = DungeonInstance.getEvents().iterator();

            while(var6.hasNext()) {
                DungeonEvent de = (DungeonEvent)var6.next();
                if (de.getData().getType() == DungeonEventType.MOB_WAVE_ENDED && endedActions.containsAll(((MobWaveEndedData)de.getData()).getMobFromActions())) {
                    endedActions.removeAll(((MobWaveEndedData)de.getData()).getMobFromActions());
                    de.event();
                }
            }

        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (!event.getFrom().getBlock().equals(event.getTo().getBlock())) {
            Iterator var2 = DungeonInstance.getEvents().iterator();

            while(true) {
                label43:
                while(true) {
                    DungeonEvent de;
                    do {
                        if (!var2.hasNext()) {
                            return;
                        }

                        de = (DungeonEvent)var2.next();
                    } while(de.getData().getType() != DungeonEventType.AREA_DETECTION);

                    double radius = ((AreaDetectionData)de.getData()).getActivationRadius() * ((AreaDetectionData)de.getData()).getActivationRadius();
                    Iterator var6 = ((AreaDetectionData)de.getData()).getActivationLocations().iterator();

                    while(var6.hasNext()) {
                        Vector actPos = (Vector)var6.next();
                        boolean near = false;
                        Iterator var9 = DungeonInstance.getOnlinePlayers().iterator();

                        while(var9.hasNext()) {
                            Player pla = (Player)var9.next();
                            if (pla.getLocation().toVector().distanceSquared(actPos) <= radius) {
                                near = true;
                            }
                        }

                        if (!near) {
                            continue label43;
                        }
                    }

                    de.event();
                }
            }
        }
    }

    public static void setEntitiesFrom(List<LivingEntity> le, DungeonAction action) {
        entitiesFromAction.put(le, action);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!event.isCancelled()) {
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                Iterator var2 = DungeonInstance.getEvents().iterator();

                while(var2.hasNext()) {
                    DungeonEvent de = (DungeonEvent)var2.next();
                    if (de.getData().getType() == DungeonEventType.INTERACT && ((InteractData)de.getData()).getBlockLocation().equals(event.getClickedBlock().getLocation().toVector())) {
                        de.event();
                    }
                }

            }
        }
    }

    @EventHandler
    public void onPlayerPlaceBlock(BlockPlaceEvent event) {
        if (!event.isCancelled()) {
            Iterator var2 = DungeonInstance.getEvents().iterator();

            while(var2.hasNext()) {
                DungeonEvent de = (DungeonEvent)var2.next();
                if (de.getData().getType() == DungeonEventType.PLACE_BLOCK && event.getBlockPlaced().getLocation().toVector().equals(((BlockPlaceData)de.getData()).getBlockLocation()) && event.getBlockPlaced().getType() == ((BlockPlaceData)de.getData()).getBlockType()) {
                    de.event();
                }
            }

        }
    }

    @EventHandler
    public void onPlayerCloseInventory(InventoryCloseEvent event) {
        Inventory inv = event.getInventory();
        if (inv.getHolder() instanceof Chest) {
            Chest c = (Chest)inv.getHolder();
            Iterator var4 = DungeonInstance.getEvents().iterator();

            while(var4.hasNext()) {
                DungeonEvent de = (DungeonEvent)var4.next();
                if (de.getData().getType() == DungeonEventType.PUT_ITEM && ((PutItemData)de.getData()).getChestLocation().equals(c.getLocation().toVector()) && c.getBlockInventory().contains(((PutItemData)de.getData()).getItem())) {
                    de.event();
                }
            }

        }
    }
}
