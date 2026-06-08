package me.dunescifye.commandutils.utils;

import dev.jorel.commandapi.arguments.*;
import me.dunescifye.commandutils.files.Config;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ProxiedCommandSender;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class ArgumentUtils {

    private static Argument<List<List<Predicate<Block>>>> whitelistedBlocksArgument(String nodeName) {

        Argument<List> listArgument = new ListArgumentBuilder<String>(nodeName)
          .withList(Utils.getPredicatesList())
          .withStringMapper()
          .buildText();

        return new CustomArgument<>(
          listArgument,
          info -> {
              List<String> input = (List<String>) info.currentInput();
              if (input.size() == 1 && Config.getPredicates().contains(input.getFirst())) {
                  return Config.getPredicate(input.getFirst());
              }
              return Utils.stringListToPredicate(input);
          }
        ).replaceSuggestions(listArgument.getOverriddenSuggestions().get());
    }

    public static Argument<List<Material>> materialsArgument(String nodeName) {

        Argument<List> listArgument = new ListArgumentBuilder<String>(nodeName)
          .withList(Utils.getMaterialsList())
          .withStringMapper()
          .buildText();

        return new CustomArgument<>(
          listArgument,
          info -> Utils.stringListToMaterials((List<String>) info.currentInput())
        ).replaceSuggestions(listArgument.getOverriddenSuggestions().get());
    }

    public static Argument<Material> materialArgument(String nodeName) {
        return new CustomArgument<>(new StringArgument(nodeName),
          info -> Material.getMaterial(info.input().toUpperCase())
        ).replaceSuggestions(ArgumentSuggestions.strings(Utils.getMaterialsList()));
    }


    private static Argument<World> bukkitWorldArgument(String nodeName) {
        return new CustomArgument<>(new StringArgument(nodeName), info -> {
            World world = Bukkit.getWorld(info.input());

            if (world == null) {
                throw CustomArgument.CustomArgumentException.fromMessageBuilder(new CustomArgument.MessageBuilder("Unknown world ").appendArgInput());
            } else {
                return world;
            }
        }).replaceSuggestions(ArgumentSuggestions.strings(info ->
            Bukkit.getWorlds().stream().map(World::getName).toArray(String[]::new))
        );
    }

    public static Argument<Attribute> attributeArgument(String nodeName) {

        return new CustomArgument<>(new StringArgument(nodeName), info -> {
            try {
                return Registry.ATTRIBUTE.get(NamespacedKey.fromString(info.input().toLowerCase()));
            } catch (IllegalArgumentException e) {
                throw CustomArgument.CustomArgumentException.fromMessageBuilder(new CustomArgument.MessageBuilder("Unknown Attribute ").appendArgInput());
            }

        }).replaceSuggestions(ArgumentSuggestions.strings(
            Registry.ATTRIBUTE.stream().map(Attribute::toString).toArray(String[]::new)

        ));
    }

    public static Argument<AttributeModifier.Operation> operationArgument(String nodeName) {

        return new CustomArgument<>(new StringArgument(nodeName), info -> AttributeModifier.Operation.valueOf(info.input().toUpperCase()))
            .replaceSuggestions(ArgumentSuggestions.strings(
                Arrays.stream(AttributeModifier.Operation.values()).map(AttributeModifier.Operation::toString).toArray(String[]::new))
            );
    }

    public static Argument<Duration> timeArgument(String nodeName) {

        return new CustomArgument<>(new StringArgument(nodeName), info -> Utils.parseDuration(info.input()));
    }

    public static Argument<String> slotArgument(String nodeName) {
        return new CustomArgument<>(new StringArgument(nodeName), info ->
            info.input().toLowerCase()
        ).replaceSuggestions(ArgumentSuggestions.strings(Utils.getItemSlots()));
    }

    public static Player getPlayer(CommandSender sender) {
        return sender instanceof ProxiedCommandSender proxy ? (Player) proxy.getCallee() : (Player) sender;
    }

    /*
     * COMMON ARGUMENTS
     * This section holds all arguments to be reused throughout commands
     */

    public static LocationArgument locArg() { return new LocationArgument("Location"); }
    public static EntitySelectorArgument.OnePlayer playerArg() { return new EntitySelectorArgument.OnePlayer("Player"); }
    public static Argument<String> slotArg() { return slotArgument("Slot"); }
    public static TextArgument namespaceArg() {
        return new TextArgument("Namespace");
    }
    public static TextArgument keyArg() {
        return new TextArgument("Key");
    }
    public static GreedyStringArgument contentArg() { return new GreedyStringArgument("Content"); }
    public static Argument<World> worldArg() { return bukkitWorldArgument("World"); }
    public static LocationArgument blockLocArg() { return new LocationArgument("Location", LocationType.BLOCK_POSITION); }
    public static IntegerArgument radiusArg() { return new IntegerArgument("Radius", 0); }
    public static IntegerArgument depthArg() { return new IntegerArgument("Depth", 0); }
    public static BlockStateArgument blockStateArg() { return new BlockStateArgument("Block State"); }
    public static IntegerArgument durationArg() { return new IntegerArgument("Duration"); }
    public static IntegerArgument heightArg() { return new IntegerArgument("Height"); }
    public static EntitySelectorArgument.ManyEntities entitiesArg() { return new EntitySelectorArgument.ManyEntities(
        "Entities"); }
    public static Argument<Duration> delayArg() { return ArgumentUtils.timeArgument("Initial Delay"); }
    public static Argument<Duration> periodArg() { return ArgumentUtils.timeArgument("Period"); }
    public static Argument<Duration> maxTimeArg() { return ArgumentUtils.timeArgument("Max Time"); }
    public static FloatArgument yawArg() { return new FloatArgument("Yaw"); }
    public static FloatArgument pitchArg() { return new FloatArgument("Pitch"); }
    public static Argument<List<List<Predicate<Block>>>> whitelistedBlocksArg() { return whitelistedBlocksArgument(
        "Whitelisted Blocks"); }
    public static ParticleArgument particleArg() { return new ParticleArgument("Particle"); }
    public static IntegerArgument amountArg() { return new IntegerArgument("Amount"); }
    public static ItemStackArgument itemArg() { return new ItemStackArgument("Item"); }
}
