package com.sadshiry;

import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class Particle_V1_8_8 implements IParticle {

    public void playCloud(Player player) {
        Location loc = player.getLocation();
        Vector dir = player.getLocation().getDirection();
        PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(EnumParticle.CLOUD, true, (float) (loc.getX() + dir.getX()), (float) (loc.getY() + 1.5), (float) (loc.getZ() + dir.getZ()), 0.2f, 0.2f, 0.2f, 0.2f, 16);
        for (Player online : Bukkit.getOnlinePlayers()) {
            ((CraftPlayer) online).getHandle().playerConnection.sendPacket(packet);
        }
    }
}
