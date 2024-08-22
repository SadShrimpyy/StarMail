package com.sadshiry;

import com.sadshiry.particle.IParticle;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class Particle_Modern implements IParticle {
    @Override
    public void playCloud(Player player) {
        Vector dir = player.getLocation().getDirection();
        player.getLocation().getWorld().spawnParticle(org.bukkit.Particle.CLOUD, player.getLocation().add(dir.getX(), 1.5, dir.getY()), 16, 0.2, 0.2, 0.2, 0.2f);
    }
}
