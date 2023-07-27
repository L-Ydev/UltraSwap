package eq.larry.dev.event.weather;

import eq.larry.dev.UltraCore;
import eq.larry.dev.UltraCoreListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.weather.ThunderChangeEvent;

public class ThunderChange extends UltraCoreListener
{
    public ThunderChange(UltraCore plugin) {
        super(plugin);
    }

    @EventHandler
    public void onThunderChange(ThunderChangeEvent event) {
        if (event.toThunderState())
            event.setCancelled(true);
    }
}
