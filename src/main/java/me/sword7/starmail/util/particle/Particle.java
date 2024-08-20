package me.sword7.starmail.util.particle;

import me.sword7.starmail.sys.Version;
import org.bukkit.entity.Player;

public class Particle {

    private static IParticle particle = selectParticle();

    private static IParticle selectParticle() {
        Version current = Version.current;
        if (current.value >= 109) {
            return new Particle_V1_9();
        } else {
            return new Particle_V1_8();
        }
    }

    public static void playCloud(Player player) {
        particle.playCloud(player);
    }

}
