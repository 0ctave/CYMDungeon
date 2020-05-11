//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package fr.dungeons.dungeon.actions.data;

import fr.dungeons.dungeon.actions.DungeonActionType;

public abstract class ActionData {
    private int Reference_ID;
    private DungeonActionType type;

    public ActionData(DungeonActionType type, int id) {
        this.Reference_ID = id;
        this.type = type;
    }

    public int getReferenceID() {
        return this.Reference_ID;
    }

    public abstract String serialize();

    public DungeonActionType getType() {
        return this.type;
    }
}
