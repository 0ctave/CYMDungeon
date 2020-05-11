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
import org.bukkit.util.Vector;

public class InteractData extends EventData {
    private Vector blockLocation;

    public InteractData(Vector blockPosition, List<DungeonAction> actionsTodo, int id) {
        super(DungeonEventType.INTERACT, actionsTodo, id);
        this.blockLocation = blockPosition;
    }

    public Vector getBlockLocation() {
        return this.blockLocation;
    }

    public void setBlockLocation(Vector blockPosition) {
        this.blockLocation = blockPosition;
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
        yml.set("blockLocation", this.getBlockLocation());
        return yml.saveToString();
    }

    public static InteractData unserialize(String serialized) {
        YamlConfiguration yml = new YamlConfiguration();

        try {
            yml.loadFromString(serialized);
            List<DungeonAction> actionsTodo = new ArrayList();
            Iterator var3 = yml.getIntegerList("actionsTodo").iterator();

            while(var3.hasNext()) {
                int id = (Integer)var3.next();
                actionsTodo.add(DungeonAction.getByID(id));
            }

            return new InteractData(yml.getVector("blockLocation"), actionsTodo, yml.getInt("Reference_ID"));
        } catch (InvalidConfigurationException var5) {
            var5.printStackTrace();
            return null;
        }
    }
}
