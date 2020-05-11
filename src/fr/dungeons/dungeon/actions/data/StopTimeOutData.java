//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package fr.dungeons.dungeon.actions.data;

import fr.dungeons.dungeon.actions.DungeonAction;
import fr.dungeons.dungeon.actions.DungeonActionType;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

public class StopTimeOutData extends ActionData {
    private List<DungeonAction> timeOutFromActions = new ArrayList();

    public StopTimeOutData(List<DungeonAction> timeOutFromActions, int id) {
        super(DungeonActionType.STOP_TIME_OUT, id);
        Iterator var3 = timeOutFromActions.iterator();

        while(var3.hasNext()) {
            DungeonAction da = (DungeonAction)var3.next();
            if (da.getType() == DungeonActionType.STOP_TIME_OUT) {
                this.timeOutFromActions.add(da);
            }
        }

    }

    public List<DungeonAction> getTimeOutFromActions() {
        return this.timeOutFromActions;
    }

    public void setTimeOutFromActions(List<DungeonAction> timeOutFromActions) {
        this.timeOutFromActions = timeOutFromActions;
    }

    public String serialize() {
        YamlConfiguration yml = new YamlConfiguration();
        List<Integer> timeOutFromIDs = new ArrayList();
        Iterator var3 = this.getTimeOutFromActions().iterator();

        while(var3.hasNext()) {
            DungeonAction da = (DungeonAction)var3.next();
            timeOutFromIDs.add(da.getReferenceID());
        }

        yml.set("timeOutFromActions", timeOutFromIDs);
        yml.set("Reference_ID", this.getReferenceID());
        yml.set("actionType", this.getType().name());
        return yml.saveToString();
    }

    public static StopTimeOutData unserialize(String serialized) {
        YamlConfiguration yml = new YamlConfiguration();

        try {
            yml.loadFromString(serialized);
            List<DungeonAction> timeOutFromActions = new ArrayList();
            Iterator var3 = yml.getIntegerList("timeOutFromActions").iterator();

            while(var3.hasNext()) {
                int id = (Integer)var3.next();
                timeOutFromActions.add(DungeonAction.getByID(id));
            }

            return new StopTimeOutData(timeOutFromActions, yml.getInt("Reference_ID"));
        } catch (InvalidConfigurationException var5) {
            var5.printStackTrace();
            return null;
        }
    }
}
