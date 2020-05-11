//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package fr.dungeons.dungeon.actions.data;

import fr.dungeons.dungeon.actions.DungeonActionType;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

public class ExpData extends ActionData {
    private int expAmount;

    public int getExpAmount() {
        return this.expAmount;
    }

    public void setExpAmount(int expAmount) {
        this.expAmount = expAmount;
    }

    public ExpData(int expAmount, int id) {
        super(DungeonActionType.GIVE_EXP, id);
        this.expAmount = expAmount;
    }

    public String serialize() {
        YamlConfiguration yml = new YamlConfiguration();
        yml.set("Reference_ID", this.getReferenceID());
        yml.set("actionType", this.getType().name());
        yml.set("extAmount", this.getExpAmount());
        return yml.saveToString();
    }

    public static ExpData unserialize(String serialized) {
        YamlConfiguration yml = new YamlConfiguration();

        try {
            yml.loadFromString(serialized);
            return new ExpData(yml.getInt("expAmount"), yml.getInt("Reference_ID"));
        } catch (InvalidConfigurationException var3) {
            var3.printStackTrace();
            return null;
        }
    }
}
