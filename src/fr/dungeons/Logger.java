//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package fr.dungeons;

public class Logger {
    public Logger() {
    }

    public static void info(String... msg) {
        String[] var1 = msg;
        int var2 = msg.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            String s = var1[var3];
            Dungeons.getPlugin().getLogger().info(s);
        }

    }

    public static void error(String... msg) {
        String[] var1 = msg;
        int var2 = msg.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            String s = var1[var3];
            Dungeons.getPlugin().getLogger().severe(s);
        }

    }
}
