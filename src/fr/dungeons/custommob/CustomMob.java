//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package fr.dungeons.custommob;

import fr.dungeons.Dungeons;
import fr.dungeons.Logger;
import fr.dungeons.dungeon.StandardizedObject;
import fr.dungeons.utils.NBTEditor;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CustomMob implements InventoryHolder, StandardizedObject {
    private static HashMap<Integer, CustomMob> loadedCustomMobs = new HashMap();
    private static boolean isEnabled = false;
    private String DisplayName;
    private double MaxHealth = -1.0D;
    private ItemStack Helmet;
    private ItemStack Chestplate;
    private ItemStack Leggings;
    private ItemStack Boots;
    private ItemStack Hand;
    private float HelmetDropChance = 0.0F;
    private float BootsDropChance = 0.0F;
    private float LeggingsDropChance = 0.0F;
    private float ChestplateDropChance = 0.0F;
    private float HandDropChance = 0.0F;
    private int Reference_ID;
    private EntityType Type;
    private Inventory inventory;

    public static void enableCustomMob() {
        if (!isEnabled) {
            Bukkit.getPluginManager().registerEvents(new CustomMobListener(), Dungeons.getPlugin());
            File folder = new File(Dungeons.getPlugin().getDataFolder().getAbsolutePath() + File.separatorChar + "CustomMobs");
            if (!folder.exists()) {
                folder.mkdir();
            }

            File[] var1 = folder.listFiles();
            int var2 = var1.length;

            for(int var3 = 0; var3 < var2; ++var3) {
                File f = var1[var3];
                if (f.isFile() && f.canRead() && f.getName().endsWith(".yml")) {
                    CustomMob cm = unserialize(f);
                    Logger.info(new String[]{"Loaded CustomMod ID " + cm.getReferenceID() + " from file"});
                }
            }

            isEnabled = true;
        }
    }

    public static void distableCustomMob() {
        if (isEnabled) {
            HandlerList.unregisterAll(CustomMobListener.instance);
            isEnabled = false;
        }
    }

    public static int generateID() {
        int id;
        for(id = 0; loadedCustomMobs.containsKey(id); ++id) {
        }

        return id;
    }

    public CustomMob(int id) {
        this.Type = EntityType.ZOMBIE;
        this.Reference_ID = id;
        this.inventory = Bukkit.createInventory(this, 9, ChatColor.GRAY + "Loots CustomMob " + id);
        loadedCustomMobs.put(id, this);
        ItemStack is = new ItemStack(Material.LEGACY_SKULL);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(ChatColor.BLUE + "Reference ID: " + this.getReferenceID());
        im.setLore(Arrays.asList(ChatColor.GRAY + "Display name: " + ChatColor.BLUE + (this.getDisplayName() == null ? "pas spécifié" : this.getDisplayName())));
        is.setItemMeta(im);
    }

    public static HashMap<Integer, CustomMob> getLoadedCustomMobs() {
        return loadedCustomMobs;
    }

    public int getReferenceID() {
        return this.Reference_ID;
    }

    public void addItemToDrop(ItemStack item, float dropChance) {
        item = NBTEditor.setFloat(item, "drop_chance", dropChance);
        this.inventory.addItem(new ItemStack[]{item});
    }

    public void removeItemToDrop(ItemStack item) {
        this.inventory.remove(item);
    }

    public HashMap<ItemStack, Float> getItemsToDrop() {
        HashMap<ItemStack, Float> itemsToDrop = new HashMap();
        ItemStack[] var2 = this.getInventory().getContents();
        int var3 = var2.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            ItemStack is = var2[var4];
            if (is != null && is.getType() != Material.AIR) {
                float chance = NBTEditor.getFloat(is, "drop_chance");
                if (chance == -1.0F) {
                    itemsToDrop.put(NBTEditor.removeNBT(is, "drop_chance"), 1.0F);
                } else {
                    itemsToDrop.put(is, chance);
                }
            }
        }

        return itemsToDrop;
    }

    public float getHandDropChance() {
        return this.HandDropChance;
    }

    public void setHandDropChance(float handDropChance) {
        this.HandDropChance = Math.min(1.0F, Math.max(0.0F, handDropChance));
    }

    public float getHelmetDropChance() {
        return this.HelmetDropChance;
    }

    public void setHelmetDropChance(float helmetDropChance) {
        this.HelmetDropChance = Math.min(1.0F, Math.max(0.0F, helmetDropChance));
    }

    public float getBootsDropChance() {
        return this.BootsDropChance;
    }

    public void setBootsDropChance(float bootsDropChance) {
        this.BootsDropChance = Math.min(1.0F, Math.max(0.0F, bootsDropChance));
    }

    public float getLeggingsDropChance() {
        return this.LeggingsDropChance;
    }

    public void setLeggingsDropChance(float leggingsDropChance) {
        this.LeggingsDropChance = Math.min(1.0F, Math.max(0.0F, leggingsDropChance));
    }

    public float getChestplateDropChance() {
        return this.ChestplateDropChance;
    }

    public void setChestplateDropChance(float chestplateDropChance) {
        this.ChestplateDropChance = Math.min(1.0F, Math.max(0.0F, chestplateDropChance));
    }

    public String getDisplayName() {
        return this.DisplayName;
    }

    public void setDisplayName(String displayName) {
        this.DisplayName = displayName.substring(0, Math.min(16, displayName.length()));
    }

    public double getMaxHealth() {
        return this.MaxHealth;
    }

    public void setMaxHealth(double maxHealth) {
        this.MaxHealth = maxHealth;
    }

    public ItemStack getHelmet() {
        return this.Helmet;
    }

    public void setHelmet(ItemStack helmet) {
        this.Helmet = helmet;
    }

    public ItemStack getChestplate() {
        return this.Chestplate;
    }

    public void setChestplate(ItemStack chestPlate) {
        this.Chestplate = chestPlate;
    }

    public ItemStack getLeggings() {
        return this.Leggings;
    }

    public void setLeggings(ItemStack leggings) {
        this.Leggings = leggings;
    }

    public ItemStack getBoots() {
        return this.Boots;
    }

    public void setBoots(ItemStack boots) {
        this.Boots = boots;
    }

    public ItemStack getHand() {
        return this.Hand;
    }

    public void setHand(ItemStack hand) {
        this.Hand = hand;
    }

    public void setType(EntityType type) {
        this.Type = type;
    }

    public EntityType getType() {
        return this.Type;
    }

    public String serialize() {
        YamlConfiguration fc = new YamlConfiguration();
        fc.set("Reference_ID", this.getReferenceID());
        fc.set("type", this.getType().name());
        if (this.getMaxHealth() > 0.0D) {
            fc.set("max_health", this.getMaxHealth());
        }

        if (this.getDisplayName() != null) {
            fc.set("display_name", this.getDisplayName());
        }

        if (this.getHelmet() != null) {
            fc.set("helmet", this.getHelmet());
        }

        if (this.getLeggings() != null) {
            fc.set("leggings", this.getLeggings());
        }

        if (this.getBoots() != null) {
            fc.set("boots", this.getBoots());
        }

        if (this.getChestplate() != null) {
            fc.set("chestplate", this.getChestplate());
        }

        if (this.getHand() != null) {
            fc.set("hand", this.getHand());
        }

        fc.set("helmet_drop_chance", Float.toString(this.getHelmetDropChance()));
        fc.set("leggings_drop_chance", Float.toString(this.getLeggingsDropChance()));
        fc.set("boots_drop_chance", Float.toString(this.getBootsDropChance()));
        fc.set("chestplate_drop_chance", Float.toString(this.getChestplateDropChance()));
        fc.set("hand_drop_chance", Float.toString(this.getHandDropChance()));
        ConfigurationSection lootSection = fc.createSection("ItemsToLoot");
        int index = 1;

        for(Iterator var4 = this.getItemsToDrop().keySet().iterator(); var4.hasNext(); ++index) {
            ItemStack is = (ItemStack)var4.next();
            ConfigurationSection cs = lootSection.createSection("item-" + index);
            cs.set("item", is);
            cs.set("drop_chance", Float.toString((Float)this.getItemsToDrop().get(is)));
        }

        return fc.saveToString();
    }

    public static CustomMob unserialize(File f) {
        YamlConfiguration fc = new YamlConfiguration();

        try {
            fc.load(f);
        } catch (InvalidConfigurationException | IOException var3) {
            Logger.error(new String[]{"Unable to load CustomMob from file: " + var3.getMessage()});
            return null;
        }

        return unserialize(fc.saveToString());
    }

    public static CustomMob unserialize(String s) {
        YamlConfiguration fc = new YamlConfiguration();

        try {
            fc.loadFromString(s);
        } catch (InvalidConfigurationException var6) {
            Logger.error(new String[]{"Unable to load CustomMob from string: " + var6.getMessage()});
            return null;
        }

        CustomMob cm = null;
        if (!fc.isInt("Reference_ID")) {
            Logger.error(new String[]{"Unable to unserialize CustomMob, no Reference_ID found"});
            return cm;
        } else {
            cm = new CustomMob(fc.getInt("Reference_ID"));
            if (fc.isString("type")) {
                cm.setType(EntityType.valueOf(fc.getString("type")));
            }

            if (fc.isString("display_name")) {
                cm.setDisplayName(fc.getString("display_name"));
            }

            if (fc.isDouble("max_health")) {
                cm.setMaxHealth(fc.getDouble("max_health"));
            }

            if (fc.isItemStack("boots")) {
                cm.setBoots(fc.getItemStack("boots"));
            }

            if (fc.isItemStack("helmet")) {
                cm.setHelmet(fc.getItemStack("helmet"));
            }

            if (fc.isItemStack("leggings")) {
                cm.setLeggings(fc.getItemStack("leggings"));
            }

            if (fc.isItemStack("hand")) {
                cm.setHand(fc.getItemStack("hand"));
            }

            if (fc.isItemStack("chestplate")) {
                cm.setChestplate(fc.getItemStack("chestplate"));
            }

            if (fc.isString("helmet_drop_chance")) {
                cm.setHelmetDropChance(Float.parseFloat(fc.getString("helmet_drop_chance")));
            }

            if (fc.isString("boots_drop_chance")) {
                cm.setBootsDropChance(Float.parseFloat(fc.getString("boots_drop_chance")));
            }

            if (fc.isString("leggings_drop_chance")) {
                cm.setLeggingsDropChance(Float.parseFloat(fc.getString("leggings_drop_chance")));
            }

            if (fc.isString("chestplate_drop_chance")) {
                cm.setChestplateDropChance(Float.parseFloat(fc.getString("chestplate_drop_chance")));
            }

            if (fc.isString("hand_drop_chance")) {
                cm.setHandDropChance(Float.parseFloat(fc.getString("hand_drop_chance")));
            }

            ConfigurationSection lootSection = fc.getConfigurationSection("ItemsToLoot");
            if (lootSection != null) {
                for(int index = 1; lootSection.isConfigurationSection("item-" + index); ++index) {
                    ConfigurationSection cs = lootSection.getConfigurationSection("item-" + index);
                    cm.addItemToDrop(cs.getItemStack("item"), Float.parseFloat(cs.getString("drop_chance")));
                }
            }

            return cm;
        }
    }

    public LivingEntity spawn(Location loc) {
        LivingEntity le = (LivingEntity)loc.getWorld().spawnEntity(loc, this.getType());
        le.setCanPickupItems(false);
        if (this.getMaxHealth() > 0.0D) {
            le.setMaxHealth(this.getMaxHealth());
            le.setHealth(this.getMaxHealth());
        }

        if (this.getDisplayName() != null) {
            le.setCustomName(this.getDisplayName());
            le.setCustomNameVisible(true);
        }

        EntityEquipment eq = le.getEquipment();
        if (this.getHelmet() != null) {
            eq.setHelmet(this.getHelmet());
        }

        if (this.getLeggings() != null) {
            eq.setLeggings(this.getLeggings());
        }

        if (this.getBoots() != null) {
            eq.setBoots(this.getBoots());
        }

        if (this.getChestplate() != null) {
            eq.setChestplate(this.getChestplate());
        }

        if (this.getHand() != null) {
            eq.setItemInHand(this.getHand());
        }

        eq.setHelmetDropChance(this.getHelmetDropChance());
        eq.setLeggingsDropChance(this.getLeggingsDropChance());
        eq.setBootsDropChance(this.getBootsDropChance());
        eq.setChestplateDropChance(this.getChestplateDropChance());
        eq.setItemInHandDropChance(this.getHandDropChance());
        if (this.getItemsToDrop().size() > 0) {
            CustomMobListener.listeningEntities.put(le.getUniqueId(), this.getItemsToDrop());
        }

        le.setRemoveWhenFarAway(false);
        return le;
    }

    public static CustomMob getByID(int id) {
        return (CustomMob)loadedCustomMobs.get(id);
    }

    public Inventory getInventory() {
        return this.inventory;
    }
}
