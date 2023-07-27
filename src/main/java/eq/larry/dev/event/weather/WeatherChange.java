package eq.larry.dev.event.weather;

import eq.larry.dev.UltraCore;
import eq.larry.dev.UltraCoreListener;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.weather.WeatherChangeEvent;

public class WeatherChange extends UltraCoreListener {
    public WeatherChange(UltraCore plugin) {
        super(plugin);
    }

    @EventHandler
    public void onWeatherChange(WeatherChangeEvent event) {
        World world = event.getWorld();
        if (!world.isThundering() && !world.hasStorm())
            event.setCancelled(true);
    }
}

