package me.sword7.starmail.util;

import me.sword7.starmail.gui.Icons;
import me.sword7.starmail.util.X.XMaterial;
import me.sword7.starmail.util.X.XSound;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class AnimationUtil {

    private static ItemStack arrow = Icons.createIcon(Material.ARROW, ChatColor.GREEN.toString() + ChatColor.BOLD + Symbol.RIGHT);
    private static ItemStack arrowHighlight = buildHighlightArrow();
    private static final Sound batWings = XSound.ENTITY_BAT_TAKEOFF.parseSound();

    private static ItemStack buildHighlightArrow() {
        Material material = XMaterial.SPECTRAL_ARROW.isSupported() ? XMaterial.SPECTRAL_ARROW.parseMaterial() : XMaterial.ARROW.parseMaterial();
        return Icons.createIcon(material, ChatColor.YELLOW.toString() + ChatColor.BOLD + Symbol.RIGHT);
    }

    public static void playWings(Player player) {
        player.playSound(player.getLocation(), AnimationUtil.batWings, 1f, 0.7f);
    }

    public static ItemStack getArrow() {
        return arrow.clone();
    }

    public static ItemStack getArrowHighlight() {
        return arrowHighlight.clone();
    }
}
