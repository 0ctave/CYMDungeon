//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package fr.dungeons.dungeon.events.data;

import fr.dungeons.dungeon.actions.DungeonAction;
import fr.dungeons.dungeon.actions.DungeonActionType;
import fr.dungeons.dungeon.events.DungeonEventType;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

public class MobWaveEndedData extends EventData {
    private List<DungeonAction> mobFromActions = new ArrayList();

    public MobWaveEndedData(List<DungeonAction> mobFrom, List<DungeonAction> actionsTodo, int id) {
        super(DungeonEventType.MOB_WAVE_ENDED, actionsTodo, id);
        Iterator var4 = mobFrom.iterator();

        while(var4.hasNext()) {
            DungeonAction da = (DungeonAction)var4.next();
            if (da.getType() == DungeonActionType.SPAWN_MOB) {
                this.mobFromActions.add(da);
            }
        }

    }

    public List<DungeonAction> getMobFromActions() {
        return this.mobFromActions;
    }

    public void setMobFromActions(List<DungeonAction> mobFromActions) {
        this.mobFromActions = new ArrayList();
        Iterator var2 = mobFromActions.iterator();

        while(var2.hasNext()) {
            DungeonAction da = (DungeonAction)var2.next();
            if (da.getType() == DungeonActionType.SPAWN_MOB) {
                this.mobFromActions.add(da);
            }
        }

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
        List<Integer> fromActionsIDs = new ArrayList();
        Iterator var7 = this.getMobFromActions().iterator();

        while(var7.hasNext()) {
            DungeonAction da = (DungeonAction)var7.next();
            fromActionsIDs.add(da.getReferenceID());
        }

        yml.set("mobFromActions", fromActionsIDs);
        return yml.saveToString();
    }

    public static MobWaveEndedData unserialize(String serialized) {
        YamlConfiguration yml = new YamlConfiguration();

        try {
            yml.loadFromString(serialized);
            List<DungeonAction> actionsTodo = new ArrayList();
            Iterator var3 = yml.getIntegerList("actionsTodo").iterator();

            while(var3.hasNext()) {
                int id = (Integer)var3.next();
                actionsTodo.add(DungeonAction.getByID(id));
            }

            List<DungeonAction> mobFromactions = new ArrayList();
            Iterator var8 = yml.getIntegerList("mobFromActions").iterator();

            while(var8.hasNext()) {
                int id = (Integer)var8.next();
                mobFromactions.add(DungeonAction.getByID(id));
            }

            return new MobWaveEndedData(mobFromactions, actionsTodo, yml.getInt("Reference_ID"));
        } catch (InvalidConfigurationException var6) {
            var6.printStackTrace();
            return null;
        }
    }
}
