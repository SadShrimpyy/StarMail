package me.sword7.starmail.gui.data;

import me.sword7.starmail.gui.MenuUtil;
import me.sword7.starmail.gui.page.PageType;
import me.sword7.starmail.gui.page.PostboxSendAnimation;
import me.sword7.starmail.post.Mail;
import me.sword7.starmail.postbox.Postbox;
import me.sword7.starmail.sys.Language;
import me.sword7.starmail.user.User;
import me.sword7.starmail.user.UserCache;
import me.sword7.starmail.util.Key;
import me.sword7.starmail.util.MailUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class PostData extends TipData implements IUpdateable {

    private Postbox postbox;
    private User to;
    private boolean validMails = false;
    private String input;
    private String suggestedCompletion = "";
    private keyState state;
    private ItemStack[] mails = new ItemStack[3];
    private Map<Integer, PostboxSendAnimation> slotToAnimation = new HashMap<>();

    public void registerAnimation(int slot, PostboxSendAnimation animation) {
        slotToAnimation.put(slot, animation);
    }

    public void removeAnimation(int slot) {
        slotToAnimation.remove(slot);
    }

    public boolean isAnimating(int slot) {
        return slotToAnimation.containsKey(slot);
    }

    public boolean noAnimationsOrMail() {
        boolean hasMail = false;
        for (int i = 0; i < mails.length; i++) {
            if (mails[i] != null) {
                hasMail = true;
                break;
            }
        }
        return slotToAnimation.isEmpty() && !hasMail;
    }

    public void reset() {
        validMails = false;
        mails = new ItemStack[3];
        slotToAnimation.clear();
    }

    public PostboxSendAnimation getAnimation(int slot) {
        return slotToAnimation.get(slot);
    }

    public void changeAnimationMenu(Inventory menu) {
        for (PostboxSendAnimation animation : slotToAnimation.values()) {
            animation.setMenu(menu);
        }
    }

    public PostData(Player player, Postbox postbox) {
        super(PageType.POSTBOX_MAIL.getPage(), player);
        setTheme(postbox.getXGlass());
        this.postbox = postbox;
        input = "";
        state = keyState.LETTERS;
    }

    public ItemStack[] getMails() {
        return mails;
    }

    public void setMails(ItemStack[] mails) {
        this.mails = mails;
        checkValid();
    }

    public void checkValid() {
        validMails = false;
        for (ItemStack itemStack : mails) {
            if (itemStack != null) {
                if (Mail.isMail(player, itemStack)) {
                    validMails = true;
                } else {
                    validMails = false;
                    return;
                }
            }
        }
    }


    public boolean hasTo() {
        return to != null;
    }

    public User getTo() {
        return to;
    }

    public Postbox getPostbox() {
        return postbox;
    }

    public boolean isValidMails() {
        return validMails;
    }

    public void acceptSuggestion() {
        input += suggestedCompletion;
        suggestedCompletion = "";
    }

    public void doKey(Key key) {
        if (key == Key.BACK) {
            if (input.length() > 0) {
                input = input.substring(0, input.length() - 1);
            }
        } else if (key == Key.LETTERS) {
            state = keyState.LETTERS;
        } else if (key == Key.NUMBERS) {
            state = keyState.NUMBERS;
        } else {
            if (input.length() < 16) {
                state = keyState.LETTERS;
                if (key == Key.UNDERSCORE) {
                    input += "_";
                } else {
                    input += key.toString().replace("NUM_", "");
                }
            } else {
                MenuUtil.playErrorSound(player);
            }
        }

        generateSuggestion();
        //TODO put in busy cache, on future, check if input same, if not , do another check (do ...s) (if looking put ... when transition)
        UserCache.getUser(input + suggestedCompletion, (User to) -> {
            if (isValid() && current.getType() == PageType.POSTBOX_PLAYER){
                this.to = to;
                transitionSilent(current.getType());
            }
        });

    }

    //TODO use user data instead of player
    private void generateSuggestion() {
        suggestedCompletion = "";
        if (input.length() > 0) {
            Player player = Bukkit.getPlayer(input);
            if (player != null) {
                String playerName = player.getName().toUpperCase();
                boolean acceptCompletion = true;
                if (playerName.length() > input.length()) {
                    for (int i = 0; i < input.length(); i++) {
                        if (input.charAt(i) != playerName.charAt(i)) {
                            acceptCompletion = false;
                        }
                    }
                } else {
                    acceptCompletion = false;
                }
                suggestedCompletion = acceptCompletion ? playerName.substring(input.length()) : "";
            }
        }
    }

    public keyState getState() {
        return state;
    }

    @Override
    public void updateContents(Inventory menu) {
        if (current.getType() == PageType.POSTBOX_MAIL) {
            ItemStack[] mails = new ItemStack[3];
            mails[0] = menu.getItem(30);
            mails[1] = menu.getItem(31);
            mails[2] = menu.getItem(32);
            setMails(mails);
        }
    }

    public enum keyState {
        LETTERS,
        NUMBERS,
    }


    @Override
    public String getEffectiveTitle() {
        String title = getCurrent().getTitle();
        if (getCurrent().getType() == PageType.POSTBOX_PLAYER) {
            title += " " + ChatColor.DARK_GRAY + input + ChatColor.GRAY + suggestedCompletion;
        }
        if (doTip) title = titleBase + Language.LABEL_POSTBOX + " " + tip;
        return title;
    }

    @Override
    public void onEnd() {
        MailUtil.giveItems(player, mails);
    }

}
