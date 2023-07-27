package eq.larry.dev;


import java.beans.ConstructorProperties;
import org.bukkit.event.Listener;

public class UltraCoreListener implements Listener {
    protected UltraCore plugin;

    @ConstructorProperties({"plugin"})
    protected UltraCoreListener(UltraCore plugin) {
        this.plugin = plugin;
    }
}
