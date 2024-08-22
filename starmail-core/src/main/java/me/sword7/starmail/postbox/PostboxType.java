package me.sword7.starmail.postbox;

import me.sword7.starmail.util.X.XGlass;

import java.util.UUID;

public enum PostboxType {
    DEFAULT(XGlass.BROWN, XGlass.RED, XGlass.GRAY, UUID.fromString("3b28de42-5619-4737-910f-1a233f7c2878"),
            "ewogICJ0aW1lc3RhbXAiIDogMTU5Njg5Mjc2ODk0OCwKICAicHJvZmlsZUlkIiA6ICJmZDYwZjM2ZjU4NjE0ZjEyYjNjZDQ3YzJkODU1Mjk5YSIsCiAgInByb2ZpbGVOYW1lIiA6ICJSZWFkIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzYzZmZlNWJiMTM2NGM0NjEyZjJhMmZiNDgxNzE3NDI1OTQ1ZTEzZjgyMmU3OGIwZmNmYmU0M2E0YWU4ZThjMDgiCiAgICB9CiAgfQp9"),
    SPACE(XGlass.MAGENTA, XGlass.WHITE, XGlass.PINK, UUID.fromString("60519ffe-0b6f-4591-82f8-e3dd4ac2a7a3"),
            "ewogICJ0aW1lc3RhbXAiIDogMTU5Njg5MjgyMDQ5NywKICAicHJvZmlsZUlkIiA6ICI5MThhMDI5NTU5ZGQ0Y2U2YjE2ZjdhNWQ1M2VmYjQxMiIsCiAgInByb2ZpbGVOYW1lIiA6ICJCZWV2ZWxvcGVyIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2FhZjBiM2MwYzgyMzEyODA0ZjBlYjQwNjJiYThhODRhYzgwYWI2ODI4ZDNjYjQyNjMxZWNiYjlmNjBiMTA5N2QiCiAgICB9CiAgfQp9"),
    RED(XGlass.RED, UUID.fromString("39da52c0-f6ff-4f86-a1aa-e3084cdf79af"),
            "ewogICJ0aW1lc3RhbXAiIDogMTU5Njg5Mjg1Njc5OCwKICAicHJvZmlsZUlkIiA6ICI0ZWQ4MjMzNzFhMmU0YmI3YTVlYWJmY2ZmZGE4NDk1NyIsCiAgInByb2ZpbGVOYW1lIiA6ICJGaXJlYnlyZDg4IiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzIyMGMxZWQ1NDVhNDliMGRjNDE5MmE4YTYzZDQ5MGVhOTkyMDUyYzY1YmI4NTM4ODhiMzU1NzY3M2Q2ZTE5MTQiCiAgICB9CiAgfQp9"),
    GREEN(XGlass.GREEN, UUID.fromString("2c7733e5-cea2-4c7b-b74f-ace493592ae3"),
            "ewogICJ0aW1lc3RhbXAiIDogMTU5Njg5Mjg5MTMyNiwKICAicHJvZmlsZUlkIiA6ICIwNGI3MDhhMzM1NjY0ZjJmODVlYzVlZWYyN2QxNGRhZCIsCiAgInByb2ZpbGVOYW1lIiA6ICJWaW9sZXRza3l6eiIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS84MTAzOWZjZGY4NjM0MWFjZDIwZjI5ZTNlYzk1OGY2ZjNhMWE0ZGZmYTEwYWJkOTE5MDVhNWNjOWYzOTk5OGIwIgogICAgfQogIH0KfQ=="),
    BLUE(XGlass.BLUE, UUID.fromString("b41670db-0a74-4ec7-acc2-28dcdba106fb"),
            "ewogICJ0aW1lc3RhbXAiIDogMTU5Njg5MjkzMjQwNywKICAicHJvZmlsZUlkIiA6ICIyM2YxYTU5ZjQ2OWI0M2RkYmRiNTM3YmZlYzEwNDcxZiIsCiAgInByb2ZpbGVOYW1lIiA6ICIyODA3IiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzdkZDkzNmE1MTc3YWY5N2I3MTNmZDVmZTVkYzA3MTIyM2JhMjVmYjgzMWIwYmE0Njg3NDMxY2IxZGRiMDA3YzgiCiAgICB9CiAgfQp9"),
    CUSTOM,
    ;

    private Postbox postbox;

    PostboxType() {

    }

    PostboxType(XGlass xGlass, UUID uuid, String data) {
        this.postbox = new Postbox(this, this.toString(), xGlass, uuid, data);
    }

    PostboxType(XGlass xGlass, XGlass hatBase, XGlass hatHighlight, UUID uuid, String data) {
        this.postbox = new HatBox(this, this.toString(), xGlass, hatBase, hatHighlight, uuid, data);
    }

    public Postbox getPostbox() {
        return postbox;
    }

}
