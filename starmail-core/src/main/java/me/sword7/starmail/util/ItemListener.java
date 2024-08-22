package me.sword7.starmail.util;

import me.sword7.starmail.StarMail;
import me.sword7.starmail.box.Box;
import me.sword7.starmail.box.BoxCache;
import me.sword7.starmail.box.PlacedBox;
import me.sword7.starmail.letter.Letter;
import me.sword7.starmail.pack.Pack;
import me.sword7.starmail.postbox.Postbox;
import me.sword7.starmail.postbox.PostboxCache;
import me.sword7.starmail.sys.Version;
import me.sword7.starmail.util.X.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ItemListener implements Listener {

    private static boolean hasLetter = Version.current.hasLetter();
    private static boolean canLectern = XMaterial.LECTERN.isSupported();
    private static Material lectern = XMaterial.LECTERN.parseMaterial();

    public ItemListener() {
        Plugin plugin = StarMail.getPlugin();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (!canLectern) return;
        if (Letter.isLetter(e.getItem()) && e.hasBlock() && e.getClickedBlock().getType() == lectern) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onEditBook(PlayerEditBookEvent e) {
        if (!hasLetter) return;
        if (Letter.isLetter(e.getPreviousBookMeta()) && e.isSigning()) {
            e.setNewBookMeta(Letter.sign(e.getNewBookMeta()));
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        if (Pack.isSealedPack(e.getItemInHand())) {
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBreak(BlockBreakEvent e) {
        BlockState state = e.getBlock().getState();

        Pack pack = Pack.getPack(state);
        if (pack != null) {
            cancelDrop(e);
            dropItem(e.getBlock(), pack.getEmptyPack());
            return;
        }

        Box box = Box.getBox(state);
        if (box != null) {
            cancelDrop(e);
            Location location = e.getBlock().getLocation();
            PlacedBox placedBox = BoxCache.getPlacedBox(location);
            boolean global = placedBox != null ? placedBox.isGlobal() : false;
            ItemStack boxStack = global ? box.getGlobalItemStack() : box.getItemStack();
            dropItem(e.getBlock(), boxStack);
            BoxCache.unRegister(e.getBlock().getLocation());
            return;
        }

        Postbox postbox = Postbox.getPostbox(state);
        if (postbox != null) {
            cancelDrop(e);
            dropItem(e.getBlock(), postbox.getItemStack());
            PostboxCache.unRegister(e.getBlock().getLocation());
        }
    }

    private void dropItem(Block block, ItemStack itemStack) {
        block.getWorld().dropItemNaturally(block.getLocation(), itemStack);
    }

    private void cancelDrop(BlockBreakEvent e) {
        if (Version.current.value >= 112) {
            e.setDropItems(false);
        } else {
            e.setCancelled(true);
            e.getBlock().setType(Material.AIR);
        }
    }

    private Set<Location> pushedBlockLocations = new HashSet<>();

    @EventHandler
    public void onFromTo(BlockFromToEvent e) {
        BlockState state = e.getToBlock().getState();

        Pack pack = Pack.getPack(state);
        Box box = Box.getBox(state);
        Postbox postbox = Postbox.getPostbox(state);
        if (pack != null || box != null || postbox != null) {
            Location location = e.getToBlock().getLocation();
            Location location2 = e.getBlock().getLocation();
            pushedBlockLocations.add(location);
            pushedBlockLocations.add(location2);
            Bukkit.getScheduler().runTaskLater(StarMail.getPlugin(), () -> {
                pushedBlockLocations.remove(location);
                pushedBlockLocations.remove(location2);
            }, 2);
        }

    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPiston(BlockPistonExtendEvent e) {
        for (Block block : e.getBlocks()) {
            BlockState state = block.getState();
            Pack pack = Pack.getPack(state);
            Box box = Box.getBox(state);
            Postbox postbox = Postbox.getPostbox(state);
            if (pack != null || box != null || postbox != null) {
                Location location = block.getLocation();
                pushedBlockLocations.add(location);
                Bukkit.getScheduler().runTaskLater(StarMail.getPlugin(), () -> {
                    pushedBlockLocations.remove(location);
                }, 2);
            }
        }
    }

    @EventHandler
    public void onSpawn(ItemSpawnEvent e) {
        Location location = e.getLocation().getBlock().getLocation();
        if (pushedBlockLocations.contains(location)) {
            ItemStack itemStack = e.getEntity().getItemStack();
            Pack pack = Pack.getPack(itemStack);
            if (pack != null) {
                e.getEntity().setItemStack(pack.getEmptyPack());
                return;
            }
            Box box = Box.getBox(itemStack);
            if (box != null) {
                PlacedBox placedBox = BoxCache.getPlacedBox(location);
                boolean global = placedBox != null ? placedBox.isGlobal() : false;
                ItemStack boxStack = global ? box.getGlobalItemStack() : box.getItemStack();
                BoxCache.unRegister(e.getLocation().getBlock().getLocation());
                e.getEntity().setItemStack(boxStack);
                return;
            }
            Postbox postbox = Postbox.getPostbox(itemStack);
            if (postbox != null) {
                PostboxCache.unRegister(e.getLocation().getBlock().getLocation());
                e.getEntity().setItemStack(postbox.getItemStack());
                return;
            }
        }
    }


    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockExplode(BlockExplodeEvent e) {
        onExplode(e.blockList());
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEntityExplode(EntityExplodeEvent e) {
        onExplode(e.blockList());
    }

    private void onExplode(List<Block> blocks) {
        for (int i = blocks.size() - 1; i >= 0; i--) {
            Block block = blocks.get(i);
            BlockState state = block.getState();

            Pack pack = Pack.getPack(state);
            if (pack != null) {
                dropItem(block, pack.getEmptyPack());
                blocks.remove(block);
                block.setType(Material.AIR);
                return;
            }

            Box box = Box.getBox(state);
            if (box != null) {
                Location location = block.getLocation();
                PlacedBox placedBox = BoxCache.getPlacedBox(location);
                boolean global = placedBox != null ? placedBox.isGlobal() : false;
                ItemStack boxStack = global ? box.getGlobalItemStack() : box.getItemStack();
                dropItem(block, boxStack);
                BoxCache.unRegister(location);
                blocks.remove(block);
                block.setType(Material.AIR);
                return;
            }

            Postbox postbox = Postbox.getPostbox(state);
            if (postbox != null) {
                dropItem(block, postbox.getItemStack());
                PostboxCache.unRegister(block.getLocation());
                blocks.remove(block);
                block.setType(Material.AIR);
                return;
            }
        }
    }

}
