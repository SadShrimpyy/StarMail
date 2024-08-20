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
        if (state instanceof Skull) {
            final Box box = Box.getBox(state);
            if (box != null) {
                if (!Box.isGlobal(e.getItemInHand())) {
                    final Player player = e.getPlayer();
                    if (PluginConfig.isAllowedMailboxWorld(blockLoc.getWorld())) {
                        UUID playerID = player.getUniqueId();
                        User user = UserCache.getCachedUser(playerID);
                        if (user != null) {
                            if (user.getPlacedBoxes() < user.getMaxBoxes(player)) {
                                BoxCache.registerBox(blockLoc, user, box);
                            } else {
                                e.setCancelled(true);
                                player.sendMessage(ChatColor.RED + WARN_BOX_LIMIT.toString());
                            }
                        } else {
                            e.setCancelled(true);
                        }
                    } else {
                        e.setCancelled(true);
                        player.sendMessage(ChatColor.RED + WARN_BLACKLIST_WORLD.toString());
                    }
                } else {
                    BoxCache.registerBox(blockLoc, box);
                }
            }
            Postbox postbox = Postbox.getPostbox(state);
            if (postbox != null) {
                PostboxCache.register(block.getLocation(), postbox);
            }
        }
    }

    private boolean oneHanded = Version.current.hasOneHand();
    private Sound batWings = XSound.ENTITY_BAT_TAKEOFF.parseSound();

    @EventHandler
    public void onClick(PlayerInteractEvent e) {
        final Player player = e.getPlayer();
        Block block = e.getClickedBlock();
        if (isRightClick(e.getAction())) {
            SneakResult sneakResult = getSneakResult(player, e.getItem());
            if (sneakResult == SneakResult.NORMAL_CLICK) {
                Location location = block != null ? block.getLocation() : null;
                if (BoxCache.hasBox(location)) {
                    if (canOpenGui) onMailbox(e, player, block, location);
                } else if (PostboxCache.hasPostbox(location)) {
                    if (canOpenGui) onPostbox(e, player, Postbox.getPostbox(block.getState()));
                } else if (Pack.isPack(e.getItem())) {
                    onPackage(e);
                }
            } else if (sneakResult == SneakResult.USE_PACK) {
                onPackage(e);
            }
        }
    }


    private void fixVisuals(PlayerInteractEvent e, Player player, ItemStack itemStack) {
        if (!noVisualFixPlayers.contains(player.getUniqueId())) {
            if (Version.current.value <= 110 && itemStack != null && itemStack.getType().isBlock()) {
                if (oneHanded || e.getHand() == EquipmentSlot.HAND) {
                    Scheduler.runLater(() -> {
                        player.getInventory().setItem(player.getInventory().getHeldItemSlot(), itemStack);
                    }, 1);
                } else {
                    Scheduler.runLater(() -> {
                        player.getInventory().setItemInOffHand(itemStack);
                    }, 1);
                }
            }
        }
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
        if (oneHanded || e.getHand() == EquipmentSlot.HAND) {
            ItemStack itemStack = e.getItem();
            Pack pack = Pack.getPack(itemStack);
            if (pack != null) {
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
        } else {
            e.setCancelled(true);
        }

    }

    private void onPostbox(PlayerInteractEvent e, Player player, Postbox postbox) {
        e.setCancelled(true);
        fixVisuals(e, player, e.getItem());
        if (oneHanded || e.getHand() == EquipmentSlot.HAND) {
            if (Permissions.canPostboxBlock(player)) {
                if (Version.current.value > 110) {
                    LiveSessions.launchPostbox(player, postbox);
                } else {
                    Scheduler.runLater(() -> {
                        LiveSessions.launchPostbox(player, postbox);
                    }, 2);
                }
            } else {
                player.sendMessage(ChatColor.RED + WARN_NOT_PERMITTED_BLOCK.toString());
            }
        }
    }

    private void onMailbox(PlayerInteractEvent e, final Player player, Block block, Location location) {
        e.setCancelled(true);
        if (oneHanded || e.getHand() == EquipmentSlot.HAND) {
            Box box = Box.getBox(block.getState());
            PlacedBox placedBox = BoxCache.getPlacedBox(e.getClickedBlock().getLocation());
            UUID ownerID = placedBox.getOwnerId();
            if (placedBox.isGlobal()) {
                if (Permissions.canGlobalboxBlock(player)) {
                    launchMailbox(player, box, location);
                } else {
                    player.sendMessage(ChatColor.RED + WARN_NOT_PERMITTED_BLOCK.toString());
                }
            } else if (player.getUniqueId().equals(ownerID)) {
                if (Permissions.canMailboxBlock(player)) {
                    launchMailbox(player, box, location);
                } else {
                    player.sendMessage(ChatColor.RED + WARN_NOT_PERMITTED_BLOCK.toString());
                }
            } else {
                if (Permissions.canMailboxBlock(player)) {
                    UserCache.getUser(ownerID, (User boxOwner) -> {
                        if (ownerID != null) {
                            User mailSender = UserCache.getCachedUser(player.getUniqueId());
                            if (player.isOnline() && mailSender != null) {
                                if (PluginConfig.isInstantSend()) {
                                    PlayerInventory inventory = player.getInventory();
                                    ItemStack itemStack = Version.current.hasOffhand() ? inventory.getItemInMainHand() : inventory.getItemInHand();
                                    if (itemStack != null && itemStack.getType() != Material.AIR) {
                                        if (Mail.isMail(player, itemStack)) {
                                            if (!PostCache.isCooling(player)) {
                                                sendInstant(player, mailSender, boxOwner, itemStack.clone());
                                            } else {
                                                player.sendMessage(ChatColor.RED + WARN_COOLING.fromSeconds(PostCache.getCooldownLeft(player)));
                                            }
                                        } else {
                                            player.sendMessage(ChatColor.RED + WARN_NOT_MAIL.toString());
                                        }
                                    } else {
                                        player.sendMessage(ChatColor.YELLOW + INFO_BOX.fromPlayer(boxOwner.getName()));
                                    }
                                } else {
                                    LiveSessions.launchBox(player, boxOwner, box, location);
                                }
                            }
                        }
                    });

                } else {
                    player.sendMessage(ChatColor.RED + WARN_NOT_PERMITTED_BLOCK.toString());
                }
            }

        }

        fixVisuals(e, player, e.getItem());

    }

    private Set<UUID> noVisualFixPlayers = new HashSet<>();

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
            Scheduler.runLater(() -> {
                LiveSessions.launchMail(player, box, location);
            }, 2);
        }
    }


    public SneakResult getSneakResult(Player player, ItemStack itemStack) {
        if (player.isSneaking()) {
            if (Pack.isPack(itemStack)) {
                return SneakResult.USE_PACK;
            } else if (itemStack != null) {
                return SneakResult.USE_ITEM;
            } else {
                return SneakResult.NORMAL_CLICK;
            }
        } else {
            return SneakResult.NORMAL_CLICK;
        }

    }


    private enum SneakResult {
        USE_ITEM,
        USE_PACK,
        NORMAL_CLICK,
    }

}