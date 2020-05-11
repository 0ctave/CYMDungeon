//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package fr.dungeons.dungeon.actions.data;

import fr.dungeons.dungeon.actions.DungeonActionType;
import java.util.List;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

public class DispMessageData extends ActionData {
    private List<String> messages;

    public List<String> getMessages() {
        return this.messages;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }

    public DispMessageData(List<String> messages, int id) {
        super(DungeonActionType.DISP_MESSAGE, id);
        this.messages = messages;
    }

    public String serialize() {
        YamlConfiguration yml = new YamlConfiguration();
        yml.set("messages", this.getMessages());
        yml.set("Reference_ID", this.getReferenceID());
        yml.set("actionType", this.getType().name());
        return yml.saveToString();
    }

    public static DispMessageData unserialize(String serialized) {
        YamlConfiguration yml = new YamlConfiguration();

        try {
            yml.loadFromString(serialized);
            return new DispMessageData(yml.getStringList("messages"), yml.getInt("Reference_ID"));
        } catch (InvalidConfigurationException var3) {
            var3.printStackTrace();
            return null;
        }
    }
}
