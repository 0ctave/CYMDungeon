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

public class AreaDetectionData extends EventData {
    private List<Vector> activationLocations;
    private double activationRadius;

    public AreaDetectionData(List<Vector> activationLocations, List<DungeonAction> actionsTodo, double activationRadius, int id) {
        super(DungeonEventType.AREA_DETECTION, actionsTodo, id);
        this.activationLocations = activationLocations;
        this.activationRadius = activationRadius;
    }

    public double getActivationRadius() {
        return this.activationRadius;
    }

    public void setActivationRadius(double activationRadius) {
        this.activationRadius = activationRadius;
    }

    public List<Vector> getActivationLocations() {
        return this.activationLocations;
    }

    public void setActivationLocations(List<Vector> activationLocations) {
        this.activationLocations = activationLocations;
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
        yml.set("activationRadius", this.getActivationRadius());
        yml.set("activationLocations", this.getActivationLocations());
        return yml.saveToString();
    }

    public static AreaDetectionData unserialize(String serialized) {
        YamlConfiguration yml = new YamlConfiguration();

        try {
            yml.loadFromString(serialized);
            List<DungeonAction> actionsTodo = new ArrayList();
            Iterator var3 = yml.getIntegerList("actionsTodo").iterator();

            while(var3.hasNext()) {
                int id = (Integer)var3.next();
                actionsTodo.add(DungeonAction.getByID(id));
            }

            return new AreaDetectionData((List<Vector>) yml.getList("activationLocations"), actionsTodo, yml.getDouble("activationRadius"), yml.getInt("Reference_ID"));
        } catch (InvalidConfigurationException var5) {
            var5.printStackTrace();
            return null;
        }
    }
}
