package me.sword7.starmail.box;

import me.shiry_recode.starmail.commands.ICommandSyntax;
import me.sword7.starmail.sys.Version;
import me.sword7.starmail.util.ILootType;
import me.sword7.starmail.util.InfoList;
import me.sword7.starmail.util.LocationParts;
import me.sword7.starmail.util.MailUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static me.sword7.starmail.sys.Language.LABEL_BOX_LOCATIONS;
import static me.sword7.starmail.sys.Language.LABEL_WORLD;

public class CommandBoxes implements ICommandSyntax {

    private String[] args;

    public CommandBoxes(String[] args) {
        this.args = args;
    }

    @Override
    public String getName() {
        return "boxes";
    }

    @Override
    public String getPermission(String[] args) {
        return "";
    }

    @Override
    public boolean hasSubcommands() {
        return false;
    }

    @Override
    public int expectedArgs() {
        return 0;
    }

    @Override
    public int possibleErrors() {
        return 0;
    }

    @Override
    public void applyLoot(ILootType lootType) {

    }

    public void perform(CommandSender sender) {

        if (sender instanceof Player) {

            Player player = (Player) sender;
            Map<LocationParts, PlacedBox> placedBoxes = BoxCache.getRelevantBoxes(player.getUniqueId());
            List<String> infos = new ArrayList<>();
            for (Map.Entry<LocationParts, PlacedBox> entry : placedBoxes.entrySet()) {
                Box box = entry.getValue().getBox();
                LocationParts locParts = entry.getKey();
                String symbol = Version.current.value >= 114 ? box.getSymbol() : box.getColor().toString() + ChatColor.BOLD + "[]";
                infos.add(symbol + ChatColor.GRAY + ChatColor.ITALIC + " (" + locParts.getWorldName() + ", " + locParts.getBlockX() + ", " + locParts.getBlockY() + ", " + locParts.getBlockZ() + ")");
            }
            placedBoxes.clear();
            InfoList infoList = new InfoList(ChatColor.YELLOW + LABEL_BOX_LOCATIONS.toString() + " (" + LABEL_WORLD.toString().toLowerCase() + ", x, y, z):", infos, 5);
            int page = args.length > 0 ? MailUtil.parseAmount(args[0]) : 1;
            infoList.displayTo(player, page);

        }
    }

}
