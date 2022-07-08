package de.deroq.bedwars.events;

import de.deroq.bedwars.game.models.GamePlayer;
import de.deroq.bedwars.npc.NPC;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class BedWarsShopInteractEvent extends Event {

    public static final HandlerList HANDLERS = new HandlerList();
    private final GamePlayer gamePlayer;
    private final NPC npc;

    public BedWarsShopInteractEvent(GamePlayer gamePlayer, NPC npc) {
        this.gamePlayer = gamePlayer;
        this.npc = npc;
    }

    public GamePlayer getGamePlayer() {
        return gamePlayer;
    }

    public NPC getNpc() {
        return npc;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }
}
