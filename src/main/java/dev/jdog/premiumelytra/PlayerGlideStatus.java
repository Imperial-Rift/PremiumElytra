package dev.jdog.premiumelytra;

import org.bukkit.Location;
import org.bukkit.event.entity.EntityToggleGlideEvent;

public class PlayerGlideStatus {
    private Location location;

    private Double startingMoney;

    public PlayerGlideStatus(Location location, Double startingMoney) {
        this.location = location;
        this.startingMoney = startingMoney;
    }


    public Double getStartingMoney() {
        return startingMoney;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

//
    public Location getLocation() {
        return location;
    }
}
