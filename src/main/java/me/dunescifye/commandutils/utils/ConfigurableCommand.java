package me.dunescifye.commandutils.utils;

import dev.dejvokep.boostedyaml.YamlDocument;

public interface ConfigurableCommand {
    void register(YamlDocument config);
}
