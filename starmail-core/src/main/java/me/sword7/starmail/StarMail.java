package me.sword7.starmail;

import me.sword7.starmail.blacklist.CommandBlacklist;
import me.sword7.starmail.box.*;
import me.sword7.starmail.gui.InputListener;
import me.sword7.starmail.gui.LiveSessions;
import me.sword7.starmail.gui.page.PageType;
import me.sword7.starmail.letter.Letter;
import me.sword7.starmail.letter.LetterLoot;
import me.sword7.starmail.pack.Pack;
import me.sword7.starmail.pack.PackLoot;
import me.sword7.starmail.pack.PackType;
import me.sword7.starmail.pack.tracking.TrackingCache;
import me.sword7.starmail.pack.tracking.TrackingRunnable;
import me.sword7.starmail.post.CommandMail;
import me.sword7.starmail.post.CommandSendTo;
import me.sword7.starmail.post.PostCache;
import me.sword7.starmail.postbox.Postbox;
import me.sword7.starmail.postbox.PostboxCache;
import me.sword7.starmail.postbox.PostboxLoot;
import me.sword7.starmail.sys.Language;
import me.sword7.starmail.sys.PluginBase;
import me.sword7.starmail.sys.Version;
import me.sword7.starmail.sys.config.*;
import me.sword7.starmail.user.UserCache;
import me.sword7.starmail.util.AutoCompleteListener;
import me.sword7.starmail.util.CommandLoot;
import me.sword7.starmail.util.Crafting;
import me.sword7.starmail.util.ItemListener;
import me.sword7.starmail.warehouse.CommandWarehouse;
import me.sword7.starmail.warehouse.WarehouseCache;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class StarMail extends JavaPlugin {

    private static StarMail plugin;
    private static final StarMailAPI starMailAPI = StarMailAPI.getInstance();

    @Override
    public void onEnable() {
        plugin = this;

        ConfigLoader.load();
        new PluginConfig();
        new IntegrationConfig();
        new ItemsConfig();
        new BlacklistConfig();
        Language.load();

        Box.init();
        Postbox.init();
        PackType.init();
        Pack.init();
        Letter.init();
        PageType.init();

        new PluginBase();

        new BoxCache();
        new PostboxCache();
        new UserCache();
        new PostCache();
        new WarehouseCache();
 
        TrackingRunnable.start();

        new Crafting();

        Objects.requireNonNull(getCommand("boxes")).setExecutor(new CommandBoxes());
        Objects.requireNonNull(getCommand("breakboxes")).setExecutor(new CommandBreakBoxes());
        Objects.requireNonNull(getCommand("mail")).setExecutor(new CommandMail());
        Objects.requireNonNull(getCommand("sendto")).setExecutor(new CommandSendTo());
        Objects.requireNonNull(getCommand("pack")).setExecutor(new CommandLoot(new PackLoot()));
        Objects.requireNonNull(getCommand("box")).setExecutor(new CommandLoot(new BoxLoot()));
        Objects.requireNonNull(getCommand("postbox")).setExecutor(new CommandLoot(new PostboxLoot()));
        Objects.requireNonNull(getCommand("globalbox")).setExecutor(new CommandLoot(new GlobalLoot()));
        Objects.requireNonNull(getCommand("starmail")).setExecutor(new CommandStarMail());
        Objects.requireNonNull(getCommand("warehouse")).setExecutor(new CommandWarehouse());
        Objects.requireNonNull(getCommand("blacklist")).setExecutor(new CommandBlacklist());
        if (Version.current.hasLetter())
            Objects.requireNonNull(getCommand("letter")).setExecutor(new CommandLoot(new LetterLoot()));

        new InputListener();
        new ItemListener();
        new MailListener();
        if (Version.current.isAutoCompleteSupported()) new AutoCompleteListener();

    }

    @Override
    public void onDisable() {

        LiveSessions.shutdown();

        TrackingRunnable.shutdown();
        TrackingCache.shutdown();

        WarehouseCache.shutdown();
        PostCache.shutdown();
        UserCache.shutdown();
        PostboxCache.shutdown();
        BoxCache.shutdown();

        PluginBase.shutdown();
    }

    public static StarMail getPlugin() {
        return plugin;
    }

    @SuppressWarnings("unused")
    public static StarMailAPI getStarMailAPI() {
        return starMailAPI;
    }

}
