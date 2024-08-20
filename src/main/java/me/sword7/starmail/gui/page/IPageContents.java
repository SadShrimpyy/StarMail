package me.sword7.starmail.gui.page;

import me.sword7.starmail.gui.data.SessionData;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;

public interface IPageContents {

    Inventory populate(Inventory menu, SessionData sessionData);

    void processClick(Player player, Inventory menu, SessionData sessionData, int slot, ClickType clickType);

}
