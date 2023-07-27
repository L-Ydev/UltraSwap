package eq.larry.dev.hand;

import java.beans.ConstructorProperties;
import org.bukkit.Location;

public class MapLocation {
    private String map;

    private Location location;

    public String getMap() {
        return this.map;
    }

    public Location getLocation() {
        return this.location;
    }

    public void setMap(String map) {
        this.map = map;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof MapLocation))
            return false;
        MapLocation other = (MapLocation)o;
        if (!other.canEqual(this))
            return false;
        Object this$map = getMap(), other$map = other.getMap();
        if ((this$map == null) ? (other$map != null) : !this$map.equals(other$map))
            return false;
        Object this$location = getLocation(), other$location = other.getLocation();
        return !((this$location == null) ? (other$location != null) : !this$location.equals(other$location));
    }

    protected boolean canEqual(Object other) {
        return other instanceof MapLocation;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $map = getMap();
        result = result * 59 + (($map == null) ? 0 : $map.hashCode());
        Object $location = getLocation();
        return result * 59 + (($location == null) ? 0 : $location.hashCode());
    }

    public String toString() {
        return "MapLocation(map=" + getMap() + ", location=" + getLocation() + ")";
    }

    @ConstructorProperties({"map", "location"})
    public MapLocation(String map, Location location) {
        this.map = map;
        this.location = location;
    }
}
