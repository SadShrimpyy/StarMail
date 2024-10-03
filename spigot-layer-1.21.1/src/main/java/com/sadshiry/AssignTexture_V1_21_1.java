package com.sadshiry;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Base64;
import java.util.UUID;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;

public class AssignTexture_V1_21_1 implements IAssignTexture {

    /**
     * @implNote This method uses the 1.21.1-R.01 Spigot API new method's that didn't accept anymore a Base64
     * @deprecated From 1.21.1 below because it didn't do exists
     *
     * An example of how before the decoded base64 string is:
     *   ewogICJ0aW1lc3RhbXAiIDogMTU5NjY0MzUzMzM4MSwKICAicHJvZmlsZUlkIiA6ICI5MThhMDI5NTU5ZGQ0Y2U2YjE2ZjdhNWQ1M2VmYjQxMiIsCiAgInByb2ZpbGVOYW1lIiA6ICJCZWV2ZWxvcGVyIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2QyMTQzOTI0YWM5ODFiYmIwNGI5ZGUyODI4MGE4NWI5MjllYjI4MTA2MmI5N2Q0OWI4NGY2MzI4NTE0MjY2N2UiCiAgICB9CiAgfQp9
     * So you can decode the base64 and obtain:
     *   """
     *    {
     *     "timestamp" : 1596643533381,
     *     "profileId" : "918a029559dd4ce6b16f7a5d53efb412",
     *     "profileName" : "Beeveloper",
     *     "signatureRequired" : true,
     *     "textures" : {
     *       "SKIN" : {
     *         "url" : "http://textures.minecraft.net/texture/d2143924ac981bbb04b9de28280a85b929eb281062b97d49b84f63285142667e"
     *       }
     *     }
     *   }
     *   """
     * And parsing it with json format you can obtain the url and feed it into the new method
     * */
    public void assingTexture(ItemStack head, String base64, String profileName, UUID profileID) {
        SkullMeta skullMeta = (SkullMeta)head.getItemMeta();
        PlayerProfile profile = Bukkit.createPlayerProfile(profileID, profileName);
        PlayerTextures textures = profile.getTextures();
        Gson gson = new Gson();

        JsonObject jsonObject = gson.fromJson(new String(Base64.getDecoder().decode(base64)), JsonObject.class);
        String decodedBase64 = jsonObject.getAsJsonObject("textures").getAsJsonObject("SKIN").get("url").getAsString();
        try {
            textures.setSkin(new URL(decodedBase64));
        } catch (MalformedURLException e) {
            System.out.println(" Error assigning head texture caused by:" + e.getMessage());
        }

        profile.setTextures(textures);
        if (skullMeta != null) {
            skullMeta.setOwnerProfile(profile);
            head.setItemMeta(skullMeta);
        }

    }
}