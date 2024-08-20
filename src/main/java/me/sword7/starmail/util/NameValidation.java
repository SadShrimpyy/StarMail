package me.sword7.starmail.util;

import me.sword7.starmail.sys.Language;

public class NameValidation {

    public static Status clean(String name) {
        if (!name.matches("^[a-zA-Z0-9\\_\\-]+$")) {
            return Status.SPECIAL_CHARACTERS;
        } else if (name.length() > 20) {
            return Status.TOO_LONG;
        } else {
            return Status.VALID;
        }
    }

    public enum Status {
        VALID(""),
        SPECIAL_CHARACTERS(Language.MISC_SPECIAL_CHAR.toString()),
        TOO_LONG(Language.MISC_TOO_LONG.toString()),
        ;

        public final String message;

        Status(String message) {
            this.message = message;
        }


    }


}