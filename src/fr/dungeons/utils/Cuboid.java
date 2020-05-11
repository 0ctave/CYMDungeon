//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package fr.dungeons.utils;

import fr.dungeons.Logger;
import java.io.File;
import java.io.IOException;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.util.Vector;

public class Cuboid {
    private Vector pos1;
    private Vector pos2;

    public Cuboid(Vector pos1, Vector pos2) {
        this.pos1 = pos1.clone();
        this.pos2 = pos2.clone();
    }

    public boolean isIn(Location loc) {
        if (loc.getX() < Math.min(this.pos1.getX(), this.pos2.getX())) {
            return false;
        } else if (loc.getX() >= 1.0D + Math.max(this.pos1.getX(), this.pos2.getX())) {
            return false;
        } else if (loc.getY() < Math.min(this.pos1.getY(), this.pos2.getY())) {
            return false;
        } else if (loc.getY() >= 1.0D + Math.max(this.pos1.getY(), this.pos2.getY())) {
            return false;
        } else if (loc.getZ() < Math.min(this.pos1.getZ(), this.pos2.getZ())) {
            return false;
        } else {
            return loc.getZ() < 1.0D + Math.max(this.pos1.getZ(), this.pos2.getZ());
        }
    }

    public void saveToFile(File f) {
        YamlConfiguration yml = new YamlConfiguration();
        yml.set("pos1x", this.pos1.getX());
        yml.set("pos1y", this.pos1.getY());
        yml.set("pos1z", this.pos1.getZ());
        yml.set("pos2x", this.pos2.getX());
        yml.set("pos2y", this.pos2.getY());
        yml.set("pos2z", this.pos2.getZ());

        try {
            yml.save(f);
        } catch (IOException var4) {
            Logger.error(new String[]{"Unable to save Cuboid to file " + f.getName() + " : " + var4.getMessage()});
        }

    }

    public YamlConfiguration saveToConfiguration() {
        YamlConfiguration yml = new YamlConfiguration();
        yml.set("pos1x", this.pos1.getX());
        yml.set("pos1y", this.pos1.getY());
        yml.set("pos1z", this.pos1.getZ());
        yml.set("pos2x", this.pos2.getX());
        yml.set("pos2y", this.pos2.getY());
        yml.set("pos2z", this.pos2.getZ());
        return yml;
    }

    public static Cuboid loadFromConfiguration(ConfigurationSection cs) {
        Vector pos1 = new Vector(cs.getDouble("pos1x"), cs.getDouble("pos1y"), cs.getDouble("pos1z"));
        Vector pos2 = new Vector(cs.getDouble("pos2x"), cs.getDouble("pos2y"), cs.getDouble("pos2z"));
        return new Cuboid(pos1, pos2);
    }

    public static Cuboid loadFromFile(File f) {
        if (f.exists() && f.getName().endsWith(".yml")) {
            YamlConfiguration yml = new YamlConfiguration();

            try {
                yml.load(f);
                return loadFromConfiguration(yml);
            } catch (InvalidConfigurationException | IOException var3) {
                Logger.error(new String[]{"Unable to load Cuboid from file " + f.getName() + " : " + var3.getMessage()});
                return null;
            }
        } else {
            return null;
        }
    }

    public Vector getPos1() {
        return this.pos1.clone();
    }

    public Vector getPos2() {
        return this.pos2.clone();
    }

    public Location getPos1(World w) {
        return this.pos1.toLocation(w);
    }

    public Location getPos2(World w) {
        return this.pos2.toLocation(w);
    }
}
