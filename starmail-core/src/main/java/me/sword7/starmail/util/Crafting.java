package me.sword7.starmail.util;

import me.sword7.starmail.StarMail;
import me.sword7.starmail.box.Box;
import me.sword7.starmail.box.BoxType;
import me.sword7.starmail.letter.Letter;
import me.sword7.starmail.letter.LetterType;
import me.sword7.starmail.pack.*;
import me.sword7.starmail.sys.Permissions;
import me.sword7.starmail.sys.Version;
import me.sword7.starmail.util.X.XDye;
import me.sword7.starmail.util.X.XMaterial;
import me.sword7.starmail.util.X.XPlanks;
import me.sword7.starmail.util.X.XWool;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.material.MaterialData;
import org.bukkit.plugin.Plugin;

import java.util.Collection;

public class Crafting implements Listener {

    private ItemStack AIR = new ItemStack(Material.AIR);
    private Version version = Version.current;
    private boolean letterSupported = version.hasLetter();

    public Crafting() {
        Plugin plugin = StarMail.getPlugin();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        registerRecipes();
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onBookCopy(PrepareItemCraftEvent e) {
        ItemStack result = e.getInventory().getResult();
        if (result != null) {
            if (Letter.isLetter(result)) {
                if (containsBook(e.getInventory().getMatrix())) e.getInventory().setResult(AIR);
            } else if (Letter.bookMats.contains(result.getType())) {
                if (containsLetter(e.getInventory().getMatrix())) e.getInventory().setResult(AIR);
            }
        }

    }


    @EventHandler(ignoreCancelled = true)
    public void onCraftPrep(PrepareItemCraftEvent e) {
        if (e.getRecipe() != null && e.getRecipe().getResult() != null) {
            ItemStack result = e.getRecipe().getResult();

            //gift and boxes have same recipe(player head + dye), so I have to convert the result for gifts
            ItemStack conversion = convertGiftResult(e.getInventory(), result);
            if (conversion != null) {
                result = conversion;
                e.getInventory().setResult(result);
            }

            Collection<HumanEntity> viewers = e.getViewers();
            if (letterSupported && Letter.isLetter(result)) {
                if (!Permissions.canCraftLetter(viewers)) {
                    e.getInventory().setResult(AIR);
                } else {
                    if (containsBook(e.getInventory().getMatrix())) {
                        e.getInventory().setResult(AIR);
                    }
                }
            } else if (Pack.isPack(result)) {
                Pack pack = Pack.getPack(result);
                if (pack instanceof Crate) {
                    if (!Permissions.canCraftCrate(viewers)) e.getInventory().setResult(AIR);
                } else if (pack instanceof Chest) {
                    if (!Permissions.canCraftChest(viewers)) e.getInventory().setResult(AIR);
                } else if (pack instanceof Gift) {
                    if (!Permissions.canCraftGift(viewers)) {
                        e.getInventory().setResult(AIR);
                    } else {
                        if (Pack.getPack(result.getItemMeta()).getType() != PackType.ACACIA_CHEST.DEFAULT_GIFT) {
                            if (!containsGift(e.getInventory().getMatrix())) {
                                e.getInventory().setResult(AIR);
                            }
                        }
                    }
                } else {
                    e.getInventory().setResult(AIR);
                }
            } else if (Box.isBox(result)) {
                if (!Permissions.canCraftBox(viewers)) {
                    e.getInventory().setResult(AIR);
                } else {
                    if (Box.getBox(result.getItemMeta()).getType() != BoxType.DEFAULT) {
                        if (!containsBox(e.getInventory().getMatrix())) {
                            e.getInventory().setResult(AIR);
                        }
                    }
                }
            }
        }
    }

    private ItemStack convertGiftResult(CraftingInventory inventory, ItemStack result) {
        Box box = Box.getBox(result);
        if (box != null) {
            if (containsGift(inventory.getMatrix())) {
                XDye xDye = box.getXDye();
                for (PackType packType : PackType.craftableGifts) {
                    if (((Gift) packType.getPack()).getXDye() == xDye) {
                        return packType.getPack().getEmptyPack();
                    }
                }
            }
        }
        return null;
    }

    public boolean containsLetter(ItemStack[] matrix) {
        for (ItemStack stack : matrix) {
            if (Letter.isLetter(stack)) {
                return true;
            }
        }
        return false;
    }

    public boolean containsBook(ItemStack[] matrix) {
        for (ItemStack stack : matrix) {
            if (stack != null && Letter.bookMats.contains(stack.getType()) && !Letter.isLetter(stack)) {
                return true;
            }
        }
        return false;
    }

    public boolean containsBox(ItemStack[] matrix) {
        for (ItemStack stack : matrix) {
            if (Box.isBox(stack)) {
                return true;
            }
        }
        return false;
    }

    public boolean containsGift(ItemStack[] matrix) {
        for (ItemStack stack : matrix) {
            if (Pack.getPack(stack) instanceof Gift) {
                return true;
            }
        }
        return false;
    }

    public void registerRecipes() {
        Plugin plugin = StarMail.getPlugin();

        ItemStack defaultBoxItemStack = BoxType.DEFAULT.getBox().getItemStack();
        ShapedRecipe defaultBox = version.hasNamespaceKey() ? new ShapedRecipe(new NamespacedKey(plugin, "DEFAULT_BOX"), defaultBoxItemStack) : new ShapedRecipe(defaultBoxItemStack);
        defaultBox.shape("---", "-ct", "---");
        defaultBox.setIngredient('-', Material.IRON_INGOT);
        defaultBox.setIngredient('c', Material.CHEST);
        defaultBox.setIngredient('t', Material.IRON_TRAPDOOR);
        plugin.getServer().addRecipe(defaultBox);

        for (BoxType boxType : BoxType.craftable) {
            Box box = boxType.getBox();
            ShapelessRecipe dyedBox = version.hasNamespaceKey() ? new ShapelessRecipe(new NamespacedKey(plugin, box.getName() + "_BOX"), box.getItemStack()) : new ShapelessRecipe(box.getItemStack());
            XDye xDye = box.getXDye();
            if (version.hasExtendedEnums()) {
                dyedBox.addIngredient(xDye.parseMaterial());
                dyedBox.addIngredient(XMaterial.PLAYER_HEAD.parseMaterial());
            } else {
                dyedBox.addIngredient(new MaterialData(xDye.parseMaterial(), xDye.getByte()));
                dyedBox.addIngredient(new MaterialData(XMaterial.PLAYER_HEAD.parseMaterial(), (byte) 3));
            }
            plugin.getServer().addRecipe(dyedBox);
        }

        if (version.hasLetter()) {
            ShapelessRecipe letterRecipe = new ShapelessRecipe(new NamespacedKey(plugin, "Letter"), LetterType.DEFAULT.getLetter().getLetterAndQuill());
            letterRecipe.addIngredient(Material.PAPER);
            letterRecipe.addIngredient(Material.INK_SAC);
            letterRecipe.addIngredient(Material.FEATHER);
            plugin.getServer().addRecipe(letterRecipe);


            for (LetterType letterType : LetterType.craftable) {
                Letter letter = letterType.getLetter();
                ShapelessRecipe dyedBox = version.hasNamespaceKey() ? new ShapelessRecipe(new NamespacedKey(plugin, letter.getName() + "_LETTER"), letter.getLetterAndQuill()) : new ShapelessRecipe(letter.getLetterAndQuill());
                XDye xDye = letter.getXDye();
                dyedBox.addIngredient(xDye.parseMaterial());
                dyedBox.addIngredient(XMaterial.WRITABLE_BOOK.parseMaterial());
                plugin.getServer().addRecipe(dyedBox);
            }
        }

        for (PackType packType : PackType.craftableCrates) {
            Crate crate = (Crate) packType.getPack();
            XPlanks planks = crate.getXPlanks();
            if (planks.isSupported()) {
                for (XWool wool : XWool.values()) {
                    ShapedRecipe packRecipe = version.hasNamespaceKey() ? new ShapedRecipe(new NamespacedKey(plugin, crate.getName() + "_CRATE_" + wool.toString()), crate.getEmptyPack()) : new ShapedRecipe(crate.getEmptyPack());
                    packRecipe.shape("www", "w*w", "www");
                    if (version.hasExtendedEnums()) {
                        packRecipe.setIngredient('w', planks.parseMaterial());
                        packRecipe.setIngredient('*', wool.parseMaterial());
                    } else {
                        packRecipe.setIngredient('w', new MaterialData(XMaterial.OAK_PLANKS.parseMaterial(), planks.getByte()));
                        packRecipe.setIngredient('*', new MaterialData(wool.parseMaterial(), wool.getByte()));
                    }
                    plugin.getServer().addRecipe(packRecipe);
                }
            }
        }


        for (PackType packType : PackType.craftableChests) {
            Chest chest = (Chest) packType.getPack();
            XPlanks planks = chest.getXPlanks();
            if (planks.isSupported()) {
                for (XWool wool : XWool.values()) {
                    ShapedRecipe packRecipe = version.hasNamespaceKey() ? new ShapedRecipe(new NamespacedKey(plugin, chest.getName() + "_CHEST_" + wool.toString()), chest.getEmptyPack()) : new ShapedRecipe(chest.getEmptyPack());
                    packRecipe.shape("---", "w*w", "www");
                    if (version.hasExtendedEnums()) {
                        packRecipe.setIngredient('-', planks.parseSlabMaterial());
                        packRecipe.setIngredient('w', planks.parseMaterial());
                        packRecipe.setIngredient('*', wool.parseMaterial());
                    } else {
                        packRecipe.setIngredient('-', new MaterialData(planks.parseSlabMaterial(), planks.getByte()));
                        packRecipe.setIngredient('w', new MaterialData(XMaterial.OAK_PLANKS.parseMaterial(), planks.getByte()));
                        packRecipe.setIngredient('*', new MaterialData(wool.parseMaterial(), wool.getByte()));
                    }
                    plugin.getServer().addRecipe(packRecipe);
                }
            }
        }

        final ItemStack defaultGiftItemStack = PackType.DEFAULT_GIFT.getPack().getEmptyPack();

        for (XWool wool : XWool.values()) {
            ShapedRecipe defaultGift = version.hasNamespaceKey() ? new ShapedRecipe(new NamespacedKey(plugin, "DEFAULT_GIFT_" + wool.toString()), defaultGiftItemStack) : new ShapedRecipe(defaultGiftItemStack);
            defaultGift.shape("---", "-*-", "---");
            defaultGift.setIngredient('-', XMaterial.PAPER.parseMaterial());
            if (version.hasExtendedEnums()) {
                defaultGift.setIngredient('*', wool.parseMaterial());
            } else {
                defaultGift.setIngredient('*', new MaterialData(wool.parseMaterial(), wool.getByte()));
            }
            plugin.getServer().addRecipe(defaultGift);
        }


    }


}
