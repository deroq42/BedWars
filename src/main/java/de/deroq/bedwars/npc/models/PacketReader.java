package de.deroq.bedwars.npc.models;

import de.deroq.bedwars.BedWars;
import de.deroq.bedwars.events.BedWarsShopInteractEvent;
import de.deroq.bedwars.game.models.GamePlayer;
import de.deroq.bedwars.npc.NPC;
import de.deroq.bedwars.npc.utils.Reflections;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayInUseEntity;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;

import java.util.List;
import java.util.Optional;

public class PacketReader extends Reflections {

    private final BedWars bedWars;
    private final GamePlayer gamePlayer;
    private Channel channel;

    public PacketReader(BedWars bedWars, GamePlayer gamePlayer) {
        this.bedWars = bedWars;
        this.gamePlayer = gamePlayer;
    }

    public void inject() {
        CraftPlayer craftPlayer = (CraftPlayer) gamePlayer.getPlayer();
        this.channel = craftPlayer.getHandle().playerConnection.networkManager.channel;
        channel.pipeline().addAfter("decoder", "PacketInjector", new MessageToMessageDecoder<Packet<?>>() {
            @Override
            protected void decode(ChannelHandlerContext arg0, Packet<?> packet, List<Object> arg2) {
                arg2.add(packet);
                readPacket(packet);
            }
        });
    }

    public void eject() {
        if (channel.pipeline().get("PacketInjector") != null) {
            channel.pipeline().remove("PacketInjector");
        }
    }


    public void readPacket(Packet<?> packet) {
        if (!(packet instanceof PacketPlayInUseEntity)) {
            return;
        }

        int id = (int) getValue(packet, "a");
        if (!getValue(packet, "action").toString().equalsIgnoreCase("INTERACT")) {
            return;
        }

        /* Task to avoid a NPE. */
        Bukkit.getScheduler().runTask(bedWars, () -> {
            Optional<NPC> optionalNPC = bedWars.getGameManager().getCurrentGameMap().getShops()
                    .stream()
                    .filter(npc -> npc.getEntityPlayer().getId() == id)
                    .findFirst();

            if (!optionalNPC.isPresent()) {
                return;
            }

            NPC npc = optionalNPC.get();
            Bukkit.getPluginManager().callEvent(new BedWarsShopInteractEvent(gamePlayer, npc));
        });
    }

    public GamePlayer getGamePlayer() {
        return gamePlayer;
    }

    public Channel getChannel() {
        return channel;
    }
}
