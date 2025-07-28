package me.dunescifye.commandutils.utils;

import dev.jorel.commandapi.arguments.*;
import me.dunescifye.commandutils.files.Config;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.List;
import java.util.function.Predicate;

public class ArgumentUtils {

    public static Argument<List<List<Predicate<Block>>>> configPredicateArgument(String nodeName) {
        return new CustomArgument<>(new StringArgument(nodeName), info -> Config.getPredicate(info.input()))
            .replaceSuggestions(ArgumentSuggestions.strings(Config.getPredicates()));
    }


    public static Argument<List<List<Predicate<Block>>>> commandWhitelistArgument(String nodeName) {

        Argument<List> listArgument = new ListArgumentBuilder<String>(nodeName)
          .withList(Utils.getPredicatesList())
          .withStringMapper()
          .buildText();

        return new CustomArgument<>(
          listArgument,
          info -> Utils.stringListToPredicate((List<String>) info.currentInput())
        ).replaceSuggestions(listArgument.getOverriddenSuggestions().get());
    }

    public static Argument<List<Material>> materialArgument(String nodeName) {

        Argument<List> listArgument = new ListArgumentBuilder<String>(nodeName)
          .withList(Utils.getMaterialsList())
          .withStringMapper()
          .buildText();

        return new CustomArgument<>(
          listArgument,
          info -> Utils.stringListToMaterials((List<String>) info.currentInput())
        ).replaceSuggestions(listArgument.getOverriddenSuggestions().get());
    }

}
