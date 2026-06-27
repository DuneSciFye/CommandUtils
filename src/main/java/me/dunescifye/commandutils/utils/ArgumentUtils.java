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

    private static Argument<String> slotArgument(String nodeName) {
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

    public static final String LOC_NAME = "Location";
    public static final String PLAYER_NAME = "Player";
    public static final String PLAYERS_NAME = "Players";
    public static final String SLOT_NAME = "Slot";
    public static final String NAMESPACE_NAME = "Namespace";
    public static final String KEY_NAME = "Key";
    public static final String CONTENT_NAME = "Content";
    public static final String WORLD_NAME = "World";
    public static final String BLOCK_LOC_NAME = "Location";
    public static final String RADIUS_NAME = "Radius";
    public static final String DEPTH_NAME = "Depth";
    public static final String BLOCK_STATE_NAME = "Block State";
    public static final String DURATION_NAME = "Duration";
    public static final String HEIGHT_NAME = "Height";
    public static final String ENTITIES_NAME = "Entities";
    public static final String INITIAL_DELAY_NAME = "Initial Delay";
    public static final String PERIOD_NAME = "Period";
    public static final String MAX_TIME_NAME = "Max Time";
    public static final String YAW_NAME = "Yaw";
    public static final String PITCH_NAME = "Pitch";
    public static final String WHITELISTED_BLOCKS_NAME = "Whitelisted Blocks";
    public static final String PARTICLE_NAME = "Particle";
    public static final String AMOUNT_NAME = "Amount";
    public static final String ITEM_NAME = "Item";
    public static final String TIME_NAME = "Time";
    public static final String MAX_ALIVE_TIME_NAME = "Max Alive Time";
    public static final String VELOCITY_MULTIPLIER_NAME = "Velocity Multiplier";
    public static final String GENERATE_BLOCK_BREAK_EVENT_NAME = "Generate Block Break Event";
    public static final String CHECK_CLAIM_NAME = "Check Claim";
    public static final String AUTO_PICKUP_NAME = "Auto Pickup";

    public static LocationArgument locArg() { return new LocationArgument(LOC_NAME); }
    public static EntitySelectorArgument.OnePlayer playerArg() { return new EntitySelectorArgument.OnePlayer(PLAYER_NAME); }
    public static EntitySelectorArgument.ManyPlayers manyPlayersArg() { return new EntitySelectorArgument.ManyPlayers(PLAYERS_NAME); }
    public static Argument<String> slotArg() { return slotArgument(SLOT_NAME); }
    public static TextArgument namespaceArg() {
        return new TextArgument(NAMESPACE_NAME);
    }
    public static TextArgument keyArg() {
        return new TextArgument(KEY_NAME);
    }
    public static GreedyStringArgument contentArg() { return new GreedyStringArgument(CONTENT_NAME); }
    public static Argument<World> worldArg() { return bukkitWorldArgument(WORLD_NAME); }
    public static LocationArgument blockLocArg() { return new LocationArgument(BLOCK_LOC_NAME, LocationType.BLOCK_POSITION); }
    public static IntegerArgument radiusArg() { return new IntegerArgument(RADIUS_NAME, 0); }
    public static IntegerArgument depthArg() { return new IntegerArgument(DEPTH_NAME, 0); }
    public static BlockStateArgument blockStateArg() { return new BlockStateArgument(BLOCK_STATE_NAME); }
    public static IntegerArgument durationArg() { return new IntegerArgument(DURATION_NAME); }
    public static IntegerArgument heightArg() { return new IntegerArgument(HEIGHT_NAME); }
    public static EntitySelectorArgument.ManyEntities entitiesArg() { return new EntitySelectorArgument.ManyEntities(ENTITIES_NAME); }
    public static Argument<Duration> delayArg() { return ArgumentUtils.timeArgument(INITIAL_DELAY_NAME); }
    public static Argument<Duration> periodArg() { return ArgumentUtils.timeArgument(PERIOD_NAME); }
    public static Argument<Duration> maxTimeArg() { return ArgumentUtils.timeArgument(MAX_TIME_NAME); }
    public static FloatArgument yawArg() { return new FloatArgument(YAW_NAME); }
    public static FloatArgument pitchArg() { return new FloatArgument(PITCH_NAME); }
    public static Argument<List<List<Predicate<Block>>>> whitelistedBlocksArg() { return whitelistedBlocksArgument(WHITELISTED_BLOCKS_NAME); }
    public static ParticleArgument particleArg() { return new ParticleArgument(PARTICLE_NAME); }
    public static IntegerArgument amountArg() { return new IntegerArgument(AMOUNT_NAME); }
    public static ItemStackArgument itemArg() { return new ItemStackArgument(ITEM_NAME); }
}
