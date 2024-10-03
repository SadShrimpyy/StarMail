package me.sword7.starmail.compatibility.versions;

import com.sadshiry.AssignTexture_V1_17_1;
import com.sadshiry.AssignTexture_V1_21_1;
import com.sadshiry.IAssignTexture;
import me.sword7.starmail.sys.Version;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class AssignTexture {

    private static final IAssignTexture manager;
    static {
        manager = Version.current.isMethodCreatePlayerProfileSupported()
                ? new AssignTexture_V1_21_1()
                : new AssignTexture_V1_17_1();
    }

    public static void assignTexture(ItemStack head, String base64, String profileName, UUID profileID) {
        manager.assingTexture(head, base64, profileName, profileID);
    }
}