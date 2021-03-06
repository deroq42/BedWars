package de.deroq.bedwars.npc;

import com.mojang.authlib.GameProfile;

import com.mojang.authlib.properties.Property;
import de.deroq.bedwars.BedWars;
import de.deroq.bedwars.npc.utils.Reflections;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

/**
 * @author deroq
 * @since 06.07.2022
 */

public class NPC extends Reflections {

    private final UUID uuid;
    private final String name;
    private final String value;
    private final String signature;
    private final Location location;
    private final GameProfile gameProfile;
    private Object npc;
    private Object id;

    private NPC(UUID uuid, String name, String value, String signature, Location location) {
        this.uuid = uuid;
        this.name = name;
        this.value = value;
        this.signature = signature;
        this.location = location;
        this.gameProfile = new GameProfile(uuid, name);

        create();
    }

    private void create() {
        try {
            /* Invoking net.minecraft.server.v1_8_R3.MinecraftServer. */
            Object minecraftServer = getCraftBukkitClass("CraftServer").getMethod("getServer").invoke(Bukkit.getServer());

            /* Invoking net.minecraft.server.v1_8_R3.WorldServer. */
            Object worldServer = getCraftBukkitClass("CraftWorld").getMethod("getHandle").invoke(Bukkit.getWorld("world"));

            /* Gets the PlayerInteractManager class and creates a new instance of it with param WorldServer. */
            Class<?> playerInteractManagerClass = getNMSClass("PlayerInteractManager");
            Constructor<?> playerInteractManagerConstructor = playerInteractManagerClass.getDeclaredConstructors()[0];
            Object playerInteractManager = playerInteractManagerConstructor.newInstance(worldServer);

            gameProfile.getProperties().put("textures", new Property("textures", value, signature));

            /* Gets the EntityPlayer class and creates a new instance of it with params
               MinecraftServer, WorldServer, GameProfile, PlayerInteractManager. */
            Class<?> entityPlayerClass = getNMSClass("EntityPlayer");
            Constructor<?> entityPlayerConstructor = entityPlayerClass.getDeclaredConstructors()[0];
            this.npc = entityPlayerConstructor.newInstance(minecraftServer, worldServer, gameProfile, playerInteractManager);
            this.id = getNMSClass("EntityPlayer").getMethod("getId").invoke(npc);

            setLocation();
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException |
                 InstantiationException e) {
            Bukkit.getLogger().warning("Error while spawning NPC " + name + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void spawn() {
        try {
            /* Gets the PacketPlayOutNamedEntitySpawn class and creates a new instance of it with param EntityPlayer. */
            Class<?> packetPlayOutNamedEntitySpawnClass = getNMSClass("PacketPlayOutNamedEntitySpawn");
            Constructor<?> packetPlayOutNamedEntitySpawnConstructor = packetPlayOutNamedEntitySpawnClass.getConstructor(getNMSClass("EntityHuman"));
            Object packetPlayOutNamedEntitySpawn = packetPlayOutNamedEntitySpawnConstructor.newInstance(npc);

            addToTabList();
            sendPacket(packetPlayOutNamedEntitySpawn);
            fixHeadDirection(location.getYaw(), location.getPitch());
            Bukkit.getScheduler().runTaskLater(JavaPlugin.getPlugin(BedWars.class), this::removeFromTabList, 10);
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                 IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void setLocation() {
        try {
            /* Gets the class of the npc and invokes the setLocation method with params
               double, double, double, float, float. */
            Class<?> npcClass = npc.getClass();
            npcClass.getMethod("setLocation", double.class, double.class, double.class, float.class, float.class).invoke(npc, location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            Bukkit.getLogger().warning("Error while setting location of NPC " + name + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void addToTabList() {
        try {
            Object array = Array.newInstance(getNMSClass("EntityPlayer"), 1);
            Array.set(array, 0, npc);

            /* Gets the PacketPlayOutPlayerInfo class and the inner class EnumPlayerInfoAction to add the npc to the tablist */
            Class<?> packetPlayOutPlayerInfoClass = getNMSClass("PacketPlayOutPlayerInfo");
            Class<?> enumPlayerInfoActionClass = getNMSClass("PacketPlayOutPlayerInfo$EnumPlayerInfoAction");
            Object addPlayerEnum = enumPlayerInfoActionClass.getField("ADD_PLAYER").get(null);

            /* Creates a new instance of it with the params EnumPlayerInfoAction, EntityPlayer. */
            Constructor<?> packetPlayOutPlayerInfoConstructor = packetPlayOutPlayerInfoClass.getConstructor(enumPlayerInfoActionClass, Class.forName("[Lnet.minecraft.server." + getServerVersion() + ".EntityPlayer;"));
            Object packetPlayOutPlayerInfo = packetPlayOutPlayerInfoConstructor.newInstance(addPlayerEnum, array);

            sendPacket(packetPlayOutPlayerInfo);
        } catch (NoSuchFieldException | ClassNotFoundException | InvocationTargetException | IllegalAccessException |
                 NoSuchMethodException | InstantiationException e) {
            Bukkit.getLogger().warning("Error while adding NPC " + name + " to tablist: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void removeFromTabList() {
        try {
            Object array = Array.newInstance(getNMSClass("EntityPlayer"), 1);
            Array.set(array, 0, npc);

            /* Gets the PacketPlayOutPlayerInfo class and the inner class EnumPlayerInfoAction to add the npc to the tablist */
            Class<?> packetPlayOutPlayerInfoClass = getNMSClass("PacketPlayOutPlayerInfo");
            Class<?> enumPlayerInfoActionClass = getNMSClass("PacketPlayOutPlayerInfo$EnumPlayerInfoAction");
            Object addPlayerEnum = enumPlayerInfoActionClass.getField("REMOVE_PLAYER").get(null);

            /* Creates a new instance of it with the params EnumPlayerInfoAction, EntityPlayer. */
            Constructor<?> packetPlayOutPlayerInfoConstructor = packetPlayOutPlayerInfoClass.getConstructor(enumPlayerInfoActionClass, Class.forName("[Lnet.minecraft.server." + getServerVersion() + ".EntityPlayer;"));
            Object packetPlayOutPlayerInfo = packetPlayOutPlayerInfoConstructor.newInstance(addPlayerEnum, array);

            sendPacket(packetPlayOutPlayerInfo);
        } catch (NoSuchFieldException | ClassNotFoundException | InvocationTargetException | IllegalAccessException |
                 NoSuchMethodException | InstantiationException e) {
            Bukkit.getLogger().warning("Error while removing NPC " + name + " from tablist: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void fixHeadDirection(float yaw, float pitch) {
        try {
            /* Gets inner class PacketPlayOutEntityLook and creates a new instance of it with params int, byte, byte, boolean. */
            Class<?> packetPlayOutEntityLookClass = getNMSClass("PacketPlayOutEntity$PacketPlayOutEntityLook");
            Constructor<?> packetPlayOutEntityLookConstructor = packetPlayOutEntityLookClass.getConstructor(int.class, byte.class, byte.class, boolean.class);

            Object packetPlayOutEntityLook = packetPlayOutEntityLookConstructor.newInstance((int) id, (byte) ((int) (yaw * 256.0F / 360.0F)), (byte) ((int) (pitch * 256.0F / 360.0F)), true);
            sendPacket(packetPlayOutEntityLook);

            /* Gets the PacketPlayOutEntityHeadRotation class and creates a new instance of it with params EntityPlayer, byte. */
            Class<?> packetPlayOutEntityHeadRotationClass = getNMSClass("PacketPlayOutEntityHeadRotation");
            Constructor<?> packetPlayOutEntityHeadRotationConstructor = packetPlayOutEntityHeadRotationClass.getConstructor(getNMSClass("Entity"), byte.class);
            Object packetPlayOutEntityHeadRotation = packetPlayOutEntityHeadRotationConstructor.newInstance(npc, (byte) ((int) (yaw * 256.0F / 360.0F)));

            sendPacket(packetPlayOutEntityHeadRotation);
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                 IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public Location getLocation() {
        return location;
    }

    public String getValue() {
        return value;
    }

    public String getSignature() {
        return signature;
    }

    public GameProfile getGameProfile() {
        return gameProfile;
    }

    public int getId() {
        return (int) id;
    }

    public static class builder {

        private UUID uuid;
        private String name;
        private String value;
        private String signature;
        private Location location;

        public builder setUuid(UUID uuid) {
            this.uuid = uuid;
            return this;
        }

        public builder setName(String name) {
            this.name = name;
            return this;
        }

        public builder setValue(String value) {
            this.value = value;
            return this;
        }

        public builder setSignature(String signature) {
            this.signature = signature;
            return this;
        }

        public builder setLocation(Location location) {
            this.location = location;
            return this;
        }

        public NPC build() {
            return new NPC(uuid, name, value, signature, location);
        }
    }
}
