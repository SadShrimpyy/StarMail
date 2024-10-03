package me.sword7.starmail.compatibility.versions;

import com.sadshiry.Particle_V1_71_1;
import com.sadshiry.Particle_V1_8_8;
import com.sadshiry.IParticle;
import me.sword7.starmail.sys.Version;
import org.bukkit.entity.Player;

public class PlayParticle {

    private static final IParticle particle;
    static {
        particle = Version.current.value >= 109
            ? new Particle_V1_71_1()
            : new Particle_V1_8_8();
    }

    public static void playCloud(Player player) {
        particle.playCloud(player);
    }

}
