package me.sword7.starmail.util;

import com.sadshiry.Particle_Legacy;
import com.sadshiry.Particle_Modern;
import com.sadshiry.particle.IParticle;
import me.sword7.starmail.sys.Version;
import org.bukkit.entity.Player;

public class Particle {

    private static IParticle particle = selectParticle();

    private static IParticle selectParticle() {
        if (Version.current.value >= 109) {
            return new Particle_Modern();
        } else {
            return new Particle_Legacy();
        }
    }

    public static void playCloud(Player player) {
        particle.playCloud(player);
    }

}
