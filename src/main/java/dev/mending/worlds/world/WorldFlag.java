package dev.mending.worlds.world;

import lombok.Getter;

@Getter
public enum WorldFlag {

    PVP("pvp"),
    BLOCK_BREAK("blockBreak"),
    BLOCK_PLACE("blockPlace");

    final private String name;

    WorldFlag(String name) {
        this.name = name;
    }

    public static WorldFlag getByName(String name) {
        for (WorldFlag flag : values()) {
            if (flag.getName().equals(name)) {
                return flag;
            }
        }
        return null;
    }
}
