//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package fr.dungeons.dungeon.actions.data;

import fr.dungeons.dungeon.actions.DungeonActionType;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class SpawnItemData extends ActionData {
    private Vector locToSpawn;
    private ItemStack item;

    public Vector getLocToSpawn() {
        return this.locToSpawn;
    }

    public void setLocToSpawn(Vector locToSpawn) {
        this.locToSpawn = locToSpawn;
    }

    public ItemStack getItem() {
        return this.item;
    }

    public void setItem(ItemStack item) {
        this.item = item;
    }

    public SpawnItemData(ItemStack item, Vector locToSpawn, int id) {
        super(DungeonActionType.SPAWN_ITEM, id);
        this.item = item;
        this.locToSpawn = locToSpawn;
    }

    public String serialize() {
        YamlConfiguration yml = new YamlConfiguration();
        yml.set("Reference_ID", this.getReferenceID());
        yml.set("actionType", this.getType().name());
        yml.set("locToSpawn", this.getLocToSpawn());
        yml.set("item", this.getItem());
        return yml.saveToString();
    }

    public static SpawnItemData unserialize(String serialized) {
        YamlConfiguration yml = new YamlConfiguration();

        try {
            yml.loadFromString(serialized);
            return new SpawnItemData(yml.getItemStack("item"), yml.getVector("locToSpawn"), yml.getInt("Reference_ID"));
        } catch (InvalidConfigurationException var3) {
            var3.printStackTrace();
            return null;
        }
    }
}
