package fr.dungeons.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Title {
    public static void sendTitle(String title, String subtitle, Player... players) {
        if (title != null)
            if (players.length == 0) {
                for (Player pla : Bukkit.getOnlinePlayers())
                    Reflection.sendPacket(pla, Reflection.buildTitlePacket(title));
            } else {
                for (Player pla : players) Reflection.sendPacket(pla, Reflection.buildTitlePacket(title));
            }

        if (subtitle != null)
            if (players.length == 0) {
                for (Player pla : Bukkit.getOnlinePlayers())
                    Reflection.sendPacket(pla, Reflection.buildSubTitlePacket(subtitle));
            } else {
                for (Player pla : players) Reflection.sendPacket(pla, Reflection.buildSubTitlePacket(subtitle));
            }

    }
}