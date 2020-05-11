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

public class TimeOutData extends ActionData {
    private int tick;
    private List<DungeonAction> todoActions;

    public int getTick() {
        return this.tick;
    }

    public void setTick(int tick) {
        this.tick = tick;
    }

    public List<DungeonAction> getTodoActions() {
        return this.todoActions;
    }

    public void setTodoActions(List<DungeonAction> todoActions) {
        this.todoActions = todoActions;
    }

    public TimeOutData(int tick, List<DungeonAction> todoActions, int id) {
        super(DungeonActionType.TIME_OUT, id);
        this.todoActions = todoActions;
        this.tick = tick;
    }

    public String serialize() {
        YamlConfiguration yml = new YamlConfiguration();
        yml.set("Reference_ID", this.getReferenceID());
        yml.set("tick", this.getTick());
        yml.set("actionType", this.getType().name());
        List<Integer> actions = new ArrayList();
        Iterator var3 = this.getTodoActions().iterator();

        while(var3.hasNext()) {
            DungeonAction da = (DungeonAction)var3.next();
            actions.add(da.getReferenceID());
        }

        yml.set("todoActions", actions);
        return yml.saveToString();
    }

    public static TimeOutData unserialize(String serialized) {
        YamlConfiguration yml = new YamlConfiguration();

        try {
            yml.loadFromString(serialized);
            List<Integer> actionIDs = yml.getIntegerList("todoActions");
            List<DungeonAction> actions = new ArrayList();
            Iterator var4 = actionIDs.iterator();

            while(var4.hasNext()) {
                int id = (Integer)var4.next();
                actions.add(DungeonAction.getByID(id));
            }

            return new TimeOutData(yml.getInt("tick"), actions, yml.getInt("Reference_ID"));
        } catch (InvalidConfigurationException var6) {
            var6.printStackTrace();
            return null;
        }
    }
}
