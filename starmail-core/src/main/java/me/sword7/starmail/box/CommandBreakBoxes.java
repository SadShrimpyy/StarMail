package me.sword7.starmail.box;

import me.sword7.starmail.util.LocationParts;
import me.sword7.starmail.util.X.XSound;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

import static me.sword7.starmail.sys.Language.SUCCESS_BREAK;

public class CommandBreakBoxes implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player) {

            Player player = (Player) sender;
            List<LocationParts> locationList = BoxCache.getBoxLocations(player.getUniqueId());

            for (LocationParts locationParts : locationList) {
                BoxCache.unRegister(locationParts);
                Location location = locationParts.toLocation();
                if (location != null) {
                    dropBox(location);
                } else {
                    BoxCache.requestBreak(locationParts);
                }
            }
            locationList.clear();

            player.sendMessage(ChatColor.YELLOW + SUCCESS_BREAK.toString());

        }

        return false;
    }


    public static void dropBox(Location location) {
        Block block = location.getBlock();
        BlockState state = block.getState();
        Box box = Box.getBox(state);
        if (box != null) {
            location.getWorld().dropItemNaturally(location, box.getItemStack());
            location.getBlock().setType(Material.AIR);
            location.getWorld().playSound(location, XSound.ENTITY_ITEM_PICKUP.parseSound(), 1f, 0.8f);
        }
    }

}
