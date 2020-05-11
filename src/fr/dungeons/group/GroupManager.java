//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package fr.dungeons.group;

import java.util.Iterator;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GroupManager {
    private static Command command = Bukkit.getPluginCommand("groupe");

    public GroupManager() {
    }

    public static void onCommand(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            if (args.length == 0) {
                sender.sendMessage(ChatColor.RED + command.getUsage());
            } else if (args.length == 1) {
                if (args[0].equalsIgnoreCase("create")) {
                    createGroupeForPlayer(sender.getName());
                } else {
                    Group g;
                    if (args[0].equalsIgnoreCase("info")) {
                        g = Group.getPlayerGroup(sender.getName());
                        if (g == null) {
                            sender.sendMessage(Group.getPrefix() + ChatColor.RED + "Vous n'avez pas de groupe, faite " + ChatColor.GRAY + "/groupe create" + ChatColor.RED + " pour en creer un");
                        } else {
                            displayInfo((Player)sender, g);
                        }
                    } else if (args[0].equalsIgnoreCase("leave")) {
                        g = Group.getPlayerGroup(sender.getName());
                        if (g == null) {
                            sender.sendMessage(Group.getPrefix() + ChatColor.RED + "Vous n'avez pas de groupe");
                        }
                    }
                }
            }
        } else {
            sender.sendMessage(ChatColor.RED + "Vous devez etre un joueur pour faire ca");
        }

    }

    public static Group createGroupeForPlayer(String playerName) {
        Group g = Group.createGroup(playerName);
        Player pla = Bukkit.getPlayer(playerName);
        if (pla == null) {
            return g;
        } else {
            if (g == null) {
                pla.sendMessage(Group.getPrefix() + ChatColor.RED + "Impossible de creer un groupe car vous êtes déjà dans le groupe de " + ChatColor.BLUE + Group.getPlayerGroup(playerName));
            } else {
                pla.sendMessage(ChatColor.GREEN + "Groupe créé !");
            }

            return g;
        }
    }

    public static void displayInfo(Player pla, Group g) {
        pla.sendMessage(Group.getPrefix() + ChatColor.GRAY + "Leader du groupe: " + ChatColor.BLUE + g.getLeader());
        pla.sendMessage(Group.getPrefix() + ChatColor.GRAY + "Membre" + (g.getPlayers().size() > 1 ? "s:" : ":"));
        Iterator var2 = g.getPlayers().iterator();

        while(true) {
            while(var2.hasNext()) {
                String s = (String)var2.next();
                Player p = Bukkit.getPlayer(s);
                if (p != null && p.isOnline()) {
                    pla.sendMessage(Group.getPrefix() + ChatColor.GRAY + "- " + ChatColor.BLUE + s + ChatColor.GRAY + " (" + ChatColor.GREEN + "Connecté" + ChatColor.GRAY + ")");
                } else {
                    pla.sendMessage(Group.getPrefix() + ChatColor.GRAY + "- " + ChatColor.BLUE + s + ChatColor.GRAY + " (" + ChatColor.RED + "Déconnecté" + ChatColor.GRAY + ")");
                }
            }

            return;
        }
    }
}
