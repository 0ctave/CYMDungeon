//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package fr.dungeons.dungeon.actions.data;

import fr.dungeons.dungeon.actions.DungeonActionType;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.util.Vector;

public class SchematicData extends ActionData {
    private String schematicName;
    private Vector locToPast;
    private boolean air = false;

    public boolean isAir() {
        return this.air;
    }

    public void setAir(boolean air) {
        this.air = air;
    }

    public String getSchematicName() {
        return this.schematicName;
    }

    public void setSchematicName(String schematicName) {
        this.schematicName = schematicName;
    }

    public Vector getLocToPast() {
        return this.locToPast;
    }

    public void setLocToPast(Vector locToPast) {
        this.locToPast = locToPast;
    }

    public SchematicData(Vector locToPaste, String schematicName, int id) {
        super(DungeonActionType.PASTE_SCHEMATIC, id);
        this.schematicName = schematicName;
        this.locToPast = locToPaste;
    }

    public String serialize() {
        YamlConfiguration yml = new YamlConfiguration();
        yml.set("Reference_ID", this.getReferenceID());
        yml.set("actionType", this.getType().name());
        yml.set("schematicName", this.getSchematicName());
        yml.set("locToPaste", this.getLocToPast());
        yml.set("air", this.isAir());
        return yml.saveToString();
    }

    public static SchematicData unserialize(String serialized) {
        YamlConfiguration yml = new YamlConfiguration();

        try {
            yml.loadFromString(serialized);
            return new SchematicData(yml.getVector("locToPaste"), yml.getString("schematicName"), yml.getInt("Reference_ID"));
        } catch (InvalidConfigurationException var3) {
            var3.printStackTrace();
            return null;
        }
    }
}
