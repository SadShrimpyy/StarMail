package me.sword7.starmail.util;

import org.bukkit.inventory.ItemStack;

import java.util.List;

public interface ILootType {

    ItemStack getLootFrom(String s);

    String getListLabel();

    List<String> getLootTypes();

    String getRoot();

    String getType();

}
