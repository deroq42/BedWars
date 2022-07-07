package de.deroq.bedwars.npc.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;

public class Reflections {

    public void sendPacket(Object packet) {
        for (Player players : Bukkit.getOnlinePlayers()) {
            try {
                Object handle = players.getClass().getMethod("getHandle").invoke(players);
                Object playerConnection = handle.getClass().getField("playerConnection").get(handle);
                playerConnection.getClass().getMethod("sendPacket", getNMSClass("Packet")).invoke(playerConnection, packet);
            } catch (NoSuchFieldException | InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
                Bukkit.getLogger().warning("Error while sending packet: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public Class<?> getNMSClass(String name) {
        try {
            return Class.forName("net.minecraft.server." + getServerVersion() + "." + name);
        } catch (ClassNotFoundException e) {
            Bukkit.getLogger().warning("Error while getting NMS class " + name + ":" + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    public Class<?> getCraftBukkitClass(String name) {
        try {
            return Class.forName("org.bukkit.craftbukkit." + getServerVersion() + "." + name);
        } catch (ClassNotFoundException e) {
            Bukkit.getLogger().warning("Error while getting CraftBukkit class " + name + ":" + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    public String getServerVersion() {
        return Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
    }
}
