//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package fr.dungeons.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Reflection {
    private static String buildVersion;
    private static Class<?> packetClass;
    private static Class<?> craftPlayer;
    private static Class<?> titlePacket;
    private static Class<?> enumTitleClass;

    public Reflection() {
    }

    public static String getBuildVersion() {
        if (buildVersion == null) {
            String pack = Bukkit.getServer().getClass().getPackage().toString();
            buildVersion = pack.substring(pack.lastIndexOf(46) + 1, pack.indexOf(44, pack.lastIndexOf(46) + 1));
        }

        return buildVersion;
    }

    public static void sendPacket(Player p, Object packet) {
        try {
            if (packetClass == null) {
                packetClass = Class.forName("net.minecraft.server." + getBuildVersion() + ".Packet");
            }

            if (craftPlayer == null) {
                craftPlayer = Class.forName("org.bukkit.craftbukkit." + getBuildVersion() + ".entity.CraftPlayer");
            }

            Object player = craftPlayer.cast(p);
            Object entityPlayer = player.getClass().getMethod("getHandle").invoke(player);
            Field playerConnection = entityPlayer.getClass().getField("playerConnection");
            Method sendPacket = playerConnection.getType().getMethod("sendPacket", packetClass);
            sendPacket.invoke(playerConnection.get(entityPlayer), packet);
        } catch (NoSuchFieldException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | ClassNotFoundException var6) {
            var6.printStackTrace();
        }

    }

    public static Object buildTitlePacket(String title) {
        title = "{\"text\":\"" + title + "\"}";

        try {
            if (titlePacket == null) {
                titlePacket = Class.forName("net.minecraft.server." + getBuildVersion() + ".PacketPlayOutTitle");
            }

            if (enumTitleClass == null) {
                enumTitleClass = Class.forName("net.minecraft.server." + getBuildVersion() + ".PacketPlayOutTitle$EnumTitleAction");
            }

            Object enumTitle = enumTitleClass.getMethod("valueOf", String.class).invoke((Object)null, "TITLE");
            Method stringToChatComponent = Class.forName("net.minecraft.server." + getBuildVersion() + ".IChatBaseComponent$ChatSerializer").getMethod("a", String.class);
            Object chatComponent = stringToChatComponent.invoke((Object)null, title);
            Object packet = titlePacket.getConstructor(enumTitle.getClass(), Class.forName("net.minecraft.server." + getBuildVersion() + ".IChatBaseComponent")).newInstance(enumTitle, chatComponent);
            return packet;
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | InstantiationException | ClassNotFoundException var5) {
            var5.printStackTrace();
            return null;
        }
    }

    public static Object buildSubTitlePacket(String subtitle) {
        subtitle = "{\"text\":\"" + subtitle + "\"}";

        try {
            Class<?> titlePacket = Class.forName("net.minecraft.server." + getBuildVersion() + ".PacketPlayOutTitle");
            Class<?> enumTitleClass = Class.forName("net.minecraft.server." + getBuildVersion() + ".PacketPlayOutTitle$EnumTitleAction");
            Object enumTitle = enumTitleClass.getMethod("valueOf", String.class).invoke((Object)null, "SUBTITLE");
            Method stringToChatComponent = Class.forName("net.minecraft.server." + getBuildVersion() + ".IChatBaseComponent$ChatSerializer").getMethod("a", String.class);
            Object chatComponent = stringToChatComponent.invoke((Object)null, subtitle);
            Object packet = titlePacket.getConstructor(enumTitle.getClass(), Class.forName("net.minecraft.server." + getBuildVersion() + ".IChatBaseComponent")).newInstance(enumTitle, chatComponent);
            return packet;
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | InstantiationException | ClassNotFoundException var7) {
            var7.printStackTrace();
            return null;
        }
    }
}
