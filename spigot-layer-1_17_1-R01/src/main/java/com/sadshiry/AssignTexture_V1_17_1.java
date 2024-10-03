package com.sadshiry;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.UUID;

public class AssignTexture_V1_17_1 implements IAssignTexture {

    /**
     * @implNote This method uses the 1.17.1-R.01 Spigot API and it's valid until 1.20.6 included
     * @deprecated From 1.21.1 included
     * */
    public void assingTexture(ItemStack head, String base64, String profileName, UUID profileID) {
        SkullMeta skullMeta = (SkullMeta)head.getItemMeta();
        GameProfile profile = new GameProfile(profileID, profileName);
        profile.getProperties().put("textures", new Property("textures", base64));

        try {
            Field profileField = skullMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(skullMeta, profile);
            head.setItemMeta(skullMeta);
        } catch (SecurityException | NoSuchFieldException | IllegalAccessException | IllegalArgumentException var8) {
            Exception e = var8;
            ((Exception)e).printStackTrace();
        }

    }
}
