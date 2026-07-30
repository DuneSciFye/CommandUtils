---
icon: hand-wave
description: A Paper plugin of building blocks for custom items, tools and abilities
layout:
  width: default
  title:
    visible: true
  description:
    visible: true
  tableOfContents:
    visible: true
  outline:
    visible: true
  pagination:
    visible: true
---

# Welcome

**CommandUtils** adds around 130 commands that fill the gaps between vanilla commands and what a custom item, block or ability actually needs — breaking a 3×3 of blocks, running commands on a timer, stealing a potion effect, giving a player a fake health bar.

Every command works from command blocks, datapacks, the console, and plugins such as ExecutableItems and ExecutableBlocks. Nothing here needs a Java plugin of your own.

### Jump right in

<table data-view="cards"><thead><tr><th></th><th></th><th data-hidden data-card-target data-type="content-ref"></th></tr></thead><tbody><tr><td><strong>Quickstart</strong></td><td>Install the plugin and run your first command</td><td><a href="getting-started/quickstart.md">quickstart.md</a></td></tr><tr><td><strong>Commands</strong></td><td>The full command reference, by category</td><td><a href="commandutils/commands/">commands</a></td></tr><tr><td><strong>Config</strong></td><td>Enable, rename and tune commands</td><td><a href="commandutils/config.md">config.md</a></td></tr></tbody></table>

### What it's for

| You want | Use |
| --- | --- |
| A hammer that breaks a 3×3 | [Break In Facing](commandutils/commands/break-in-facing.md) |
| A vein-mining pickaxe | [Break In Vein](commandutils/commands/break-in-vein.md) |
| A harvester hoe that replants | [Select Blocks](commandutils/commands/select-blocks.md) |
| An ability on a cooldown | [Cooldown Command](commandutils/commands/cooldown-command.md) |
| A channelled effect over several seconds | [Loop](commandutils/commands/loop.md) |
| A weighted loot table | [Weighted Random](commandutils/commands/weighted-random.md) |
| Buffs that don't overwrite each other | [Effect](commandutils/commands/effect.md) |
| Values read back into other plugins | [Placeholders](commandutils/placeholders/) |

### Requirements

* **Paper** 1.20 or newer, Java 21
* No hard dependencies. A handful of commands need an extra plugin and are simply not registered without it — [PlaceholderAPI](https://www.spigotmc.org/resources/placeholderapi.6245/), [CoreProtect](https://www.spigotmc.org/resources/coreprotect.8631/) and [ProtocolLib](https://www.spigotmc.org/resources/protocollib.1997/) are each used by one or two
* Claim and region protection is respected automatically when GriefPrevention, Factions or WorldGuard is installed
