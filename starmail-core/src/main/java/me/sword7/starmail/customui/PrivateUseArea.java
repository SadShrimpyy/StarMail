package me.sword7.starmail.customui;

public enum PrivateUseArea {

    NEG1("\uF801"),
    NEG2("\uF802"),
    NEG4("\uF804"),
    NEG8("\uF808"),
    NEG16("\uF809"),
    NEG32("\uF80A"),
    NEG64("\uF80B"),
    NEG128("\uF80C"),
    NEG256("\uF80D"),
    NEG512("\uF80E"),
    NEG1024("\uF80F"),

    POS1("\uF821"),
    POS2("\uF822"),
    POS4("\uF824"),
    POS8("\uF828"),
    POS16("\uF829"),
    POS32("\uF82A"),
    POS64("\uF82B"),
    POS128("\uF82C"),
    POS256("\uF82D"),
    POS512("\uF82E"),
    POS1024("\uF82F"),

    MENU_CONTAINER_27("\uF001"),
    MENU_CONTAINER_36("\uF002"),
    MENU_BUTTON("\uF003");

    public final String literal;
    PrivateUseArea(String literal) {
        this.literal = literal;
    }

    @Override
    public String toString() {
        return this.literal;
    }

    private enum SpacingCharacters {
        NEG1(-1, PrivateUseArea.NEG1),
        NEG2(-2, PrivateUseArea.NEG2),
        NEG4(-4, PrivateUseArea.NEG4),
        NEG8(-8, PrivateUseArea.NEG8),
        NEG16(-16, PrivateUseArea.NEG16),
        NEG32(-32, PrivateUseArea.NEG32),
        NEG64(-64, PrivateUseArea.NEG64),
        NEG128(-128, PrivateUseArea.NEG128),
        NEG256(-256, PrivateUseArea.NEG256),
        NEG512(-512, PrivateUseArea.NEG512),
        NEG1024(-1024, PrivateUseArea.NEG1024),

        POS1(1, PrivateUseArea.POS1),
        POS2(2, PrivateUseArea.POS2),
        POS4(4, PrivateUseArea.POS4),
        POS8(8, PrivateUseArea.POS8),
        POS16(16, PrivateUseArea.POS16),
        POS32(32, PrivateUseArea.POS32),
        POS64(64, PrivateUseArea.POS64),
        POS128(128, PrivateUseArea.POS128),
        POS256(256, PrivateUseArea.POS256),
        POS512(512, PrivateUseArea.POS512),
        POS1024(1024, PrivateUseArea.POS1024);

        private final int weight;
        private final PrivateUseArea charRef;

        SpacingCharacters(int weight, PrivateUseArea charRef) {
            this.weight = weight;
            this.charRef = charRef;
        }
    }

    public static String getSpacing(int pixelAmount) {
        String binary = new StringBuilder(Integer.toBinaryString(Math.abs(pixelAmount)))
                .reverse().toString();
        StringBuilder sb = new StringBuilder();
        char[] binaries = binary.toCharArray();
        for(int index = 0; index < binaries.length; index++){
            char ch = binaries[index];
            if(ch == '0') continue;

            int weight = (int) Math.pow(2, index);
            weight = pixelAmount < 0
                    ? -weight
                    : weight;
            PrivateUseArea ref = getCharacterByWeight(weight);

            if(ref != null) sb.append(ref.literal);
        }
        return sb.toString();
    }

    public static String getPositiveChars(int pixelAmount) {
        return getSpacing(Math.abs(pixelAmount));
    }

    public static String getNegativeChars(int pixelAmount) {
        return getSpacing(-Math.abs(pixelAmount));
    }

    public static PrivateUseArea getCharacterByWeight(int weight) {
        for(SpacingCharacters ch : SpacingCharacters.values()) {
            if(ch.weight == weight)
                return ch.charRef;
        }
        return null;
    }
}
