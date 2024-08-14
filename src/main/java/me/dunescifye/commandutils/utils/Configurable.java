package me.dunescifye.commandutils.utils;

import dev.dejvokep.boostedyaml.YamlDocument;

public interface Configurable {
    void register(YamlDocument config);
}
