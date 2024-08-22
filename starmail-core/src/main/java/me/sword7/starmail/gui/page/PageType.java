package me.sword7.starmail.gui.page;

import org.bukkit.ChatColor;

import static me.sword7.starmail.sys.Language.*;

public enum PageType {

    MAIL_HOME(5, new MailOverview(), null),
    MAIL_NOTIFICATIONS(5, new MailNotifications(), MAIL_HOME),
    CRATE_SEAL(5, new CrateSeal(), null),
    CHEST_SEAL(5, new ChestSeal(), null),
    GIFT_SEAL(5, new GiftSeal(), null),
    PACKAGE_CONTENTS(5, new PackageContents(), null),
    EMPTY_PACKAGE(5, new EmptyPackage(), null),
    POSTBOX_MAIL(6, new PostboxMail(), null),
    POSTBOX_PLAYER(6, new PostboxPlayer(), null),
    POSTBOX_SEND(6, new PostboxSend(), null),
    WAREHOUSE_HOME(3, new WarehouseOverview(), null),
    WAREHOUSE_ITEM(3, new WarehouseItem(), WAREHOUSE_HOME),
    WAREHOUSE_FROM(3, new WarehouseFrom(), WAREHOUSE_HOME),
    WAREHOUSE_CONTENTS(5, new WarehouseContents(), WAREHOUSE_HOME),
    WAREHOUSE_STYLE(5, new WarehouseStyle(), WAREHOUSE_HOME),
    F_MAILBOX(6, new FMailbox(), null),
    ;

    private Page page;

    PageType(int rows, IPageContents contents, PageType previous) {
        this.page = new Page(this, rows, "", contents, previous);
    }

    public static void init() {
        String base = ChatColor.DARK_GRAY.toString() + ChatColor.BOLD;
        MAIL_HOME.page.setTitle(base + LABEL_MAILBOX);
        F_MAILBOX.page.setTitle(base + LABEL_MAILBOX);
        MAIL_NOTIFICATIONS.page.setTitle(base + LABEL_NOTIFICATIONS);
        CRATE_SEAL.page.setTitle(base + LABEL_CRATE);
        CHEST_SEAL.page.setTitle(base + LABEL_CHEST);
        GIFT_SEAL.page.setTitle(base + LABEL_GIFT);
        PACKAGE_CONTENTS.page.setTitle(base + LABEL_PACKAGE);
        EMPTY_PACKAGE.page.setTitle(base + LABEL_PACKAGE);

        String postTitle = LABEL_POSTBOX.toString();
        String promptBuffer = " " + ChatColor.DARK_GRAY + ChatColor.ITALIC;
        POSTBOX_MAIL.page.setTitle(base + postTitle + promptBuffer + ICON_INSERT_MAIL);
        POSTBOX_PLAYER.page.setTitle(base + postTitle + promptBuffer + ICON_TO);
        POSTBOX_SEND.page.setTitle(base + postTitle + promptBuffer + ICON_SEND_MAIL);


        String wareTitle = LABEL_WAREHOUSE.toString();
        WAREHOUSE_HOME.page.setTitle(base + wareTitle);
        WAREHOUSE_ITEM.page.setTitle(base + wareTitle + promptBuffer + LABEL_ITEM);
        WAREHOUSE_CONTENTS.page.setTitle(base + wareTitle + promptBuffer + LABEL_CONTENTS);
        WAREHOUSE_STYLE.page.setTitle(base + wareTitle + promptBuffer + LABEL_STYLE);
        WAREHOUSE_FROM.page.setTitle(base + wareTitle + promptBuffer + LABEL_FROM);
    }

    public Page getPage() {
        return page;
    }

}
