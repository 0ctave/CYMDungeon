//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package fr.dungeons.dungeon.events.data;

import fr.dungeons.dungeon.actions.DungeonAction;
import fr.dungeons.dungeon.events.DungeonEventType;
import java.util.List;

public abstract class EventData {
    private int Reference_ID;
    private DungeonEventType type;
    private List<DungeonAction> actionsTodo;

    public EventData(DungeonEventType type, List<DungeonAction> actionsTodo, int id) {
        this.Reference_ID = id;
        this.type = type;
        this.actionsTodo = actionsTodo;
    }

    public int getReferenceID() {
        return this.Reference_ID;
    }

    public List<DungeonAction> getActionsTodo() {
        return this.actionsTodo;
    }

    public abstract String serialize();

    public DungeonEventType getType() {
        return this.type;
    }
}
