//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package fr.dungeons.dungeon.actions.data;

import fr.dungeons.dungeon.actions.DungeonActionType;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

public class TitleData extends ActionData {
    private String title;
    private String subTitle;

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return this.subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public TitleData(String title, String subTitle, int id) {
        super(DungeonActionType.DISP_TITLE, id);
        this.title = title;
        this.subTitle = subTitle;
    }

    public String serialize() {
        YamlConfiguration yml = new YamlConfiguration();
        yml.set("Reference_ID", this.getReferenceID());
        yml.set("actionType", this.getType().name());
        yml.set("title", this.getTitle());
        yml.set("subTitle", this.getSubTitle());
        return yml.saveToString();
    }

    public static TitleData unserialize(String serialized) {
        YamlConfiguration yml = new YamlConfiguration();

        try {
            yml.loadFromString(serialized);
            return new TitleData(yml.getString("title"), yml.getString("subTitle"), yml.getInt("Reference_ID"));
        } catch (InvalidConfigurationException var3) {
            var3.printStackTrace();
            return null;
        }
    }
}
