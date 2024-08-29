package me.sword7.starmail;

import me.shiry_recode.starmail.commands.CommandsManager;
import me.shiry_recode.starmail.commands.ICommandSyntax;
import me.shiry_recode.starmail.sadlibrary.SadLibrary;
import me.sword7.starmail.box.Box;
import me.sword7.starmail.box.BoxCache;
import me.sword7.starmail.box.CommandBoxes;
import me.sword7.starmail.gui.InputListener;
import me.sword7.starmail.gui.LiveSessions;
import me.sword7.starmail.gui.page.PageType;
import me.sword7.starmail.letter.Letter;
import me.sword7.starmail.pack.Pack;
import me.sword7.starmail.pack.PackType;
import me.sword7.starmail.pack.tracking.TrackingCache;
import me.sword7.starmail.pack.tracking.TrackingRunnable;
import me.sword7.starmail.post.PostCache;
import me.sword7.starmail.postbox.Postbox;
import me.sword7.starmail.postbox.PostboxCache;
import me.sword7.starmail.sys.Language;
import me.sword7.starmail.sys.PluginBase;
import me.sword7.starmail.sys.Version;
import me.sword7.starmail.sys.config.ConfigLoader;
import me.sword7.starmail.sys.config.IntegrationConfig;
import me.sword7.starmail.sys.config.ItemsConfig;
import me.sword7.starmail.sys.config.PluginConfig;
import me.sword7.starmail.user.UserCache;
import me.sword7.starmail.util.AutoCompleteListener;
import me.sword7.starmail.util.Crafting;
import me.sword7.starmail.util.ItemListener;
import me.sword7.starmail.warehouse.WarehouseCache;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.function.Function;

public final class StarMail extends JavaPlugin {

    private static StarMail plugin;
    private static StarMailAPI starMailAPI = StarMailAPI.getInstance();
    private HashMap<String, Function<String[], ICommandSyntax>> registeredCommands;
    private SadLibrary sadLibrary;
    private CommandsManager commandsManager;

    @Override
    public void onEnable() {
        plugin = this;
        sadLibrary = new SadLibrary();
        sadLibrary.initialize();

        commandsManager = new CommandsManager();

        initConfigsAndFeatures();
        setCommands();
        initListeners();
    }

    private void initListeners() {
        new InputListener();
        new ItemListener();
        new MailListener();
        if (Version.current.isAutoCompleteSupported()) new AutoCompleteListener();
    }

    private void initConfigsAndFeatures() {
        ConfigLoader.load();
        new PluginConfig();
        new IntegrationConfig();
        new ItemsConfig();
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

        new TrackingCache();
        TrackingRunnable.start();

        new LiveSessions();
        new Crafting();
    }

    private void setCommands() {
        registeredCommands = new HashMap<>();
//        if (Version.current.hasLetter()) commands.put("letter", CommandLoot::new); //.applyLoot(new LetterLoot());
//        commands.put("box", CommandLoot::new); //.applyLoot(new BoxLoot());
//        commands.put("pack", CommandLoot::new); //.applyLoot(new PackLoot());
//        commands.put("postbox", CommandLoot::new); //.applyLoot(new PostboxLoot());
//        commands.put("globalbox", CommandLoot::new); //.applyLoot(new GlobalLoot());
        registeredCommands.put("boxes", CommandBoxes::new);
//        commands.put("breakboxes", CommandBreakBoxes::new);
//        commands.put("mail", CommandMail::new);
//        commands.put("sendto", CommandSendTo::new);
//        commands.put("starmail", CommandStarMail::new);
//        commands.put("warehouse", CommandWarehouse::new);
    }

    @Override
    public void onDisable() {
        sadLibrary.destroy();

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

    public static StarMailAPI getStarMailAPI() {
        return starMailAPI;
    }

    public HashMap<String, Function<String[], ICommandSyntax>> getRegisteredCommand() {
        return registeredCommands;
    }

}
