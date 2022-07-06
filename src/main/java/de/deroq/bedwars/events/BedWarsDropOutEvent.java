package de.deroq.bedwars.events;

import de.deroq.bedwars.game.models.GamePlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class BedWarsDropOutEvent extends Event {

    public static HandlerList HANDLERS = new HandlerList();
    private final GamePlayer gamePlayer;

    public BedWarsDropOutEvent(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }

    public GamePlayer getGamePlayer() {
        return gamePlayer;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public HandlerList getHandlers() {
        return null;
    }
}
