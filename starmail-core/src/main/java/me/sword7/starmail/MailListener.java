package me.sword7.starmail;

import me.sword7.starmail.box.Box;
import me.sword7.starmail.box.BoxCache;
import me.sword7.starmail.box.PlacedBox;
import me.sword7.starmail.gui.LiveSessions;
import me.sword7.starmail.pack.Pack;
import me.sword7.starmail.post.Mail;
import me.sword7.starmail.post.PostCache;
import me.sword7.starmail.postbox.Postbox;
import me.sword7.starmail.postbox.PostboxCache;
import me.sword7.starmail.sys.Permissions;
import me.sword7.starmail.sys.Version;
import me.sword7.starmail.sys.config.PluginConfig;
import me.sword7.starmail.user.User;
import me.sword7.starmail.user.UserCache;
import me.sword7.starmail.util.MailUtil;
import me.sword7.starmail.util.Scheduler;
import me.sword7.starmail.util.X.XSound;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Skull;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import static me.sword7.starmail.sys.Language.*;

public class MailListener implements Listener {

    private static final ItemStack AIR = new ItemStack(Material.AIR);
    private boolean canOpenGui = false;

    public MailListener() {
        Plugin plugin = StarMail.getPlugin();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);

        Scheduler.runLater(() -> {
            canOpenGui = true;
        }, 5);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlace(BlockPlaceEvent e) {
        Block block = e.getBlock();
        BlockState state = block.getState();
        final Location blockLoc = block.getLocation();
        if (!(state instanceof Skull)) return;

        final Box box = Box.getBox(state);
        if (box == null) {
            registerPostbox(state, block);
            return;
        }

        if (Box.isGlobal(e.getItemInHand())) {
            BoxCache.registerBox(blockLoc, box);
            registerPostbox(state, block);
            return;
        }

        final Player player = e.getPlayer();
        if (!(PluginConfig.isAllowedMailboxWorld(Objects.requireNonNull(blockLoc.getWorld())))) {
            e.setCancelled(true);
            player.sendMessage(ChatColor.RED + WARN_BLACKLIST_WORLD.toString());
            registerPostbox(state, block);
            return;
        }

        UUID playerID = player.getUniqueId();
        User user = UserCache.getCachedUser(playerID);
        if (user == null) {
            e.setCancelled(true);
            registerPostbox(state, block);
            return;
        }

        if (user.getPlacedBoxes() < user.getMaxBoxes(player)) {
            BoxCache.registerBox(blockLoc, user, box);
        } else {
            e.setCancelled(true);
            player.sendMessage(ChatColor.RED + WARN_BOX_LIMIT.toString());
        }

        registerPostbox(state, block);
    }

    private static void registerPostbox(BlockState state, Block block) {
        Postbox postbox = Postbox.getPostbox(state);
        if (postbox != null)
            PostboxCache.register(block.getLocation(), postbox);
    }

    private boolean oneHanded = Version.current.hasOneHand();
    private Sound batWings = XSound.ENTITY_BAT_TAKEOFF.parseSound();

    @EventHandler
    public void onClick(PlayerInteractEvent e) {
        final Player player = e.getPlayer();
        Block block = e.getClickedBlock();
        if (!(isRightClick(e.getAction()))) return;
        SneakResult sneakResult = getSneakResult(player, e.getItem());
        if (sneakResult == SneakResult.NORMAL_CLICK) {
            Location location = block != null ? block.getLocation() : null;
            if (canOpenGui) {
                if (BoxCache.hasBox(location)) {
                    onMailbox(e, player, block, location);
                } else if (PostboxCache.hasPostbox(location)) {
                    onPostbox(e, player, Postbox.getPostbox(block.getState()));
                }
            } else if (Pack.isPack(e.getItem())) {
                onPackage(e);
            }
        } else if (sneakResult == SneakResult.USE_PACK) {
            onPackage(e);
        }
    }


    private void fixVisuals(PlayerInteractEvent e, Player player, ItemStack itemStack) {
        if (noVisualFixPlayers.contains(player.getUniqueId())) return;
        if (!isaBlock(itemStack)) return;

        if (oneHanded || e.getHand() == EquipmentSlot.HAND) {
            Scheduler.runLater(() -> player.getInventory().setItem(player.getInventory().getHeldItemSlot(), itemStack), 1);
        } else {
            Scheduler.runLater(() -> player.getInventory().setItemInOffHand(itemStack), 1);
        }
    }

    private static boolean isaBlock(ItemStack itemStack) {
        return (Version.current.value <= 110)
                && (itemStack != null)
                && (itemStack.getType().isBlock());
    }

    private boolean isInteracting(Player player, Block block) {
        return block != null && MailUtil.isInteractable(block.getType()) && !player.isSneaking();
    }

    private boolean isRightClick(Action action) {
        return action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK;
    }

    private void onPackage(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if (isInteracting(player, e.getClickedBlock())) return;
        if (!(oneHanded || e.getHand() == EquipmentSlot.HAND)) {
            e.setCancelled(true);
            return;
        }

        ItemStack itemStack = e.getItem();
        Pack pack = Pack.getPack(itemStack);
        if (pack == null) return;

        int slot = player.getInventory().getHeldItemSlot();
        ItemMeta meta = itemStack.getItemMeta();
        if (Pack.isSealedPack(meta)) {
            UUID trackingNo = Pack.getTrackingNo(meta);
            if (trackingNo != null) {
                LiveSessions.launchSealedPackage(player, pack, trackingNo, slot);
                e.setCancelled(true);
            }
        } else if (Pack.isEmptyPack(meta) && !player.isSneaking()) {
            LiveSessions.launchEmptyPackage(player, pack, slot, e.getItem());
            e.setCancelled(true);
        }

    }

    private void onPostbox(PlayerInteractEvent e, Player player, Postbox postbox) {
        e.setCancelled(true);
        fixVisuals(e, player, e.getItem());
        if (!(oneHanded || e.getHand() == EquipmentSlot.HAND)) return;

        if (!(Permissions.canPostboxBlock(player))) {
            player.sendMessage(ChatColor.RED + WARN_NOT_PERMITTED_BLOCK.toString());
            return;
        }

        if (Version.current.value > 110) {
            LiveSessions.launchPostbox(player, postbox);
        } else {
            Scheduler.runLater(() -> LiveSessions.launchPostbox(player, postbox), 2);
        }

    }

    private void onMailbox(PlayerInteractEvent e, final Player player, Block block, Location location) {
        e.setCancelled(true);
        if (!(oneHanded || e.getHand() == EquipmentSlot.HAND)) {
            fixVisuals(e, player, e.getItem());
            return;
        }

        Box box = Box.getBox(block.getState());
        PlacedBox placedBox = BoxCache.getPlacedBox(
                Objects.requireNonNull(e.getClickedBlock()).getLocation()
        );
        UUID ownerID = placedBox.getOwnerId();
        if (placedBox.isGlobal()) {
            checkAndLaunch(Permissions.canGlobalboxBlock(player), player, box, location);
        } else if (player.getUniqueId().equals(ownerID)) {
            checkAndLaunch(Permissions.canMailboxBlock(player), player, box, location);
        } else {
            checkAndSendInstant(player, location, ownerID, box);
        }

        fixVisuals(e, player, e.getItem());
    }

    @Deprecated
    private void checkAndSendInstant(Player player, Location location, UUID ownerID, Box box) {
        if (!(Permissions.canMailboxBlock(player)))
            player.sendMessage(ChatColor.RED + WARN_NOT_PERMITTED_BLOCK.toString());

        UserCache.getUser(ownerID, (User boxOwner) -> {
            if (ownerID == null) return;
            User mailSender = UserCache.getCachedUser(player.getUniqueId());
            if (!(player.isOnline() && mailSender != null)) return;

            if (!(PluginConfig.isInstantSend())) {
                LiveSessions.launchBox(player, boxOwner, box, location);
                return;
            }

            PlayerInventory inventory = player.getInventory();
            ItemStack itemStack = Version.current.hasOffhand()
                    ? inventory.getItemInMainHand()
                    : inventory.getItemInHand();
            if (itemStack == null) return;

            if (itemStack.getType() == Material.AIR) {
                player.sendMessage(ChatColor.YELLOW + INFO_BOX.fromPlayer(boxOwner.getName()));
                return;
            }

            if (!(Mail.isMail(player, itemStack))) {
                player.sendMessage(ChatColor.RED + WARN_NOT_MAIL.toString());
                return;
            }

            if (!PostCache.isCooling(player)) {
                sendInstant(player, mailSender, boxOwner, itemStack.clone());
            } else {
                player.sendMessage(ChatColor.RED + WARN_COOLING.fromSeconds(PostCache.getCooldownLeft(player)));
            }
        });
    }

    private void checkAndLaunch(boolean player, Player player1, Box box, Location location) {
        if (player) {
            launchMailbox(player1, box, location);
        } else {
            player1.sendMessage(ChatColor.RED + WARN_NOT_PERMITTED_BLOCK.toString());
        }
    }

    private final Set<UUID> noVisualFixPlayers = new HashSet<>();

    private void sendInstant(Player player, User mailSender, User boxOwner, ItemStack mailItem) {
        player.sendMessage(ChatColor.YELLOW + SUCCESS_SENT.fromPlayer(boxOwner.getName()));
        player.playSound(player.getLocation(), batWings, 1f, 0.7f);
        int coolDuration = mailSender.getSendCooldown(player);
        PostCache.send(mailSender, boxOwner.getID(), mailItem, coolDuration);
        noVisualFixPlayers.add(player.getUniqueId());
        player.getInventory().setItem(player.getInventory().getHeldItemSlot(), AIR);
        noVisualFixPlayers.remove(player.getUniqueId());
    }


    private void launchMailbox(Player player, Box box, Location location) {
        if (Version.current.value > 110) {
            LiveSessions.launchMail(player, box, location);
        } else {
            Scheduler.runLater(() -> LiveSessions.launchMail(player, box, location), 2);
        }
    }


    public SneakResult getSneakResult(Player player, ItemStack itemStack) {
        if (!player.isSneaking()) return SneakResult.NORMAL_CLICK;
        if (Pack.isPack(itemStack)) return SneakResult.USE_PACK;
        else if (itemStack != null) return SneakResult.USE_ITEM;
        else return SneakResult.NORMAL_CLICK;
    }


    private enum SneakResult {
        USE_ITEM,
        USE_PACK,
        NORMAL_CLICK,
    }

}