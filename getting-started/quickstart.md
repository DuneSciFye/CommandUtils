---
icon: bullseye-arrow
description: Install CommandUtils and build your first custom tool
---

# Quickstart

## Install

1. Drop `CommandUtils.jar` into `plugins/`.
2. Start the server. `plugins/CommandUtils/config.yml` is generated on first run.
3. Check it loaded with a command that reports something back:

```
/parseplaceholder me %player_name%
```

Every command is also reachable as `commandutils:<command>` if another plugin registers the same name.

## Run a command

Commands take arguments in a fixed order, shown at the top of each page:

```
/breakinradius <World> <Location> <Player> <Radius>
```

* `<arrows>` are required, `[<brackets>]` are optional
* A page with several **Usage** lines has several valid forms

So this clears a 3×3×3 cube, crediting a player so their claim permissions are respected:

```
/breakinradius world 100 64 -30 Steve 1
```

## Build a tool

Custom items are made by handing a command to whatever plugin fires it. With ExecutableItems, this is a hammer that breaks a 3×3 of anything a pickaxe can mine:

```yaml
Activators:
  break:
    Type: PLAYER_BLOCK_BREAK
    Commands:
      - "CONSOLE breakinfacing %world% %block_x% %block_y% %block_z% %player% 1 1 pickaxe"
```

Two things are doing the work:

* `pickaxe` is a **whitelist** defined in [config.yml](../commandutils/config.md), so the hammer never breaks chests or spawners. See [Whitelisted Blocks](../commandutils/arguments/whitelisted-blocks.md).
* `%player%` makes the break belong to that player, so claims are checked and their held item decides the drops.

## Add behaviour

[Select Blocks](../commandutils/commands/select-blocks.md) replaces one fixed behaviour with a list of steps, which is how most custom tools end up written:

```
/selectblocks %world% %block_x% %block_y% %block_z% %player% 1 pickaxe ; % true BLOCK:BREAK;ITEM:SMELT;ITEM:AUTO_PICKUP;ITEM:DROP
```

That mines a 3×3×3, smelts what it collects, and puts it straight in the player's inventory.

## Where to go next

* [Commands](../commandutils/commands/) — the full reference, grouped by what it does
* [Arguments](../commandutils/arguments/) — worlds, locations, slots, whitelists and durations, explained once
* [Config](../commandutils/config.md) — enable, disable, rename and tune commands
* [Placeholders](../commandutils/placeholders/) — read plugin state back into your commands
