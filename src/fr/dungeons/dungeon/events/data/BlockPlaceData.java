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
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.util.Vector;

public class BlockPlaceData extends EventData {
    private Material blockType;
    private Vector blockLocation;

    public BlockPlaceData(Vector blockLocation, Material blockType, List<DungeonAction> actionsTodo, int id) {
        super(DungeonEventType.PLACE_BLOCK, actionsTodo, id);
        this.blockLocation = blockLocation;
        this.blockType = blockType;
    }

    public Material getBlockType() {
        return this.blockType;
    }

    public void setBlockType(Material blockType) {
        this.blockType = blockType;
    }

    public Vector getBlockLocation() {
        return this.blockLocation;
    }

    public void setBlockLocation(Vector blockLocation) {
        this.blockLocation = blockLocation;
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
        yml.set("blockType", this.getBlockType().name());
        yml.set("blockLocation", this.getBlockLocation());
        return yml.saveToString();
    }

    public static BlockPlaceData unserialize(String serialized) {
        YamlConfiguration yml = new YamlConfiguration();

        try {
            yml.loadFromString(serialized);
            List<DungeonAction> actionsTodo = new ArrayList();
            Iterator var3 = yml.getIntegerList("actionsTodo").iterator();

            while(var3.hasNext()) {
                int id = (Integer)var3.next();
                actionsTodo.add(DungeonAction.getByID(id));
            }

            return new BlockPlaceData(yml.getVector("blockLocation"), Material.valueOf(yml.getString("blockType")), actionsTodo, yml.getInt("Reference_ID"));
        } catch (InvalidConfigurationException var5) {
            var5.printStackTrace();
            return null;
        }
    }
}
