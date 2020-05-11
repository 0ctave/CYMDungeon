//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package fr.dungeons.dungeon;

import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.world.DataException;
import fr.dungeons.Dungeons;
import fr.dungeons.Logger;
import fr.dungeons.dungeon.events.DungeonEvent;
import fr.dungeons.utils.Cuboid;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.util.Vector;

public class Dungeon implements StandardizedObject {
    private static HashMap<String, Clipboard> loadedSchematics = new HashMap();
    private String dungeonName;
    private Vector spawn;
    private int minPlayer = 0;
    private int maxPlayer = 1000;
    private int maxDuration = 0;
    private Cuboid area;
    private int reference_ID;
    private int maxHealth = 3;
    private List<Material> placableBlocks = new ArrayList();
    private List<Material> breakableBlocks = new ArrayList();
    private List<DungeonEvent> events = new ArrayList();

    public Dungeon(int id, Vector pos1, Vector pos2, Vector spawn, String name) {
        this.reference_ID = id;
        this.area = new Cuboid(pos1, pos2);
        this.setSpawn(spawn);
        this.setName(name);
    }

    public boolean equals(Object o) {
        return o instanceof Dungeon && ((Dungeon)o).getReferenceID() == this.getReferenceID();
    }

    public int getReferenceID() {
        return this.reference_ID;
    }

    public int getMaxHealth() {
        return this.maxHealth;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    public List<Material> getPlacableBlocks() {
        return this.placableBlocks;
    }

    public void setPlacableBlocks(List<Material> placableBlocks) {
        this.placableBlocks = placableBlocks;
    }

    public List<Material> getBreakableBlocks() {
        return this.breakableBlocks;
    }

    public void setBreakableBlocks(List<Material> breakableBlocks) {
        this.breakableBlocks = breakableBlocks;
    }

    public void setSpawn(Vector v) {
        this.spawn = v.clone();
    }

    public Vector getSpawn() {
        return this.spawn.clone();
    }

    public Location getSpawn(World w) {
        return this.getSpawn().toLocation(w);
    }

    public void setName(String name) {
        this.dungeonName = name;
    }

    public String getName() {
        return this.dungeonName;
    }

    public int getMaxPlayer() {
        return this.maxPlayer;
    }

    public int getMinPlayer() {
        return this.minPlayer;
    }

    public void setMaxPlayer(int i) {
        if (i < 1) {
            this.maxPlayer = 1;
        } else if (i < this.getMinPlayer()) {
            this.maxPlayer = this.getMinPlayer();
        } else {
            this.maxPlayer = i;
        }

    }

    public void setMinPlayer(int i) {
        if (i < 0) {
            this.minPlayer = 0;
        } else if (i > this.getMaxPlayer()) {
            this.minPlayer = this.getMaxPlayer();
        } else {
            this.minPlayer = i;
        }

    }

    public Cuboid getCuboid() {
        return this.area;
    }

    public void setMaxDuration(int max) {
        if (max <= 0) {
            this.maxDuration = 0;
        } else {
            this.maxDuration = max;
        }

    }

    public int getMaxDuration() {
        return this.maxDuration;
    }

    public void addEvent(DungeonEvent event) {
        this.events.add(event);
    }

    public List<DungeonEvent> getEvents() {
        return new ArrayList(this.events);
    }

    public String serialize() {
        YamlConfiguration yml = new YamlConfiguration();
        yml.set("name", this.getName());
        yml.set("Reference_ID", this.getReferenceID());
        yml.set("min_players", this.getMinPlayer());
        yml.set("max_players", this.getMaxPlayer());
        yml.set("max_duration", this.getMaxDuration());
        yml.set("spawn", this.getSpawn());
        yml.set("max_health", this.getMaxHealth());
        List<String> placable = new ArrayList();
        Iterator var3 = this.getPlacableBlocks().iterator();

        while(var3.hasNext()) {
            Material m = (Material)var3.next();
            placable.add(m.name());
        }

        yml.set("placable", placable);
        List<String> breakable = new ArrayList();
        Iterator var9 = this.getBreakableBlocks().iterator();

        while(var9.hasNext()) {
            Material m = (Material)var9.next();
            breakable.add(m.name());
        }

        yml.set("breakable", breakable);
        List<Integer> eventsIDS = new ArrayList();
        Iterator var11 = this.getEvents().iterator();

        while(var11.hasNext()) {
            DungeonEvent event = (DungeonEvent)var11.next();
            eventsIDS.add(event.getReferenceID());
        }

        yml.set("events", eventsIDS);
        ConfigurationSection cs = yml.createSection("cuboid");
        Iterator var13 = this.getCuboid().saveToConfiguration().getValues(true).entrySet().iterator();

        while(var13.hasNext()) {
            Entry<String, Object> entry = (Entry)var13.next();
            cs.set((String)entry.getKey(), entry.getValue());
        }

        return yml.saveToString();
    }

    public static Dungeon unserialize(String serialized) {
        YamlConfiguration yml = new YamlConfiguration();

        try {
            yml.loadFromString(serialized);
        } catch (InvalidConfigurationException var16) {
            Logger.error(new String[]{"Unable to load Dungeon from String : " + var16.getMessage()});
            return null;
        }

        if (!yml.contains("Reference_ID")) {
            Logger.error(new String[]{"Can't load Dungeon from String, no Reference_ID"});
            return null;
        } else {
            String name = yml.getString("name");
            Cuboid area = Cuboid.loadFromConfiguration(yml.getConfigurationSection("cuboid"));
            int min = yml.getInt("min_players");
            int max = yml.getInt("max_players");
            int maxduration = yml.getInt("max_duration");
            int id = yml.getInt("Reference_ID");
            int health = yml.getInt("max_health");
            Vector spawn = yml.getVector("spawn");
            Dungeon d = new Dungeon(id, area.getPos1(), area.getPos2(), spawn, name);
            d.setMaxPlayer(max);
            d.setMinPlayer(min);
            d.setMaxDuration(maxduration);
            d.setMaxHealth(health);
            List<Material> placable = new ArrayList();
            Iterator var12 = yml.getStringList("placable").iterator();

            while(var12.hasNext()) {
                String s = (String)var12.next();
                placable.add(Material.valueOf(s));
            }

            d.setPlacableBlocks(placable);
            List<Material> breakable = new ArrayList();
            Iterator var18 = yml.getStringList("breakable").iterator();

            while(var18.hasNext()) {
                String s = (String)var18.next();
                breakable.add(Material.valueOf(s));
            }

            d.setBreakableBlocks(breakable);
            List<Integer> eventsIDS = yml.getIntegerList("events");
            Iterator var20 = eventsIDS.iterator();

            while(var20.hasNext()) {
                int eid = (Integer)var20.next();
                d.addEvent(DungeonEvent.getByID(eid));
            }

            return d;
        }
    }

    public static Dungeon loadFromFile(File f) {
        if (f.exists() && f.getName().endsWith(".yml")) {
            YamlConfiguration yml = new YamlConfiguration();

            try {
                yml.load(f);
                return unserialize(yml.saveToString());
            } catch (Exception var3) {
                Logger.error(new String[]{"Unable to load Dungeon from file " + f.getName() + " : " + var3.getMessage()});
                return null;
            }
        } else {
            return null;
        }
    }

    public static void loadShematics() {
        File schematicsFolder = new File(Dungeons.getPlugin().getDataFolder().getAbsolutePath() + File.separatorChar + "Schematics");
        if (!schematicsFolder.exists()) {
            schematicsFolder.mkdir();
        }

        File[] var1 = schematicsFolder.listFiles();
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            File sc = var1[var3];
            if (sc.getName().endsWith(".schematic")) {
                try {
                    ClipboardFormat format = ClipboardFormats.findByFile(sc);
                    ClipboardReader reader = format.getReader(new FileInputStream(sc));
                    Clipboard clipboard = reader.read();
                    String name = sc.getName().substring(0, sc.getName().indexOf(".schematic"));
                    loadedSchematics.put(name, clipboard);
                    Logger.info(new String[]{"Schematic " + name + " loaded"});
                } catch (IOException var7) {
                    Logger.error(new String[]{"Can't load schematic file " + sc.getName() + " : " + var7.getMessage()});
                }
            }
        }

    }

    public static Clipboard getSchematic(String schemName) {
        return (Clipboard)loadedSchematics.get(schemName);
    }
}
