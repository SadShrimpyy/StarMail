package com.sadshiry;

import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.Objects;

public class Particle_V1_71_1 implements IParticle {
    @Override
    public void playCloud(Player player) {
        Vector dir = player.getLocation().getDirection();
        Objects.requireNonNull(player.getLocation().getWorld()).spawnParticle(Particle.CLOUD, player.getLocation().add(dir.getX(), 1.5, dir.getY()), 16, 0.2, 0.2, 0.2, 0.2f);
    }
}
