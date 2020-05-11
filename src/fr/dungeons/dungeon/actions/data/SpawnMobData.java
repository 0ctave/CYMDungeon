//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package fr.dungeons.dungeon.actions.data;

import fr.dungeons.custommob.CustomMob;
import fr.dungeons.dungeon.actions.DungeonActionType;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.util.Vector;

public class SpawnMobData extends ActionData {
    private CustomMob customMob;
    private int customMobCount = 1;
    private Vector locToSpawn;

    public Vector getLocToSpawn() {
        return this.locToSpawn;
    }

    public void setLocToSpawn(Vector locToSpawn) {
        this.locToSpawn = locToSpawn;
    }

    public SpawnMobData(Vector locToSpawn, CustomMob customMob, int id) {
        super(DungeonActionType.SPAWN_MOB, id);
        this.customMob = customMob;
        this.locToSpawn = locToSpawn;
    }

    public CustomMob getCustomMob() {
        return this.customMob;
    }

    public void setCustomMob(CustomMob customMob) {
        this.customMob = customMob;
    }

    public int getCustomMobCount() {
        return this.customMobCount;
    }

    public void setCustomMobCount(int customMobCount) {
        this.customMobCount = customMobCount;
    }

    public String serialize() {
        YamlConfiguration yml = new YamlConfiguration();
        yml.set("Reference_ID", this.getReferenceID());
        yml.set("actionType", this.getType().name());
        yml.set("customMob", this.getCustomMob().getReferenceID());
        yml.set("customMobCount", this.getCustomMobCount());
        yml.set("locToSpawn", this.getLocToSpawn());
        return yml.saveToString();
    }

    public static SpawnMobData unserialize(String serialized) {
        YamlConfiguration yml = new YamlConfiguration();

        try {
            yml.loadFromString(serialized);
            SpawnMobData data = new SpawnMobData(yml.getVector("locToSpawn"), CustomMob.getByID(yml.getInt("customMob")), yml.getInt("Reference_ID"));
            data.setCustomMobCount(yml.getInt("customMobCount"));
            return data;
        } catch (InvalidConfigurationException var3) {
            var3.printStackTrace();
            return null;
        }
    }
}
