package me.dunescifye.commandutils.commands;

import dev.dejvokep.boostedyaml.YamlDocument;

public interface Configurable {
    void register(YamlDocument config);
}
