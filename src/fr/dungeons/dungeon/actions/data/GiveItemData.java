//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package fr.dungeons.dungeon.actions.data;

import fr.dungeons.dungeon.actions.DungeonActionType;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

public class GiveItemData extends ActionData {
    private ItemStack item;

    public GiveItemData(ItemStack item, int id) {
        super(DungeonActionType.GIVE_ITEM, id);
        this.item = item;
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
        yml.set("actionType", this.getType().name());
        yml.set("item", this.getItem());
        return yml.saveToString();
    }

    public static GiveItemData unserialize(String serialized) {
        YamlConfiguration yml = new YamlConfiguration();

        try {
            yml.loadFromString(serialized);
            return new GiveItemData(yml.getItemStack("item"), yml.getInt("Reference_ID"));
        } catch (InvalidConfigurationException var3) {
            var3.printStackTrace();
            return null;
        }
    }
}
