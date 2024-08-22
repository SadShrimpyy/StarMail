package me.sword7.starmail.pack;

import me.sword7.starmail.gui.page.PageType;
import me.sword7.starmail.sys.Language;
import me.sword7.starmail.sys.config.PluginConfig;
import me.sword7.starmail.util.Head;
import me.sword7.starmail.util.MailWood;
import me.sword7.starmail.util.X.XGlass;
import me.sword7.starmail.util.X.XPlanks;
import me.sword7.starmail.util.X.XSound;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class Crate extends Pack {

    private static Sound openSound = XSound.BLOCK_BARREL_OPEN.isSupported() ? XSound.BLOCK_BARREL_OPEN.parseSound() : XSound.BLOCK_CHEST_OPEN.parseSound();
    private static Sound closeSound = XSound.BLOCK_BARREL_CLOSE.isSupported() ? XSound.BLOCK_BARREL_CLOSE.parseSound() : XSound.BLOCK_CHEST_CLOSE.parseSound();

    private XPlanks xPlanks;
    private XGlass borderWood;
    private XGlass wood;
    private UUID profileIDSeal;
    private String dataSeal;
    private boolean doStraps = false;
    private ItemStack strapStack;

    public Crate(PackType type, MailWood mailWood, String profileID, String data) {
        super(type, type.toString(), Language.LABEL_WOODEN_CRATE.fromWood(mailWood.getLanguage()), profileID, data);
        this.xPlanks = mailWood.getXPlanks();
        this.wood = mailWood.getXGlass();
        this.borderWood = mailWood.getXGlass();
    }

    public Crate(PackType type, String name, String displayName, XGlass wood, XGlass borderWood, String profileID, String data) {
        super(type, name, displayName, profileID, data);
        this.wood = wood;
        this.borderWood = borderWood;
    }

    public Crate(PackType type, MailWood mailWood, String profileID, String data, String profileIDSeal, String dataSeal) {
        super(type, type.toString(), Language.LABEL_WOODEN_CRATE.fromWood(mailWood.getLanguage()), profileID, data);
        this.xPlanks = mailWood.getXPlanks();
        this.wood = mailWood.getXGlass();
        this.borderWood = mailWood.getXGlass();
        this.profileIDSeal = UUID.fromString(profileIDSeal);
        this.dataSeal = dataSeal;
        doStraps = PluginConfig.isShowCrateStraps();
        strapStack = Head.createHeadItem(dataSeal, this.profileIDSeal, displayName);
    }

    public Crate(PackType type, String name, String displayName, XGlass wood, XGlass borderWood, String profileID, String data, String profileIDSeal, String dataSeal) {
        super(type, name, displayName, profileID, data);
        this.wood = wood;
        this.borderWood = borderWood;
        this.profileIDSeal = UUID.fromString(profileIDSeal);
        this.dataSeal = dataSeal;
        doStraps = PluginConfig.isShowCrateStraps();
        strapStack = Head.createHeadItem(dataSeal, this.profileIDSeal, displayName);
    }

    @Override
    public XGlass getBorder() {
        return borderWood;
    }

    @Override
    public void playOpenSound(Player player) {
        if (XSound.BLOCK_BARREL_OPEN.isSupported()) {
            player.playSound(player.getLocation(), openSound, 1.5f, 1.2f);
        } else {
            player.playSound(player.getLocation(), openSound, 0.5f, 1.2f);
        }
    }

    @Override
    public void playCloseSound(Player player) {
        if (XSound.BLOCK_BARREL_CLOSE.isSupported()) {
            player.playSound(player.getLocation(), closeSound, 1.5f, 1.2f);
        } else {
            player.playSound(player.getLocation(), closeSound, 0.5f, 1.2f);
        }
    }

    @Override
    public PageType getOpener() {
        return PageType.CRATE_SEAL;
    }

    @Override
    public boolean isSupported() {
        return xPlanks == null || xPlanks.isSupported();
    }

    @Override
    public ItemStack getSealBaseStack() {
        return new ItemStack(Material.STRING);
    }

    private static Sound unseal = XSound.ENTITY_SHEEP_SHEAR.parseSound();

    @Override
    public void playSealSound(Player player) {
        //do nothing
    }

    @Override
    public void playUnsealSound(Player player) {
        player.playSound(player.getLocation(), unseal, 2f, 0.7f);
    }

    public XPlanks getXPlanks() {
        return xPlanks;
    }

    public XGlass getWood() {
        return wood;
    }

    public XGlass getBorderWood() {
        return borderWood;
    }

    public void addStraps(ItemStack head) {
        if (doStraps) Head.assignTexture(head, dataSeal, displayName, profileIDSeal);
    }

    public void removeStraps(ItemStack head) {
        if (doStraps) Head.assignTexture(head, data, displayName, profileID);
    }

    public boolean isDoStraps() {
        return doStraps;
    }

    public UUID getProfileIDSeal() {
        return profileIDSeal;
    }

    public String getDataSeal() {
        return dataSeal;
    }

    public ItemStack getStrapStack() {
        return strapStack;
    }
}
