//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package fr.dungeons.dungeon.actions.data;

import fr.dungeons.dungeon.actions.DungeonActionType;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.potion.PotionEffectType;

public class PotionData extends ActionData {
    private int duration;
    private int amplifier;
    private boolean ambient = true;
    private boolean particles = true;
    private PotionEffectType effectType;

    public PotionData(PotionEffectType effectType, int duration, int amplifier, int id) {
        super(DungeonActionType.GIVE_POTION, id);
        this.amplifier = amplifier;
        this.duration = duration;
        this.effectType = effectType;
    }

    public int getDuration() {
        return this.duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getAmplifier() {
        return this.amplifier;
    }

    public void setAmplifier(int amplifier) {
        this.amplifier = amplifier;
    }

    public boolean isAmbient() {
        return this.ambient;
    }

    public void setAmbient(boolean ambient) {
        this.ambient = ambient;
    }

    public boolean isParticles() {
        return this.particles;
    }

    public void setParticles(boolean particles) {
        this.particles = particles;
    }

    public PotionEffectType getEffectType() {
        return this.effectType;
    }

    public void setType(PotionEffectType effectType) {
        this.effectType = effectType;
    }

    public String serialize() {
        YamlConfiguration yml = new YamlConfiguration();
        yml.set("Reference_ID", this.getReferenceID());
        yml.set("actionType", this.getType().name());
        yml.set("duration", this.getDuration());
        yml.set("amplifier", this.getAmplifier());
        yml.set("ambient", this.isAmbient());
        yml.set("particles", this.isParticles());
        yml.set("effectType", this.getEffectType().getName());
        return yml.saveToString();
    }

    public static PotionData unserialize(String serialized) {
        YamlConfiguration yml = new YamlConfiguration();

        try {
            yml.loadFromString(serialized);
            PotionData data = new PotionData(PotionEffectType.getByName(yml.getString("effectType")), yml.getInt("duration"), yml.getInt("amplifier"), yml.getInt("Reference_ID"));
            data.setAmbient(yml.getBoolean("ambient"));
            data.setParticles(yml.getBoolean("particles"));
            return data;
        } catch (InvalidConfigurationException var3) {
            var3.printStackTrace();
            return null;
        }
    }
}
