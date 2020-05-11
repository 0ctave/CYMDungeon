//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package fr.dungeons.group;

import fr.dungeons.Logger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Group {
    private static List<Group> registeredGroups = new ArrayList();
    private List<String> players = new ArrayList();
    private String leader;
    private int maxSize = 10;
    private static final String prefix;

    public static String getPrefix() {
        return prefix;
    }

    public static Group createGroup(String creator) {
        Group playerGroup = getPlayerGroup(creator);
        if (playerGroup == null) {
            Group g = new Group();
            g.addPlayer(creator);
            g.setLeader(creator);
            return g;
        } else {
            Logger.info(new String[]{"Can't create group for player \"" + creator + "\" because he's already in " + playerGroup.getLeader() + "'s group"});
            return null;
        }
    }

    private Group() {
        registeredGroups.add(this);
    }

    public boolean addPlayer(String name) {
        Group playerGroup = getPlayerGroup(name);
        if (!this.getPlayers().contains(name) && this.getPlayers().size() < this.maxSize && playerGroup == null) {
            this.players.add(name);
            Player p = Bukkit.getPlayer(name);
            if (p != null) {
                p.sendMessage(getPrefix() + ChatColor.GRAY + "Vous rejoignez le groupe de " + ChatColor.BLUE + this.getLeader());
            }

            return true;
        } else if (this.getPlayers().size() >= this.maxSize) {
            Logger.info(new String[]{"Can't add player \"" + name + "\" to " + this.getLeader() + "'s group because it's full"});
            return false;
        } else if (playerGroup != null) {
            Logger.info(new String[]{"Can't add player \"" + name + "\" because he's already in " + playerGroup.getLeader() + "'s group"});
            return false;
        } else {
            return true;
        }
    }

    public boolean removePlayer(String name) {
        if (!this.getPlayers().contains(name)) {
            Logger.info(new String[]{"Unable to remove player \"" + name + "\" from " + this.getLeader() + "'s group because he isn't in"});
            return false;
        } else {
            this.players.remove(name);
            Iterator var2 = this.getOnlinePlayers().iterator();

            while(var2.hasNext()) {
                Player pla = (Player)var2.next();
                pla.sendMessage(getPrefix() + ChatColor.BLUE + name + ChatColor.GRAY + " a quitté le groupe");
            }

            if (this.getLeader().equalsIgnoreCase(name)) {
                if (this.getPlayers().size() == 0) {
                    registeredGroups.remove(this);
                } else {
                    this.setLeader((String)this.getPlayers().get(0));
                }
            }

            return true;
        }
    }

    public String getLeader() {
        return this.leader == null ? "Groupe Independant" : this.leader;
    }

    public boolean setLeader(String name) {
        if (!this.getPlayers().contains(name)) {
            Logger.info(new String[]{"Can't set lead to " + name + " because he's not in the group"});
            return false;
        } else {
            this.leader = name;
            Iterator var2 = this.getOnlinePlayers().iterator();

            while(var2.hasNext()) {
                Player pla = (Player)var2.next();
                pla.sendMessage(getPrefix() + ChatColor.GRAY + "Le nouveau leader de votre groupe est maintenant " + ChatColor.BLUE + name);
            }

            return true;
        }
    }

    public List<String> getPlayers() {
        return new ArrayList(this.players);
    }

    public List<Player> getOnlinePlayers() {
        List<Player> onlinePlayers = new ArrayList();
        Iterator var2 = this.getPlayers().iterator();

        while(var2.hasNext()) {
            String s = (String)var2.next();
            Player p = Bukkit.getPlayer(s);
            if (p != null && p.isOnline()) {
                onlinePlayers.add(p);
            }
        }

        return onlinePlayers;
    }

    public static Group getPlayerGroup(String name) {
        Iterator var1 = registeredGroups.iterator();

        Group g;
        do {
            if (!var1.hasNext()) {
                return null;
            }

            g = (Group)var1.next();
        } while(!g.getPlayers().contains(name));

        return g;
    }

    static {
        prefix = ChatColor.GRAY + "[" + ChatColor.BLUE + "Groupe" + ChatColor.GRAY + "] " + ChatColor.RESET;
    }
}
