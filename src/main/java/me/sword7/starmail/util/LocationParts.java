package me.sword7.starmail.util;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class LocationParts {

    private Location location;
    private final String worldName;
    private final int blockX;
    private final int blockY;
    private final int blockZ;

    public LocationParts(Location location) {
        if (location != null) {
            this.worldName = location.getWorld().getName();
            this.blockX = location.getBlockX();
            this.blockY = location.getBlockY();
            this.blockZ = location.getBlockZ();
        } else {
            this.worldName = "";
            this.blockX = 0;
            this.blockY = 0;
            this.blockZ = 0;
        }
    }

    public LocationParts(String worldName, int blockX, int blockY, int blockZ) {
        this.worldName = worldName;
        this.blockX = blockX;
        this.blockY = blockY;
        this.blockZ = blockZ;
    }

    public String getWorldName() {
        return worldName;
    }

    public double getBlockX() {
        return blockX;
    }

    public double getBlockY() {
        return blockY;
    }

    public double getBlockZ() {
        return blockZ;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Location) {
            Location l = (Location) o;
            return this.worldName.equals(l.getWorld().getName()) && this.blockX == l.getBlockX() &&
                    this.blockY == l.getBlockY() && this.blockZ == l.getBlockZ();
        } else if (o instanceof LocationParts) {
            LocationParts l = (LocationParts) o;
            return this.worldName.equals(l.worldName) && this.blockX == l.blockX &&
                    this.blockY == l.blockY && this.blockZ == l.blockZ;
        } else {
            return super.equals(o);
        }
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 31)
                .append(worldName)
                .append(blockX)
                .append(blockY)
                .append(blockZ)
                .toHashCode();
    }

    public LocationParts clone() {
        return new LocationParts(worldName, blockX, blockY, blockZ);
    }

    public Location toLocation() {
        if (location == null) {
            World world = Bukkit.getWorld(this.worldName);
            if (world != null) {
                Location location = new Location(world, blockX, blockY, blockZ, 0, 0);
                this.location = location;
                return location;
            } else {
                return null;
            }
        } else {
            return location;
        }
    }

    public void initialize(World world) {
        if (this.worldName.equals(world.getName())) {
            this.location = new Location(world, blockX, blockY, blockZ, 0, 0);
        }
    }

    public boolean isInitialized() {
        return location != null;
    }

    @Override
    public String toString() {
        return worldName + "_" + blockX + "_" + blockY + "_" + blockZ;
    }

}
