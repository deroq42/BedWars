package de.deroq.bedwars.npc.misc;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import de.deroq.bedwars.npc.NPC;
import org.bukkit.Location;

import java.util.UUID;

public class NPCBuilder {

    private final NPC npc;

    public NPCBuilder() {
        this.npc = new NPC();
    }

    public NPCBuilder setUuid(UUID uuid) {
        npc.setUuid(uuid);
        return this;
    }

    public NPCBuilder setName(String name) {
        npc.setName(name);
        npc.setGameProfile(new GameProfile(UUID.randomUUID(), name));
        return this;
    }

    public NPCBuilder setSkin(String value, String signature) {
        npc.getGameProfile().getProperties().put("textures", new Property("textures", value, signature));
        return this;
    }

    public NPCBuilder setLocation(Location location) {
        npc.setLocation(location);
        return this;
    }

    public NPC build() {
        return npc;
    }
}
