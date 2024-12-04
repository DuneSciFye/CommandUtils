package me.dunescifye.commandutils.placeholders;

import dev.dejvokep.boostedyaml.YamlDocument;
import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.dunescifye.commandutils.CommandUtils;
import me.dunescifye.commandutils.commands.TempPlayerVarCommand;
import me.dunescifye.commandutils.commands.TempVarCommand;
import me.dunescifye.commandutils.utils.Utils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ArmorMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.RayTraceResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Placeholders extends PlaceholderExpansion {

    private static String defaultSeparator = ",", elseIfKeyword, elseKeyword, conditionSeparator;
    private static String nbtSeparator = ",";
    private static String amountSeparator = ",";

    public Placeholders(CommandUtils plugin, YamlDocument config) {
        Logger logger = plugin.getLogger();

        if (config.isString("Placeholders.StringUtils.If.ElseIfKeyword")) {
            elseIfKeyword = config.getString("Placeholders.StringUtils.If.ElseIfKeyword");
            if (elseIfKeyword == null)
                config.set("Placeholders.StringUtils.If.ElseIfKeyword", "elseif");
        } else {
            logger.warning("Configuration Placeholders.StringUtils.If.ElseIfKeyword is not a String. Using default value of `elseif`");
            elseIfKeyword = "elseif";
        }

        if (config.isString("Placeholders.StringUtils.If.ElseKeyword")) {
            elseKeyword = config.getString("Placeholders.StringUtils.If.ElseKeyword");
            if (elseKeyword == null)
                config.set("Placeholders.StringUtils.If.elseKeyword", "else");
        } else {
            logger.warning("Configuration Placeholders.StringUtils.If.ElseKeyword is not a String. Using default value of `else`");
            elseKeyword = "else";
        }

        if (config.isString("Placeholders.StringUtils.If.ConditionSeparator")) {
            conditionSeparator = config.getString("Placeholders.StringUtils.If.ConditionSeparator");
            if (conditionSeparator == null)
                config.set("Placeholders.StringUtils.If.ConditionSeparator", "\\\"");
        } else {
            logger.warning("Configuration Placeholders.StringUtils.If.ConditionSeparator is not a String. Using default value of `\"`");
            conditionSeparator = "\"";
        }
        if (config.isString("Placeholders.InvUtils.Nbt.ArgsSeparator")) {
            nbtSeparator = config.getString("Placeholders.InvUtils.Nbt.ArgsSeparator");
            if (nbtSeparator == null)
                config.set("Placeholders.InvUtils.Nbt.ArgsSeparator", ",");
        } else {
            logger.warning("Configuration Placeholders.InvUtils.Nbt.ArgsSeparator is not a String. Using default value of `,`");
            nbtSeparator = ",";
        }

        if (config.isString("Placeholders.InvUtils.Amount.ArgsSeparator")) {
            amountSeparator = config.getString("Placeholders.InvUtils.Amount.ArgsSeparator");
            if (amountSeparator == null)
                config.set("Placeholders.InvUtils.Amount.ArgsSeparator", ",");
        } else {
            logger.warning("Configuration Placeholders.InvUtils.Amount.ArgsSeparator is not a String. Using default value of `,`");
            amountSeparator = ",";
        }
    }


    public static void setSeparator(String separator) {
        Placeholders.defaultSeparator = separator;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "stringutils";
    }

    @Override
    public @NotNull String getAuthor() {
        return "DuneSciFye";
    }

    @Override
    public @NotNull String getVersion() {
        return "2.1.4";
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public @Nullable String onRequest(OfflinePlayer player, @NotNull String args) {
        String[] parts = args.split("_", 2); //Get function
        String function = parts[0];
        String arguments = parts.length == 2 ? PlaceholderAPI.setBracketPlaceholders(player, parts[1]) : null;
        String separator = defaultSeparator;

        Player p = player.getPlayer();

        String output = function(function, arguments, separator, p);
        if (output != null && output.equals("Unknown function")) {
            String[] temp = arguments.split("_", 2);
            output = function(temp[0], temp[1], function, p);
        }

        return output;
        //return super.onRequest(player, args);
    }

    private String function(String function, String arguments, String separator, Player p) {
        switch (function) {
            case "inputoutput" -> {
                String[] split = StringUtils.splitByWholeSeparatorPreserveAllTokens(arguments, separator);
                int length = split.length;
                if (length > 1) {
                    for (int i = 0; i < (length - 1) / 2; i++) {
                        if (Objects.equals(split[0], split[1 + i * 2])) {
                            return split[2 + i * 2];
                        }
                    }
                    return length % 2 == 1 ? "" : split[length - 1];
                }
            }
            case "inputoutputcycle" -> {
                String[] splitInputOutputCycle = StringUtils.splitByWholeSeparatorPreserveAllTokens(arguments, separator);
                int lengthInputOutputCycle = splitInputOutputCycle.length;
                if (lengthInputOutputCycle > 1) {
                    for (int i = 0; i < (lengthInputOutputCycle - 1); i++) {
                        if (Objects.equals(splitInputOutputCycle[0], splitInputOutputCycle[1 + i])) {
                            return splitInputOutputCycle[2 + i];
                        }
                    }
                    return lengthInputOutputCycle % 2 == 1 ? "" : splitInputOutputCycle[lengthInputOutputCycle - 1];
                }
            }
            case "randomint" -> {
                String[] partsRandomInt = StringUtils.splitByWholeSeparatorPreserveAllTokens(arguments, separator);
                int[] randomint = new int[partsRandomInt.length];

                try {
                    for (int i = 0; i < partsRandomInt.length; i++) {
                        randomint[i] = Integer.parseInt(arguments);
                    }
                } catch (NumberFormatException e) {
                    return "Only integer numbers are allowed.";
                }

                int min = Math.min(randomint[0], randomint[1]);
                int max = Math.max(randomint[0], randomint[1]);

                Random random = new Random();

                if (randomint.length == 3) {
                    random = new Random(randomint[2]);
                }
                return String.valueOf(random.nextInt((max - min + 1)) + min);
            }
            case "randomdouble" -> {
                arguments = "_" + arguments;
                if (!arguments.contains("_min:") || !arguments.contains("_max:")) {
                    return "Invalid arguments. Missing min or max.";
                }
                String regexDouble = "(_seed:|_round:|_min:|_max:)(.+?)(?=(_seed:|_round:|_min:|_max:|$))";

                Pattern patternDouble = Pattern.compile(regexDouble);
                Matcher matcherDouble = patternDouble.matcher(arguments);

                double minDouble = 0, maxDouble = 0;
                long seedDouble = 0;
                int round = 2;

                while (matcherDouble.find()) {
                    String key = matcherDouble.group(1);
                    String value = matcherDouble.group(2);

                    switch (key) {
                        case "_seed:":
                            seedDouble = Long.parseLong(value);
                            break;
                        case "_min:":
                            minDouble = Double.parseDouble(value);
                            break;
                        case "_max:":
                            maxDouble = Double.parseDouble(value);
                            break;
                        case "_round:":
                            round = Integer.parseInt(value);
                            break;
                    }
                }
                Random randomDouble = new Random();
                if (seedDouble != 0) {
                    randomDouble = new Random(seedDouble);
                }
                return String.format("%." + round + "f", randomDouble.nextDouble((maxDouble - minDouble)) + minDouble);
            }
            case "randomstring" -> {

                Pattern patternRandomString = Pattern.compile("^(\\d+)_(.*)$");  //If arguments starts with double, use double as seed
                Matcher matcherRandomString = patternRandomString.matcher(arguments);
                Random randomString = new Random();

                if (matcherRandomString.find()) {
                    arguments = matcherRandomString.group(2);
                    randomString = new Random(Long.parseLong(matcherRandomString.group(1)));
                }

                String[] splitRandom = StringUtils.splitByWholeSeparatorPreserveAllTokens(arguments, separator);
                return splitRandom[randomString.nextInt(splitRandom.length)];
            }
            case "weightedrandomstring" -> {

                Pattern patternWeightedRandomString = Pattern.compile("^(\\d+)_(.*)$");  //If arguments starts with double, use double as seed
                Matcher matcherWeightedRandomString = patternWeightedRandomString.matcher(arguments);
                Random randomWeightedString = new Random();

                if (matcherWeightedRandomString.find()) {
                    arguments = matcherWeightedRandomString.group(2);
                    randomWeightedString = new Random(Long.parseLong(matcherWeightedRandomString.group(1)));
                }

                String[] splitWeightedRandom = arguments.split(separator);
                List<String> splitWeightedRandom2 = new ArrayList<String>();

                for (int i = 0; i < splitWeightedRandom.length; i += 2) {
                    for (int j = 0; j < Integer.parseInt(splitWeightedRandom[i + 1]); j++) {
                        splitWeightedRandom2.add(splitWeightedRandom[i]);
                    }
                }
                return splitWeightedRandom2.get(randomWeightedString.nextInt(splitWeightedRandom2.size()));
            }
            case "changecolor" -> {
                String[] argsChangeColor = StringUtils.splitByWholeSeparatorPreserveAllTokens(arguments, separator);
                if (argsChangeColor.length < 2) {
                    return "Missing arguments!";
                }
                String input = argsChangeColor[0];
                String newColor = argsChangeColor[1];
                String regexColor = "(?i)\\b(WHITE|LIGHT_GRAY|GRAY|BLACK|BROWN|RED|ORANGE|YELLOW|LIME|GREEN|CYAN|LIGHT_BLUE|BLUE|PURPLE|MAGENTA|PINK)(_(\\w+))";
                if (input.matches(regexColor)) {
                    return input.replaceAll(regexColor, newColor + "$2");
                } else {
                    if (input.equalsIgnoreCase("GLASS")) input = "STAINED_" + input;
                    return newColor + "_" + input;
                }
            }
            case "changewood" -> {
                String[] argsChangeWood = StringUtils.splitByWholeSeparatorPreserveAllTokens(arguments, separator);
                if (argsChangeWood.length < 2) {
                    System.out.println("Missing arguments.");
                }

                String inputChangeWood = argsChangeWood[0];
                String newWood = argsChangeWood[1];

                if (newWood.equalsIgnoreCase("stripped") && !inputChangeWood.contains("stripped")) {
                    return "stripped_" + inputChangeWood;
                } else if (newWood.isBlank()) {
                    return inputChangeWood;
                } else {
                    Pattern patternChangeWood = Pattern.compile("(dark_oak_|oak_|jungle_|acacia_|birch_|spruce_|mangrove_|cherry_|bamboo_|warped_|crimson_)");
                    Matcher matcherChangeWood = patternChangeWood.matcher(inputChangeWood);
                    if (matcherChangeWood.find()) {
                        return matcherChangeWood.group(1) + newWood;
                    } else {
                        return inputChangeWood;
                    }
                }
            }
            case "replace" -> {
                String[] argsReplace = StringUtils.splitByWholeSeparatorPreserveAllTokens(arguments, separator);
                if (argsReplace.length < 3) {
                    return "Invalid arguments!";
                }

                if (argsReplace.length == 3) return argsReplace[0].replace(argsReplace[1], argsReplace[2]);
                else if (argsReplace[3].equalsIgnoreCase("upper") || argsReplace[3].equalsIgnoreCase("uppercase"))
                    return argsReplace[0].toUpperCase().replace(argsReplace[1].toUpperCase(), argsReplace[2].toUpperCase());
                else if (argsReplace[3].equalsIgnoreCase("lower") || argsReplace[3].equalsIgnoreCase("lowercase"))
                    return argsReplace[0].toLowerCase().replace(argsReplace[1].toLowerCase(), argsReplace[2].toLowerCase());

                return argsReplace[0].replace(argsReplace[1], argsReplace[2]);
            }
            case "multireplace" -> {
                String[] argsMultiReplace = StringUtils.splitByWholeSeparatorPreserveAllTokens(arguments, separator);
                if (argsMultiReplace.length < 3) {
                    return "Invalid arguments!";
                }
                String outputMultiReplace = argsMultiReplace[0];
                for (int i = 0; i < (argsMultiReplace.length - 1) / 2; i++) {
                    outputMultiReplace = outputMultiReplace.replaceAll(argsMultiReplace[1 + i * 2], argsMultiReplace[2 + i * 2]);
                }
                return outputMultiReplace;
            }
            case "executein" -> {
                String defaultOverworld = Bukkit.getWorlds().get(0).getName();
                String defaultNether = Bukkit.getWorlds().get(1).getName();
                String defaultEnd = Bukkit.getWorlds().get(2).getName();

                if (arguments.equals(defaultOverworld)) {
                    return "overworld";
                } else if (arguments.equals(defaultNether)) {
                    return "the_nether";
                } else if (arguments.equals(defaultEnd)) {
                    return "the_end";
                } else {
                    return arguments.toLowerCase();
                }
            }
            case "isgliding" -> {
                if (p == null) return "null player";

                if (p.isGliding()) {
                    return "yes";
                } else {
                    return "no";
                }
            }
            case "cursoritem" -> {
                String[] argsCursorItem = StringUtils.splitByWholeSeparatorPreserveAllTokens(arguments, separator);
                if (argsCursorItem == null) {
                    return String.valueOf(p.getItemOnCursor().getType());
                } else if (argsCursorItem[0].equals("amt") || argsCursorItem[0].equals("amount")) {
                    return String.valueOf(p.getItemOnCursor().getAmount());
                }

                return String.valueOf(p.getItemOnCursor().getType());
            }
            case "inventoryinfo" -> {
                //Requires: Slot, info type
                String[] inventoryInfoArgs = StringUtils.splitByWholeSeparatorPreserveAllTokens(arguments, separator);

                if (inventoryInfoArgs == null || inventoryInfoArgs.length < 2) return "Missing arguments";
                if (p == null) return "Invalid Player";

                ItemStack itemStack = Utils.getInvItem(p, inventoryInfoArgs[0]);
                if (itemStack == null) return "";
                ItemMeta itemMeta = itemStack.getItemMeta();

                String infoType = inventoryInfoArgs[1];

                switch (infoType) {
                    case "armortrim", "trim" -> {
                        if (itemMeta instanceof ArmorMeta armorMeta) {
                            if (armorMeta.hasTrim())
                                return armorMeta.getTrim().getPattern().getKey().getKey();
                        }
                        return "";
                    }
                    case "material", "mat" -> {
                        return itemStack.getType().toString();
                    }
                    case "amount", "amt" -> {
                        return String.valueOf(itemStack.getAmount());
                    }
                    case "enchantlevel", "enchantlvl", "enchantmentlvl", "enchantmentlevel" -> {
                        if (inventoryInfoArgs.length < 3) return "";
                        String enchantName = inventoryInfoArgs[2];
                        NamespacedKey key = NamespacedKey.fromString(enchantName);
                        if (key == null) return "Invalid Enchant Name";
                        Enchantment enchant = Registry.ENCHANTMENT.get(key);
                        if (enchant == null) return "Invalid Enchant Name";
                        return String.valueOf(itemStack.getEnchantmentLevel(enchant));
                    }
                    default -> {
                        return "Invalid infotype";
                    }
                }
            }
            case "slottovanilla" -> {

                int slot = Integer.parseInt(arguments);
                //Converts bukkit slot numbers to vanilla slot text
                if (slot < 9) {
                    return "hotbar." + slot;
                } else
                    return "inventory." + (slot - 9);
            }
            case "blockat" -> {
                String[] blockatargs = StringUtils.splitByWholeSeparatorPreserveAllTokens(arguments, separator);
                if (blockatargs.length < 4) return "Invalid arguments";
                return String.valueOf(Bukkit.getWorld(blockatargs[3]).getBlockAt(Integer.parseInt(blockatargs[0]), Integer.parseInt(blockatargs[1]), Integer.parseInt(blockatargs[2])).getType());
            }
            case "isblocknatural" -> {
                String[] isBlockNaturalArgs = StringUtils.splitByWholeSeparatorPreserveAllTokens(arguments, separator);
                Block isBlockNaturalBlock = Bukkit.getWorld(isBlockNaturalArgs[3]).getBlockAt(Integer.parseInt(isBlockNaturalArgs[0]), Integer.valueOf(isBlockNaturalArgs[1]), Integer.valueOf(isBlockNaturalArgs[2]));
                return String.valueOf(Utils.isNaturallyGenerated(isBlockNaturalBlock));
            }
            case "weightedrandom" -> {
                String[] weightedRandomArgs = StringUtils.splitByWholeSeparatorPreserveAllTokens(arguments, separator);

                int totalWeight = 0;
                List<String> items = new ArrayList<>();
                List<Integer> numbers = new ArrayList<>();
                for (int i = 0; i < weightedRandomArgs.length; i++) {
                    if (i % 2 == 0) {
                        items.add(weightedRandomArgs[i]);
                    } else {
                        numbers.add(totalWeight + Integer.parseInt(weightedRandomArgs[i]));
                        totalWeight += Integer.parseInt(weightedRandomArgs[i]);
                    }
                }
                int randomweightedrandom = ThreadLocalRandom.current().nextInt(1, totalWeight);

                for (int i = 0; i < numbers.size(); i++) {
                    if (randomweightedrandom <= numbers.get(i)) {
                        return items.get(i);
                    }
                }
            }
            case "armorset" -> {
                String[] armorSetArgs = StringUtils.splitByWholeSeparatorPreserveAllTokens(arguments, separator);
                if (armorSetArgs == null) return "Invalid args.";

                String armorSetID = armorSetArgs[0];

                if (p == null || !p.isOnline()) return "0";

                int amount = 0;

                for (ItemStack armor : p.getInventory().getArmorContents()) {
                    if (armor != null && armor.hasItemMeta()) {
                        String armorID = armor.getItemMeta().getPersistentDataContainer().get(CommandUtils.keyEIID, PersistentDataType.STRING);
                        if (armorID != null && armorID.contains(armorSetID)) amount++;
                    }
                }

                return String.valueOf(amount);
            }
            case "worldenvironment" -> {
                return p.getWorld().getEnvironment().toString();
            }
            case "if" -> {
                String[] inputSplit = arguments.split(elseIfKeyword);
                String[] elseSplit = inputSplit[inputSplit.length - 1].split(elseKeyword);

                String[] combinedSplit = ArrayUtils.addAll(inputSplit, elseSplit);

                //If and Else If's
                for (int i = 0; i <= combinedSplit.length; i++) {
                    String[] argSplit = combinedSplit[i].split(conditionSeparator, 3);
                    if (argSplit[1].contains("=")) {
                        String[] condition = argSplit[1].split("=", 2);
                        if (Objects.equals(condition[0], condition[1])) {
                            return argSplit[2];
                        }
                    } else if (argSplit[1].contains("!=")) {
                        String[] condition = argSplit[1].split("!=", 2);
                        if (!Objects.equals(condition[0], condition[1])) {
                            return argSplit[2];
                        }
                    } else if (argSplit[1].contains(">")) {
                        String[] condition = argSplit[1].split(">", 2);
                        if (NumberUtils.isCreatable(condition[0]) && NumberUtils.isCreatable(condition[1]) && (Double.parseDouble(condition[0]) > Double.parseDouble(condition[1]))) {
                            return argSplit[2];
                        }
                    } else if (argSplit[1].contains("<")) {
                        String[] condition = argSplit[1].split("<", 2);
                        if (NumberUtils.isCreatable(condition[0]) && NumberUtils.isCreatable(condition[1]) && (Double.parseDouble(condition[0]) < Double.parseDouble(condition[1]))) {
                            return argSplit[2];
                        }
                    } else if (argSplit[1].contains(">=")) {
                        String[] condition = argSplit[1].split(">=", 2);
                        if (NumberUtils.isCreatable(condition[0]) && NumberUtils.isCreatable(condition[1]) && (Double.parseDouble(condition[0]) >= Double.parseDouble(condition[1]))) {
                            return argSplit[2];
                        }
                    } else if (argSplit[1].contains("<=")) {
                        String[] condition = argSplit[1].split("<=", 2);
                        if (NumberUtils.isCreatable(condition[0]) && NumberUtils.isCreatable(condition[1]) && (Double.parseDouble(condition[0]) <= Double.parseDouble(condition[1]))) {
                            return argSplit[2];
                        }
                    }
                }

                //Else
                return combinedSplit[combinedSplit.length - 1];
            }
            case "material", "mat" -> {

                if (p == null) {
                    return "null player";
                }

                ItemStack item = Utils.getInvItem(p, arguments);

                if (item == null) {
                    return "AIR";
                }

                return item.getType().toString();
            }
            case "nbt" -> {
                String[] argsNbt = arguments.split(nbtSeparator);
                if (p == null) {
                    return "Null player";
                }

                if (argsNbt.length < 3) {
                    return "Missing arguments";
                }

                ItemStack item = Utils.getInvItem(p, argsNbt[0]);

                if (item == null || !item.hasItemMeta()) return "";

                PersistentDataContainer container = item.getItemMeta().getPersistentDataContainer();
                NamespacedKey key = new NamespacedKey(argsNbt[1], argsNbt[2]);

                if (!container.has(key)) return "";

                try {
                    return item.getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.STRING);
                } catch (IllegalArgumentException e) {
                    return String.valueOf(item.getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.DOUBLE));
                }
            }

            /*
             * Gets how many of a material is in a player's inventory + cursor slot. Does not count opened containers.
             * @author DuneSciFye
             */
            case "amount", "amt" -> {
                if (p == null) {
                    return "null player";
                }

                ItemStack item = Utils.getInvItem(p, arguments);

                if (item == null) { //Argument is not an inv slot
                    Material mat = Material.getMaterial(arguments.toUpperCase());
                    if (mat == null) return "";
                    int count = 0;
                    ArrayList<ItemStack> items = new ArrayList<>(Arrays.asList(p.getInventory().getContents()));
                    items.add(p.getItemOnCursor());
                    for (ItemStack content : items)
                        if (content != null && Objects.equals(content.getType(), mat))
                            count += content.getAmount();

                    return String.valueOf(count);
                }

                return String.valueOf(item.getAmount()); //Argument was an inv slot
            }
            /*
             * Get distance between two coordinates
             * @author DuneSciFye
             */
            case "distance" -> {
                String[] distanceArgs = arguments.split("_", 2);

                if (distanceArgs.length != 2) return "Missing arguments.";

                String[] coords1 = distanceArgs[0].split(",", 3);
                String[] coords2 = distanceArgs[1].split(",", 3);

                if (coords1.length != 3 && coords2.length != 3) return "Missing arguments.";

                double[] num1;
                double[] num2;

                try {
                    num1 = Arrays.stream(coords1).mapToDouble(Double::parseDouble).toArray();
                    num2 = Arrays.stream(coords2).mapToDouble(Double::parseDouble).toArray();
                } catch (NumberFormatException e) {
                    return "Invalid Number for Coordinates provided";
                }

                World world = Bukkit.getWorlds().getFirst();

                Location loc1 = new Location(world, num1[0], num1[1], num1[2]);
                Location loc2 = new Location(world, num2[0], num2[1], num2[2]);
                double distance = loc1.distance(loc2);
                return String.valueOf(distance);

            }
            /*
             * Get value of temporary variable
             * @author DuneSciFye
             * @since 2.0.0
             * @param Variable Name of Variable
             */
            case "variable", "var", "tempvar", "tempvariable" -> {
                return TempVarCommand.getVar(arguments);
            }
            /*
             * Get value of temporary player variable
             * @author DuneSciFye
             * @since 2.1.5
             * @param Variable Name of Player Variable
             */
            case "playervariable", "playervar", "playertempvar", "playertempvariable", "pvar" -> {
                return TempPlayerVarCommand.getPlayerVar(p, arguments);
            }
            case "variabledefault", "vardefault" -> {
                String[] varParts = arguments.split("_", 2);
                String var = TempVarCommand.getVar(varParts[1]);
                return var.isEmpty() ? varParts[0] : var;
            }
            /*
             * Get block relative to player eyesight
             * @author DuneSciFye
             * @since 2.0.1
             * @param Double Distance to go
             * @param Function Data to get, allowed: coordinates, coord, coords, mat, material
             * @param StopAtBlock Optional; Should it give first block encountered
             */
            case "raytrace" -> {
                String[] rayTraceArgs = StringUtils.splitByWholeSeparatorPreserveAllTokens(arguments, separator);
                if (p == null || rayTraceArgs.length < 2 || !NumberUtils.isCreatable(rayTraceArgs[0])) return null;

                Block b;

                if (rayTraceArgs.length > 2 && rayTraceArgs[2].equalsIgnoreCase("true")) {
                    RayTraceResult result = p.rayTraceBlocks(Double.parseDouble(rayTraceArgs[0]));
                    if (result == null || result.getHitBlock() == null) return "AIR";
                    b = result.getHitBlock();
                } else {
                    b = p.getEyeLocation().add(p.getEyeLocation().getDirection().multiply(Double.parseDouble(rayTraceArgs[0]))).getBlock();
                }

                switch (rayTraceArgs[1]) {
                    case
                        "coordinates",
                        "coord",
                        "coords" -> {
                        return b.getX() + " " + b.getY() + " " + b.getZ();
                    }
                    case
                        "material",
                        "mat" -> {
                        return b.getType().toString();
                    }
                    case "x" -> {
                        return String.valueOf(b.getX());
                    }
                    case "y" -> {
                        return String.valueOf(b.getY());
                    }
                    case "z" -> {
                        return String.valueOf(b.getZ());
                    }
                }
            }
            /*
             * Returns a random potion effect from input list that player does not already have
             * @author DuneSciFye
             * @since 2.1.1
             * @param Effects List of potion effects to check for separated by space
             * @param Level Minimum level of potion effect to check for
             */
            case "randomnewpotioneffect" -> {
                String[] params = StringUtils.splitByWholeSeparatorPreserveAllTokens(arguments, separator);
                if (p == null || params.length < 1) return null;

                int minLevel = params.length > 1 && Utils.isInteger(params[1]) ? Integer.parseInt(params[1]) - 1 : 0; //Zero based

                ArrayList<String> effects = new ArrayList<>(List.of(params[0].split(" ")));
                Collections.shuffle(effects);
                for (String effect : effects) {
                    NamespacedKey key = NamespacedKey.fromString(effect);
                    if (key == null) continue;
                    PotionEffectType type = Registry.POTION_EFFECT_TYPE.get(key);
                    if (type == null) continue;
                    PotionEffect potionEffect = p.getPotionEffect(type);
                    if (potionEffect == null || potionEffect.getAmplifier() < minLevel) return effect;
                }
                return "";
            }
            /*
             * Returns the level of a potion effect. 0 based.
             * @author DuneSciFye
             * @since 2.1.1
             * @param Potion Effect
             */
            case "potioneffectlevel" -> {
                String[] params = StringUtils.splitByWholeSeparatorPreserveAllTokens(arguments, separator);
                if (p == null || params.length < 1) return null;

                NamespacedKey key = NamespacedKey.fromString(params[0]);
                if (key == null) return "";
                PotionEffectType type = Registry.POTION_EFFECT_TYPE.get(key);
                if (type == null) return "";
                PotionEffect potionEffect = p.getPotionEffect(type);
                if (potionEffect == null) return "";
                return String.valueOf(potionEffect.getAmplifier());

            }
            case "exists", "alive" -> {
                String[] params = StringUtils.splitByWholeSeparatorPreserveAllTokens(arguments, separator);
                if (params.length < 1) return null;
                try {
                    UUID uuid = UUID.fromString(params[0]);
                    return Bukkit.getEntity(uuid) == null ? "false" : "true";
                } catch (IllegalArgumentException e) {
                    return "Invalid UUID";
                }
            }
            case "inground", "inblock", "arrowinground", "arrowinblock" -> {
                String[] params = StringUtils.splitByWholeSeparatorPreserveAllTokens(arguments, separator);
                if (params.length < 1) return null;
                try {
                    UUID uuid = UUID.fromString(params[0]);
                    Entity e = Bukkit.getEntity(uuid);
                    if (!(e instanceof Arrow arrow)) return "Invalid Arrow";
                    return String.valueOf(!arrow.isInBlock());
                } catch (IllegalArgumentException e) {
                    return "Invalid UUID";
                }
            }
            case "isinlava", "inlava" -> {
                String[] params = StringUtils.splitByWholeSeparatorPreserveAllTokens(arguments, separator);
                if (params.length < 1) return null;
                try {
                    UUID uuid = UUID.fromString(params[0]);
                    Entity e = Bukkit.getEntity(uuid);
                    if (e == null) return "Invalid Entity";
                    return String.valueOf(e.isInLava());
                } catch (IllegalArgumentException e) {
                    return "Invalid UUID";
                }
            }
            case "entityinfo" -> {
                String[] params = StringUtils.splitByWholeSeparatorPreserveAllTokens(arguments, separator);
                if (params.length < 2) return null;
                try {
                    UUID uuid = UUID.fromString(params[0]);
                    Entity e = Bukkit.getEntity(uuid);
                    if (e == null) return "Invalid Entity";
                    switch (params[1]) {
                        case "x" -> {
                            return String.valueOf(e.getLocation().getX());
                        }
                        case "y" -> {
                            return String.valueOf(e.getLocation().getY());
                        }
                        case "z" -> {
                            return String.valueOf(e.getLocation().getZ());
                        }
                        case "xint" -> {
                            return String.valueOf(e.getLocation().getBlockX());
                        }
                        case "yint" -> {
                            return String.valueOf(e.getLocation().getBlockY());
                        }
                        case "zint" -> {
                            return String.valueOf(e.getLocation().getBlockZ());
                        }
                    }
                } catch (IllegalArgumentException e) {
                    return "Invalid UUID";
                }
            }
            case "hasai" -> {
                String[] params = StringUtils.splitByWholeSeparatorPreserveAllTokens(arguments, separator);
                if (params.length < 1) return null;
                try {
                    UUID uuid = UUID.fromString(params[0]);
                    Entity e = Bukkit.getEntity(uuid);
                    if (!(e instanceof LivingEntity entity)) return "Invalid Entity";
                    return String.valueOf(entity.hasAI());
                } catch (IllegalArgumentException e) {
                    return "Invalid UUID";
                }
            }
            default -> {
                return "Unknown function";
            }
        }
        return null;
    }
}
