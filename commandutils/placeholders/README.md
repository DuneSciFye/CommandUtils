---
icon: percent
description: PlaceholderAPI expansions provided by CommandUtils
---

# Placeholders

With [PlaceholderAPI](https://www.spigotmc.org/resources/placeholderapi.6245/) installed, CommandUtils registers three expansions. They are toggled and tuned under the `Placeholders` section of [config.yml](../config.md).

| Identifier | Purpose |
| --- | --- |
| `%stringutils_...%` | Text, randomness, conditions, item and block queries, variables |
| `%blockutils_...%` | Blocks relative to a coordinate |
| `%playerutils_...%` | Player state |

## Syntax

```
%stringutils_<function>_<arg1>,<arg2>,<arg3>%
```

* Arguments are separated by the configured `ArgumentSeparator`, `,` by default.
* With `AllowCustomSeparator` on, a placeholder can name its own separator by putting it before the function: `%stringutils_;_replace_a;b;c%`. Use this when your arguments contain commas.
* Ending the arguments with `,lower` or `,upper` converts the result's case.
* Bracket placeholders `{...}` inside the arguments are resolved first, so placeholders can be nested.

Test any placeholder in-game with [Parse Placeholder](../commands/parse-placeholder.md).

## StringUtils

### Randomness

| Placeholder | Returns |
| --- | --- |
| `randomint_<min>,<max>[,<seed>]` | A whole number in the range, inclusive |
| `randomdouble_min:<n>_max:<n>[_round:<n>][_seed:<n>]` | A decimal, rounded to `round` places (default 2) |
| `randomstring_[<seed>_]<a>,<b>,<c>` | One of the values, each equally likely |
| `weightedrandomstring_[<seed>_]<a>,<weight>,<b>,<weight>` | One value, chosen by weight |

```
%stringutils_randomint_1,100%
%stringutils_weightedrandomstring_common,90,rare,9,legendary,1%
```

### Text

| Placeholder | Returns |
| --- | --- |
| `replace_<input>,<find>,<to>[,upper\|lower]` | The input with `find` swapped for `to` |
| `multireplace_<input>,<find>,<to>,<find>,<to>...` | Several replacements in order |
| `replaceregex_<input>,<regex>,<to>` | Regex replacement. Alias `regexreplace` |
| `inputoutput_<input>,<case>,<result>,<case>,<result>[,<default>]` | The result matching the input |
| `inputoutputcycle_<input>,<a>,<b>,<c>` | The value after the one that matched, wrapping around |
| `changecolor_<block>,<color>` | The same block in another colour, e.g. `red_wool` → `blue_wool` |
| `changewood_<block>,<wood>` | The same block in another wood. `stripped` prefixes instead |
| `smelt_<material>` | The furnace result of the material |
| `croptoblock_<seed>` | The block a seed item plants |

```
%stringutils_inputoutput_%player_world%,world,Overworld,world_nether,Nether,Elsewhere%
%stringutils_changewood_oak_planks,spruce%
```

### Conditions

| Placeholder | Returns |
| --- | --- |
| `if_"<condition>" <output> elseif "<condition>" <output> else <output>` | The first matching output |

Uses the same operators as the [If](../commands/if.md) command.

### Items

`inventoryinfo_<slot>,<info>` — aliases `invinfo`, `iteminfo`. The [slot](../arguments/slot-argument.md) may be a number or a keyword.

| Info | Returns |
| --- | --- |
| `material` / `mat` | The item type |
| `amount` / `amt` | Stack size |
| `isvanilla` | Whether the item has no custom data |
| `enchantlevel,<enchantment>` | The level of that enchantment, `0` if absent |
| `potiontype` | Base potion type |
| `hascustomeffects` | Whether a potion has custom effects |
| `custommodeldata` / `cmdata` | Custom model data |
| `armortrim` / `trim` | Trim pattern |
| `color` / `colorrgb` / `colorargb` / `colorbgr` | Leather armour colour |
| `flightduration` | Firework power |
| `lore` | The lore, newline separated, with `&` colour codes |
| `skullowner` / `skullowneruuid` | Player head owner |
| `dumpitem` | The full component string, for debugging |

```
%stringutils_inventoryinfo_mainhand,material%
%stringutils_invinfo_mainhand,enchantlevel,minecraft:efficiency%
```

Also: `material_<slot>` for the held material, `nbt_...`, `amount_...`, `cursoritem[_amount]`, `slottovanilla_<slot>`, `armorset`, `armorsetlowestlevel`.

### Blocks

| Placeholder | Returns |
| --- | --- |
| `blockat_<x>,<y>,<z>,<world>` | The material at those coordinates |
| `blockinfo_<uuid>[,<dx>,<dy>,<dz>],<info>` | Info about a block near an entity: `material`, `shape`, `fullygrown`, `canbonemeal`, `attachedtostem` |
| `getrelative_<x>,<y>,<z>,<sep>,<direction>...` | Coordinates after stepping in one or more directions. Alias `getblockrelative` |
| `isblocknatural_<x>,<y>,<z>,<world>` | Whether the block was naturally generated |

### Entities

| Placeholder | Returns |
| --- | --- |
| `entityinfo_<uuid>,<info>` | `x`, `y`, `z`, `xint`, `yint`, `zint`, `owner`, `damage`, `health`, `armor`, `data`, `hasai`, `isinvulnerable` |
| `exists_<uuid>` | Whether the entity is still loaded. Alias `alive` |
| `inground_<uuid>` | Whether an arrow has landed |
| `isinlava_<uuid>` | Whether the entity is in lava |
| `distance_<from>,<to>` | Distance between two entities or coordinates |
| `isdisguised_<uuid>` | Whether LibsDisguises has it disguised |
| `villagerprofession_<uuid>` | The villager's profession |

### Player state

`isgliding`, `isblocking`, `isfrozen`, `worldenvironment` (alias `dimension`), `facing`, `raytrace_<distance>[,...]`, `potioneffectlevel_<effect>`, `potioneffectduration_<effect>`, `lastrawdamage`, `lastfinaldamage`, `lastrawdamagedealt`, `lastfinaldamagedealt`, `lastattacker`, `lastbowforce`, `expreason`.

`lastattacker` (aliases `lastplayerattacker`, `lastdamager`, `lastplayerdamager`) is the name of the player who last damaged you. It is blank if you have not been damaged, or if your most recent damage came from anything other than a player.

### Land protection

`isinclaim`, `isinwilderness`, `inclaimorwilderness`, `worldguardregions`, `notinregion`.

### Variables

| Placeholder | Returns |
| --- | --- |
| `variable_<name>` | A [Temp Var](../commands/temp-var.md). Aliases `var`, `tempvar` |
| `playervariable_<name>` | A [Temp Player Var](../commands/temp-player-var.md) for the current player. Aliases `playervar`, `pvar` |
| `vardefault_<default>_<name>` | The variable, or the default if it is unset |
| `pvardefault_<default>_<name>` | The same, for a player variable |

## BlockUtils

```
%blockutils_<function>_<world>,<x>,<y>,<z>,<BlockFace>,<amount>,<info>%
```

* **getrelative** — steps `amount` blocks in the given direction and returns `material` / `mat`, `coords` / `coord`, `x`, `y` or `z`.
* **getrelativeonlyair** — the same, for when the target is expected to be air.

```
%blockutils_getrelative_world,100,64,-30,UP,1,material%
```

## PlayerUtils

| Group | Functions |
| --- | --- |
| Movement | `velocity` (alias `speed`), `falldistance`, `isflying`, `issprinting`, `iscrouching`, `vehicle`, `vehicleuuid` |
| World and time | `playertime`, `ptimeisday`, `ptimeisnight`, `isthundering`, `israining`, `nearestbiome` |
| Targeting | `facing`, `getblock`, `raytrace`, `nearentity`, `relational` |
| Misc | `xplevel`, `isblocking`, `hasnbtitem`, `vanillascale` |

```
%playerutils_facing%
%playerutils_nearestbiome_plains%
```
