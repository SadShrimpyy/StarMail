package me.sword7.starmail.util;

import me.sword7.starmail.sys.Version;

public enum Symbol {
    UP("↑", "⬆"),
    DOWN("↓", "⬇"),
    LEFT("←", "⬅"),
    RIGHT("→", "➡"),
    ;

    private String symbol;
    private String legacy;

    Symbol(String symbol, String legacy) {
        this.symbol = symbol;
        this.legacy = legacy;
    }

    public String getSymbol() {
        return Version.current.value >= 113 ? symbol : legacy;
    }

    @Override
    public String toString(){
        return getSymbol();
    }
}
