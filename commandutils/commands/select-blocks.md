---
description: Runs a list of functions on every matching block in a cube
---

# Select Blocks

Usage: /selectblocks \<[World](../arguments/world-argument.md)> \<[Location](../arguments/location-argument.md)> \<Player> \<Radius> \<[Whitelisted Blocks](../arguments/whitelisted-blocks.md)> \<Command Separator> \<Placeholder Surrounder> \<Custom Placeholders> \<Functions>

* World - The world the location is in
* Location - Coordinates of the centre block
* Player - The player the actions run as. Their held item decides drops, and their claim permissions are checked
* Radius - Cube radius. `1` is 3×3×3, `2` is 5×5×5
* Whitelisted Blocks - Which blocks are selected
* Command Separator - String that separates the functions, e.g. `;`
* Placeholder Surrounder - Character used in place of `%` in the function list, so nested placeholders survive being passed through other plugins. Use `""` to disable placeholder handling entirely
* Custom Placeholders - Kept for compatibility; placeholder handling is driven by `Placeholder Surrounder`
* Functions - The functions to run on each block, in order

This is the toolkit behind custom mining tools: instead of one fixed behaviour, you list the steps to run on every selected block.

{% hint style="info" %}
`Command Separator` is treated as a regular expression. Prefer plain separators like `;` or `,,` over `|`.
{% endhint %}

## Functions

Functions run **in order**, on each block, with their own drop collection.

| Function | Effect |
| --- | --- |
| `BLOCK:BREAK` | Breaks the block and collects its normal drops |
| `BLOCK:SILK_TOUCH` | Breaks the block and collects the block itself. Handles two-block-tall blocks |
| `BLOCK:REMOVE` | Deletes the block, no drops |
| `BLOCK:AUTO_REPLANT` | Harvests an Ageable crop, resets it to age 0 and pays one seed out of the drops |
| `BLOCK:BONE_MEAL` | Applies bone meal to the block |
| `BLOCK:WAX` | Converts the block to its waxed variant, if one exists |
| `BLOCK:VEIN_MINE` | Breaks the connected vein of the same block, up to 160 blocks |
| `BLOCK:TRIGGER_BLOCK_BREAK` | Fires a `BlockBreakEvent` so other plugins can react — and cancel |
| `BLOCK:CONDITION:FULLY_GROWN` | Stops processing this block unless it is a fully grown crop |
| `BLOCK:CONDITION:NOT_CANCELLED` | Stops processing this block if an earlier `BLOCK:TRIGGER_BLOCK_BREAK` was cancelled |
| `ITEM:DUPLICATE` | Doubles everything collected so far |
| `ITEM:SMELT` | Replaces the collected drops with their furnace results |
| `ITEM:AUTO_PICKUP` | Puts the drops in the player's inventory, keeping only the overflow |
| `ITEM:DROP` | Drops what's collected at the centre block |
| `ITEM:DROP <player>` | Drops at that player, by name or UUID |
| `ITEM:DROP <x> <y> <z>` | Drops at those coordinates |
| `ITEM:DROP <world> <x> <y> <z>` | Drops at those coordinates in that world |

Anything else in the list is run as a console command. These placeholders are filled in first:

| Placeholder | Value |
| --- | --- |
| `%block_x%` `%block_y%` `%block_z%` | Coordinates of the current block |
| `%block%` | Material of the current block |
| `%crop%` | The crop item matching the current block |
| `%item%` / `%item_lower%` | Material of the first collected drop |

PlaceholderAPI placeholders are parsed against the player afterwards.

{% hint style="warning" %}
Nothing drops unless the list ends with an `ITEM:` function. `BLOCK:BREAK` on its own collects drops and then throws them away.
{% endhint %}

### Examples

Break every log in a 3×3×3 and put the drops straight in the player's inventory:

```
/selectblocks world 100 64 -30 @p 1 "#logs" ; % true BLOCK:BREAK;ITEM:AUTO_PICKUP;ITEM:DROP
```

A harvester hoe — only fully grown crops, replanted, drops auto-picked up:

```
/selectblocks %world% %block_x% %block_y% %block_z% %player_name% 2 "#crops" ; % true BLOCK:CONDITION:FULLY_GROWN;BLOCK:AUTO_REPLANT;ITEM:AUTO_PICKUP;ITEM:DROP
```

Respect other plugins' protections, smelt the ore, then drop it:

```
/selectblocks world 100 32 -30 @p 1 "#iron_ores" ; % true BLOCK:TRIGGER_BLOCK_BREAK;BLOCK:CONDITION:NOT_CANCELLED;BLOCK:BREAK;ITEM:SMELT;ITEM:DROP
```

Run a command for each matching block:

```
/selectblocks world 100 64 -30 @p 2 diamond_ore ; % true spawnnodamagelightning world %block_x% %block_y% %block_z%
```
