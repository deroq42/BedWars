package de.deroq.bedwars.listeners.misc;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;

/**
 * @author deroq
 * @since 07.07.2022
 */

public class WeatherChangeListener implements Listener {

    @EventHandler
    public void onWeatherChange(WeatherChangeEvent event) {
        event.getWorld().setTime(0);
        event.setCancelled(true);
    }
}
