package me.shiry_recode.starmail.sadlibrary;

public enum SadPlaceholders {

    PlayerName("%player-name%"),
    PlayerInvolved("%player-involved%"),
    PlayerExecutor("%player-executor%"),
    ActionName("%action-name%"),

    Prefix("%prefix%"),
    Permission("%permission%"),
    Command("%command%"),

    HelpCurPage("%help-cur-page%"),
    HelpMaxPage("%help-max-page%"),
    HelpPrevPage("%help-prev-page%"),
    HelpNextPage("%help-next-page%"),

    HelpBtnNext("%help-button-next%"),
    HelpBtnPrev("%help-button-previous%")
    ;

    private final String text;

    SadPlaceholders(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return this.text;
    }
}
