//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package fr.dungeons.dungeon.actions.data;

import fr.dungeons.dungeon.actions.DungeonActionType;
import java.util.List;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

public class ActivateEventData extends ActionData {
    private List<Integer> todoEventsID;

    public List<Integer> getTodoEvents() {
        return this.todoEventsID;
    }

    public void setTodoEvents(List<Integer> todoEventsID) {
        this.todoEventsID = todoEventsID;
    }

    public ActivateEventData(List<Integer> todoEventsID, int id) {
        super(DungeonActionType.ACTIVATE_EVENT, id);
        this.todoEventsID = todoEventsID;
    }

    public String serialize() {
        YamlConfiguration yml = new YamlConfiguration();
        yml.set("Reference_ID", this.getReferenceID());
        yml.set("actionType", this.getType().name());
        yml.set("todoEvents", this.todoEventsID);
        return yml.saveToString();
    }

    public static ActivateEventData unserialize(String serialized) {
        YamlConfiguration yml = new YamlConfiguration();

        try {
            yml.loadFromString(serialized);
            return new ActivateEventData(yml.getIntegerList("todoEvents"), yml.getInt("Reference_ID"));
        } catch (InvalidConfigurationException var3) {
            var3.printStackTrace();
            return null;
        }
    }
}
