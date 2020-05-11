//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package fr.dungeons.dungeon.actions.data;

import fr.dungeons.Logger;
import fr.dungeons.dungeon.actions.DungeonActionType;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.util.Vector;

public class ExplosionData extends ActionData {
    private Vector explosionLocation;
    private float explosionStrength;
    private boolean breakBlocks;
    private boolean fire;

    public ExplosionData(float explosionStrength, Vector where, boolean fire, boolean breakBlocks, int id) {
        super(DungeonActionType.CREATE_EXPLOSION, id);
        this.explosionLocation = where.clone();
        this.explosionStrength = explosionStrength;
        this.fire = fire;
        this.breakBlocks = breakBlocks;
    }

    public Vector getExplosionLocation() {
        return this.explosionLocation;
    }

    public void setExplosionLocation(Vector explosionLocation) {
        this.explosionLocation = explosionLocation;
    }

    public float getExplosionStrength() {
        return this.explosionStrength;
    }

    public void setExplosionStrength(float explosionStrength) {
        this.explosionStrength = explosionStrength;
    }

    public boolean isBreakBlocks() {
        return this.breakBlocks;
    }

    public void setBreakBlocks(boolean breakBlocks) {
        this.breakBlocks = breakBlocks;
    }

    public boolean isFire() {
        return this.fire;
    }

    public void setFire(boolean fire) {
        this.fire = fire;
    }

    public String serialize() {
        YamlConfiguration yml = new YamlConfiguration();
        yml.set("Reference_ID", this.getReferenceID());
        yml.set("actionType", this.getType().name());
        yml.set("explosionLocation", this.getExplosionLocation());
        yml.set("explosionStrength", this.getExplosionStrength());
        yml.set("breakBlocks", this.isBreakBlocks());
        yml.set("fire", this.isFire());
        return yml.saveToString();
    }

    public static ExplosionData unserialize(String serialized) {
        YamlConfiguration yml = new YamlConfiguration();

        try {
            yml.loadFromString(serialized);
            ExplosionData data = new ExplosionData((float)yml.getDouble("explosionStrength"), yml.getVector("explosionLocation"), yml.getBoolean("fire"), yml.getBoolean("breakBlocks"), yml.getInt("Reference_ID"));
            return data;
        } catch (InvalidConfigurationException var3) {
            Logger.error(new String[]{"Can't unserialize ExplosionData " + var3.getMessage()});
            return null;
        }
    }
}
