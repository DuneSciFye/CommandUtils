---
icon: brackets-curly
description: Argument types shared by many CommandUtils commands
---

# Arguments

Several argument types show up across the whole command set. They are documented once here and linked from each command page.

| Argument | Accepts |
| --- | --- |
| [World](world-argument.md) | The name of a Bukkit world, e.g. `world`, `world_nether` |
| [Location](location-argument.md) | Decimal coordinates, `~` and `^` supported |
| [Block Location](block-location-argument.md) | Whole-block coordinates |
| [Slot](slot-argument.md) | A slot number `0`–`40`, or a keyword such as `mainhand` |
| [Whitelisted Blocks](whitelisted-blocks.md) | A config whitelist name, or an inline list of materials and tags |
| [NamespacedKeys](namespacedkeys.md) | The `Namespace` + `Key` pair used by the item NBT commands |

## Duration

Anywhere a **Duration**, **Time**, **Period**, **Initial Delay** or **Max Time** is asked for, the value is a number plus a unit:

| Suffix | Unit | Example |
| --- | --- | --- |
| _(none)_ | Ticks | `100` |
| `t` | Ticks | `100t` |
| `s`, `sec`, `seconds` | Seconds | `30s` |
| `m`, `min`, `minutes` | Minutes | `5m` |
| `h`, `hr`, `hours` | Hours | `1h` |
| `d`, `day`, `days` | Days | `2d` |

Units can be combined and decimals are allowed — `1m30s`, `2.5s`.

## Entity selectors

Arguments named **Player**, **Players** or **Entities** accept a name or a vanilla selector (`@p`, `@a`, `@e[type=zombie,distance=..10]`). **Player** must resolve to exactly one player; **Players** and **Entities** accept any number.
