package me.sword7.starmail.util.particle;

import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class Particle_V1_9 implements IParticle {
    @Override
    public void playCloud(Player player) {
        Vector dir = player.getLocation().getDirection();
        player.getLocation().getWorld().spawnParticle(Particle.CLOUD, player.getLocation().add(dir.getX(), 1.5, dir.getY()), 16, 0.2, 0.2, 0.2, 0.2f);
    }
}
