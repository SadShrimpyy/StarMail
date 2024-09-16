package me.sword7.starmail.post;

import me.sword7.starmail.StarMail;
import me.sword7.starmail.post.notifications.Alerts;
import me.sword7.starmail.sys.config.PluginConfig;
import me.sword7.starmail.user.User;
import me.sword7.starmail.util.Cooldown;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PostCache implements Listener {

    private static Map<UUID, Cooldown> playerToCooldown = new HashMap<>();
    private static Map<UUID, List<Mail>> playerToMail = new HashMap<>();
    private static Map<UUID, List<Mail>> unsavedMail = new HashMap<>();

    private static PostFlatFile postFlatFile = new PostFlatFile();

    public PostCache() {
        Plugin plugin = StarMail.getPlugin();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        load();
    }

    public void load() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            UUID playerID = player.getUniqueId();
            List<Mail> mailList = postFlatFile.fetch(playerID);
            if (mailList.size() > 0) playerToMail.put(playerID, mailList);
        }
    }

    public static void shutdown() {
        for (Map.Entry<UUID, List<Mail>> entry : unsavedMail.entrySet()) {
            postFlatFile.store(entry.getKey(), entry.getValue());
        }
        unsavedMail.clear();
        playerToMail.clear();
    }

    public static void save() {
        for (Map.Entry<UUID, List<Mail>> entry : unsavedMail.entrySet()) {
            postFlatFile.store(entry.getKey(), entry.getValue());
        }
        unsavedMail.clear();
    }

    public static boolean isCooling(Player player) {
        return playerToCooldown.containsKey(player.getUniqueId());
    }

    public static int getCooldownLeft(Player player) {
        return playerToCooldown.get(player.getUniqueId()).getCountdown();
    }

    public static boolean hasMail(UUID playerID) {
        return playerToMail.containsKey(playerID);
    }

    public static List<Mail> getMail(UUID playerID) {
        return playerToMail.get(playerID);
    }

    public static void removeMail(UUID playerID, Mail mail) {
        if (playerToMail.containsKey(playerID)) {
            playerToMail.get(playerID).remove(mail);
            unsavedMail.put(playerID, playerToMail.get(playerID));
            save();
        }
    }

    public static void send(User sender, UUID targetID, ItemStack itemStack, int coolDuration) {
        UUID senderID = sender.getID();
        if (coolDuration > 0) {
            Cooldown cooldown = new Cooldown(() -> {
                playerToCooldown.remove(senderID);
            }, coolDuration);
            playerToCooldown.put(senderID, cooldown);
        }
        send(targetID, new Mail(itemStack, sender.getName()));
    }

    public static void send(UUID playerID, Mail mail) {
        if (playerToMail.containsKey(playerID)) {
            List<Mail> mailList = playerToMail.get(playerID);
            mailList.add(0, mail);
            unsavedMail.put(playerID, mailList);
            save();
        } else {
            List<Mail> mailList = postFlatFile.fetch(playerID);
            mailList.add(0, mail);
            unsavedMail.put(playerID, mailList);
            save();
            if (Bukkit.getPlayer(playerID) != null) {
                playerToMail.put(playerID, mailList);
            }
        }

        Alerts.onReceive(playerID, mail.getFrom());

    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        UUID playerID = e.getPlayer().getUniqueId();
        List<Mail> mailList = postFlatFile.fetch(playerID);
        if (mailList.size() > 0) {
            playerToMail.put(playerID, mailList);
        }
        if (PluginConfig.isAutomaticPack()) {
            e.getPlayer().setResourcePack("https://github.com/user-attachments/files/16784216/StarMail.-.Universal.zip");
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        UUID playerID = e.getPlayer().getUniqueId();
        List<Mail> mail = getMail(playerID);
        playerToMail.remove(playerID);
        unsavedMail.put(playerID, mail);
        save();
    }


}
