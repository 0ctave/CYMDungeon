//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package fr.dungeons;

import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

public class PluginMessage implements PluginMessageListener {
    public PluginMessage() {
    }

    public void onPluginMessageReceived(String arg0, Player arg1, byte[] arg2) {
        System.out.println(arg0);
        System.out.println(arg1.getName());
        System.out.println(arg2);
    }
}
