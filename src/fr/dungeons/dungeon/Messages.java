//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package fr.dungeons.dungeon;

import fr.dungeons.Dungeons;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

public class Messages {
    public static String RESTER_DUNGEON;
    public static String PAS_PLACER;
    public static String PAS_CASSER;
    public static String DUNGEON_PREFIX;
    public static String WIN1;
    public static String WIN2;
    public static String LOOSE1;
    public static String LOOSE2;
    public static String JOIN;
    public static String QUIT;

    public Messages() {
    }

    public static void init() {
        File f = new File(Dungeons.getPlugin().getDataFolder().getAbsolutePath() + File.separatorChar + "Messages.yml");
        YamlConfiguration yml = new YamlConfiguration();
        Field[] var2;
        int var3;
        int var4;
        Field field;
        if (!f.exists()) {
            var2 = Messages.class.getFields();
            var3 = var2.length;

            for(var4 = 0; var4 < var3; ++var4) {
                field = var2[var4];
                if (field.getType() == String.class) {
                    try {
                        yml.set(field.getName(), field.get(Messages.class));
                    } catch (IllegalAccessException | IllegalArgumentException var12) {
                        var12.printStackTrace();
                    }
                }
            }

            try {
                yml.save(f);
            } catch (IOException var11) {
                var11.printStackTrace();
            }
        }

        yml = new YamlConfiguration();

        try {
            yml.load(f);
        } catch (InvalidConfigurationException | IOException var10) {
            var10.printStackTrace();
        }

        var2 = Messages.class.getFields();
        var3 = var2.length;

        for(var4 = 0; var4 < var3; ++var4) {
            field = var2[var4];
            if (field.getType() == String.class) {
                if (yml.isString(field.getName())) {
                    try {
                        field.set(Messages.class, yml.getString(field.getName()));
                    } catch (IllegalAccessException | IllegalArgumentException var8) {
                        var8.printStackTrace();
                    }
                } else {
                    try {
                        yml.set(field.getName(), field.get(Messages.class));
                    } catch (IllegalAccessException | IllegalArgumentException var9) {
                        var9.printStackTrace();
                    }
                }
            }
        }

        try {
            yml.save(f);
        } catch (IOException var7) {
            var7.printStackTrace();
        }

    }

    static {
        RESTER_DUNGEON = ChatColor.RED + "Restez dans le dongeon !";
        PAS_PLACER = ChatColor.RED + "Vous ne pouvez pas placer ce type de block dans le dongeon !";
        PAS_CASSER = ChatColor.RED + "Vous ne pouvez pas casser ce type de block dans le dongeon !";
        DUNGEON_PREFIX = ChatColor.GRAY + "[" + ChatColor.BLUE + "Dungeon" + ChatColor.GRAY + "] " + ChatColor.RESET;
        WIN1 = ChatColor.GOLD.toString() + ChatColor.MAGIC + "||| " + ChatColor.GREEN + "Dungeon terminé !" + ChatColor.GOLD + ChatColor.MAGIC + " |||";
        WIN2 = ChatColor.GOLD.toString() + ChatColor.MAGIC + "||| " + ChatColor.DARK_PURPLE + "Félicitations !" + ChatColor.GOLD + ChatColor.MAGIC + " |||";
        LOOSE1 = ChatColor.GOLD.toString() + ChatColor.MAGIC + "||| " + ChatColor.RED + "Défaite !" + ChatColor.GOLD + ChatColor.MAGIC + "||| ";
        LOOSE2 = ChatColor.GOLD.toString() + ChatColor.MAGIC + "||| " + ChatColor.DARK_PURPLE + "Vous n'étiez pas à la hauteur étrangers" + ChatColor.GOLD + ChatColor.MAGIC + "||| ";
        JOIN = ChatColor.GOLD + "{playername} " + ChatColor.GRAY + "a rejoint le Dongeon";
        QUIT = ChatColor.GOLD + "{playername} " + ChatColor.GRAY + "a quitté le Dongeon";
    }
}
