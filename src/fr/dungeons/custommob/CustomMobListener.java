//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package fr.dungeons.custommob;

import fr.dungeons.utils.NBTEditor;
import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

public class CustomMobListener implements Listener {
    public static CustomMobListener instance;
    public static HashMap<UUID, HashMap<ItemStack, Float>> listeningEntities = new HashMap();

    public CustomMobListener() {
        instance = this;
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        Entity e = event.getEntity();
        if (listeningEntities.containsKey(e.getUniqueId())) {
            HashMap<ItemStack, Float> toLoot = (HashMap)listeningEntities.get(e.getUniqueId());
            Iterator var4 = toLoot.keySet().iterator();

            while(var4.hasNext()) {
                ItemStack is = (ItemStack)var4.next();
                if (Math.random() <= (double)(Float)toLoot.get(is)) {
                    e.getWorld().dropItem(e.getLocation(), NBTEditor.removeNBT(is, "drop_chance"));
                }
            }

            listeningEntities.remove(e.getUniqueId());
        }
    }
}
