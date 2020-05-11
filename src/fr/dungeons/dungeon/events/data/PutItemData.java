//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package fr.dungeons.dungeon.events.data;

import fr.dungeons.dungeon.actions.DungeonAction;
import fr.dungeons.dungeon.events.DungeonEventType;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class PutItemData extends EventData {
    private Vector chestLocation;
    private ItemStack item;

    public PutItemData(ItemStack item, Vector chestLocation, List<DungeonAction> actionsTodo, int id) {
        super(DungeonEventType.PUT_ITEM, actionsTodo, id);
        this.item = item;
        this.chestLocation = chestLocation;
    }

    public Vector getChestLocation() {
        return this.chestLocation;
    }

    public void setChestLocation(Vector chestLocation) {
        this.chestLocation = chestLocation;
    }

    public ItemStack getItem() {
        return this.item;
    }

    public void setItem(ItemStack item) {
        this.item = item;
    }

    public String serialize() {
        YamlConfiguration yml = new YamlConfiguration();
        yml.set("Reference_ID", this.getReferenceID());
        yml.set("eventType", this.getType().name());
        List<Integer> actionsIDs = new ArrayList();
        Iterator var3 = this.getActionsTodo().iterator();

        while(var3.hasNext()) {
            DungeonAction da = (DungeonAction)var3.next();
            actionsIDs.add(da.getReferenceID());
        }

        yml.set("actionsTodo", actionsIDs);
        yml.set("item", this.getItem());
        yml.set("chestLocation", this.getChestLocation());
        return yml.saveToString();
    }

    public static PutItemData unserialize(String serialized) {
        YamlConfiguration yml = new YamlConfiguration();

        try {
            yml.loadFromString(serialized);
            List<DungeonAction> actionsTodo = new ArrayList();
            Iterator var3 = yml.getIntegerList("actionsTodo").iterator();

            while(var3.hasNext()) {
                int id = (Integer)var3.next();
                actionsTodo.add(DungeonAction.getByID(id));
            }

            return new PutItemData(yml.getItemStack("item"), yml.getVector("chestLocation"), actionsTodo, yml.getInt("Reference_ID"));
        } catch (InvalidConfigurationException var5) {
            var5.printStackTrace();
            return null;
        }
    }
}
