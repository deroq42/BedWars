package de.deroq.bedwars.events;

import de.deroq.bedwars.game.models.GamePlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * @author deroq
 * @since 06.07.2022
 */

public class BedWarsDropOutEvent extends Event {

    public static final HandlerList HANDLERS = new HandlerList();
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
        return HANDLERS;
    }
}
