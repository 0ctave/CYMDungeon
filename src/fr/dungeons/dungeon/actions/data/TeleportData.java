//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package fr.dungeons.dungeon.actions.data;

import fr.dungeons.dungeon.actions.DungeonActionType;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.util.Vector;

public class TeleportData extends ActionData {
    private Vector locToTeleport;

    public Vector getLocToTeleport() {
        return this.locToTeleport;
    }

    public void setLocToTeleport(Vector locToTeleport) {
        this.locToTeleport = locToTeleport;
    }

    public TeleportData(Vector locToTeleport, int id) {
        super(DungeonActionType.TELEPORT, id);
        this.locToTeleport = locToTeleport;
    }

    public String serialize() {
        YamlConfiguration yml = new YamlConfiguration();
        yml.set("Reference_ID", this.getReferenceID());
        yml.set("actionType", this.getType().name());
        yml.set("locToTeleport", this.getLocToTeleport());
        return yml.saveToString();
    }

    public static TeleportData unserialize(String serialized) {
        YamlConfiguration yml = new YamlConfiguration();

        try {
            yml.loadFromString(serialized);
            return new TeleportData(yml.getVector("locToTeleport"), yml.getInt("Reference_ID"));
        } catch (InvalidConfigurationException var3) {
            var3.printStackTrace();
            return null;
        }
    }
}
