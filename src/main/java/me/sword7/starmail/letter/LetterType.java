package me.sword7.starmail.letter;

import me.sword7.starmail.sys.Language;
import me.sword7.starmail.util.MailColor;

public enum LetterType {

    DEFAULT(220, Language.LABEL_LETTER_AND_QUILL.toString()),
    BLACK(222, MailColor.BLACK),
    RED(224, MailColor.RED),
    GREEN(226, MailColor.GREEN),
    BROWN(228, MailColor.BROWN),
    BLUE(230, MailColor.BLUE),
    PURPLE(232, MailColor.PURPLE),
    CYAN(234, MailColor.CYAN),
    LIGHT_GRAY(236, MailColor.LIGHT_GRAY),
    GRAY(238, MailColor.GRAY),
    PINK(240, MailColor.PINK),
    LIME(242, MailColor.LIME),
    YELLOW(244, MailColor.YELLOW),
    LIGHT_BLUE(246, MailColor.LIGHT_BLUE),
    MAGENTA(248, MailColor.MAGENTA),
    ORANGE(250, MailColor.ORANGE),
    WHITE(252, MailColor.WHITE),
    FANCY_RED(254, Language.LABEL_FANCY_LETTER_AND_QUILL.fromColor(Language.COLOR_RED)),
    FANCY_GREEN(256, Language.LABEL_FANCY_LETTER_AND_QUILL.fromColor(Language.COLOR_GREEN)),
    FANCY_BLUE(258, Language.LABEL_FANCY_LETTER_AND_QUILL.fromColor(Language.COLOR_BLUE)),
    FANCY_PURPLE(260, Language.LABEL_FANCY_LETTER_AND_QUILL.fromColor(Language.COLOR_PURPLE)),
    CUSTOM,
    ;


    public static LetterType[] craftable = {
            BLACK, RED, GREEN, BROWN, BLUE,
            PURPLE, CYAN, LIGHT_GRAY, GRAY, PINK,
            LIME, YELLOW, LIGHT_BLUE, MAGENTA,
            ORANGE, WHITE
    };

    private Letter letter;

    LetterType(){}

    LetterType(int modelData, MailColor mailColor) {
        this.letter = new Letter(this, mailColor, modelData);
    }

    LetterType(int modelData, String letterAndQuillName) {
        this.letter = new Letter(this, this.toString(), letterAndQuillName, modelData);
    }

    public Letter getLetter() {
        return letter;
    }

}
