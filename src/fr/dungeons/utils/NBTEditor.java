//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package fr.dungeons.utils;

import net.minecraft.server.v1_15_R1.EntityLiving;
import net.minecraft.server.v1_15_R1.NBTTagCompound;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_15_R1.inventory.CraftItemStack;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

public class NBTEditor {
    public NBTEditor() {
    }

    public static ItemStack setString(ItemStack is, String key, String value) {
        net.minecraft.server.v1_15_R1.ItemStack stack = CraftItemStack.asNMSCopy(is);
        NBTTagCompound tag = stack.getTag();
        if (tag == null) {
            tag = new NBTTagCompound();
        }

        tag.setString(key, value);
        stack.setTag(tag);
        is = CraftItemStack.asBukkitCopy(stack);
        return is;
    }

    public static ItemStack setInt(ItemStack is, String key, int value) {
        net.minecraft.server.v1_15_R1.ItemStack stack = CraftItemStack.asNMSCopy(is);
        NBTTagCompound tag = stack.getTag();
        if (tag == null) {
            tag = new NBTTagCompound();
        }

        tag.setInt(key, value);
        stack.setTag(tag);
        is = CraftItemStack.asBukkitCopy(stack);
        return is;
    }

    public static ItemStack setFloat(ItemStack is, String key, float value) {
        net.minecraft.server.v1_15_R1.ItemStack stack = CraftItemStack.asNMSCopy(is);
        NBTTagCompound tag = stack.getTag();
        if (tag == null) {
            tag = new NBTTagCompound();
        }

        tag.setFloat(key, value);
        stack.setTag(tag);
        is = CraftItemStack.asBukkitCopy(stack);
        return is;
    }

    public static ItemStack setBoolean(ItemStack is, String key, boolean value) {
        net.minecraft.server.v1_15_R1.ItemStack stack = CraftItemStack.asNMSCopy(is);
        NBTTagCompound tag = stack.getTag();
        if (tag == null) {
            tag = new NBTTagCompound();
        }

        tag.setBoolean(key, value);
        stack.setTag(tag);
        is = CraftItemStack.asBukkitCopy(stack);
        return is;
    }

    public static ItemStack setDouble(ItemStack is, String key, double value) {
        net.minecraft.server.v1_15_R1.ItemStack stack = CraftItemStack.asNMSCopy(is);
        NBTTagCompound tag = stack.getTag();
        if (tag == null) {
            tag = new NBTTagCompound();
        }

        tag.setDouble(key, value);
        stack.setTag(tag);
        is = CraftItemStack.asBukkitCopy(stack);
        return is;
    }

    public static ItemStack removeNBT(ItemStack is, String value) {
        net.minecraft.server.v1_15_R1.ItemStack stack = CraftItemStack.asNMSCopy(is);
        NBTTagCompound tag = stack.getTag();
        if (tag == null) {
            return is;
        } else if (!tag.hasKey(value)) {
            return is;
        } else {
            tag.remove(value);
            stack.setTag(tag);
            is = CraftItemStack.asBukkitCopy(stack);
            return is;
        }
    }

    public static void setInt(LivingEntity e, String key, int value) {
        EntityLiving ent = ((CraftLivingEntity)e).getHandle();
        NBTTagCompound nbt = new NBTTagCompound();
        ent.c(nbt);
        nbt.setInt(key, value);
        ent.a(nbt);
    }

    public static void setString(LivingEntity e, String key, String value) {
        EntityLiving ent = ((CraftLivingEntity)e).getHandle();
        NBTTagCompound nbt = new NBTTagCompound();
        ent.c(nbt);
        nbt.setString(key, value);
        ent.a(nbt);
    }

    public static int getInt(ItemStack is, String key) {
        net.minecraft.server.v1_15_R1.ItemStack stack = CraftItemStack.asNMSCopy(is);
        NBTTagCompound tag = stack.getTag();
        return tag == null ? -1 : tag.getInt(key);
    }

    public static double getDouble(ItemStack is, String key) {
        net.minecraft.server.v1_15_R1.ItemStack stack = CraftItemStack.asNMSCopy(is);
        NBTTagCompound tag = stack.getTag();
        return tag == null ? -1.0D : tag.getDouble(key);
    }

    public static String getString(ItemStack is, String key) {
        net.minecraft.server.v1_15_R1.ItemStack stack = CraftItemStack.asNMSCopy(is);
        NBTTagCompound tag = stack.getTag();
        return tag == null ? null : tag.getString(key);
    }

    public static boolean getBoolean(ItemStack is, String key) {
        net.minecraft.server.v1_15_R1.ItemStack stack = CraftItemStack.asNMSCopy(is);
        NBTTagCompound tag = stack.getTag();
        return tag == null ? false : tag.getBoolean(key);
    }

    public static float getFloat(ItemStack is, String key) {
        net.minecraft.server.v1_15_R1.ItemStack stack = CraftItemStack.asNMSCopy(is);
        NBTTagCompound tag = stack.getTag();
        return tag == null ? -1.0F : tag.getFloat(key);
    }
}
