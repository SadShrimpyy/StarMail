package me.sword7.starmail.post;

import me.sword7.starmail.util.storage.StorageUtil;
import me.sword7.starmail.sys.config.PluginConfig;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static me.sword7.starmail.sys.Language.CONST_UNKNOWN;

public class PostFlatFile {

    private static final File DIR = new File("plugins/StarMail/Data/Mail");

    public void store(UUID playerID, List<Mail> mailList) {
        File file = new File(DIR, playerID.toString() + ".yml");
        file.delete();
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);

        if (mailList != null && mailList.size() > 0) {

            for (int index = 0; index < mailList.size(); index++) {
                Mail mail = mailList.get(index);
                config.set(index + ".from", mail.getFrom());
                config.set(index + ".time", mail.getTimestamp().toString());
                config.set(index + ".item", mail.getItemStack());
            }

            StorageUtil.save(config, file);
        } else {
            file.delete();
        }
    }

    public List<Mail> fetch(UUID playerID) {
        List<Mail> mailList = new ArrayList<>();
        File file = new File(DIR, playerID.toString() + ".yml");
        if (file.exists()) {
            FileConfiguration config = YamlConfiguration.loadConfiguration(file);
            boolean hasExpired = false;
            for (String indexString : config.getRoot().getKeys(false)) {
                try {
                    String from = config.getString(indexString + ".from", CONST_UNKNOWN.toString());
                    Timestamp timestamp = Timestamp.valueOf(config.getString(indexString + ".time", ""));
                    ItemStack itemStack = config.getItemStack(indexString + ".item", null);
                    if (!StorageUtil.isExpired(timestamp, PluginConfig.getMailExpirationDays())) {
                        if (timestamp != null && itemStack != null) mailList.add(new Mail(itemStack, from, timestamp));
                    } else {
                        hasExpired = true;
                        config.set(indexString, null);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (mailList.size() == 0) {
                file.delete();
            } else if (hasExpired) {
                StorageUtil.save(config, file);
            }
        }
        return mailList;
    }

}
