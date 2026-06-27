---
icon: percent
description: PlaceholderAPI expansions provided by CommandUtils
---

# Placeholders

When [PlaceholderAPI](https://www.spigotmc.org/resources/placeholderapi.6245/) is installed, CommandUtils registers three expansions. They can be toggled and tuned under the `Placeholders` section of [config.yml](../config.md).

| Identifier | Purpose |
| --- | --- |
| `%stringutils_...%` | String manipulation, conditionals, item/block queries, and temporary variables |
| `%blockutils_...%` | Querying blocks relative to a position |
| `%playerutils_...%` | Querying player data and state |

{% hint style="info" %}
Arguments are separated by the configured `ArgumentSeparator` (default `,`). With `AllowCustomSeparator` enabled, a placeholder may specify its own separator.
{% endhint %}

## StringUtils — `%stringutils_...%`

The largest expansion. Highlights:

* **Conditionals** — `if` with `elseif` / `else` keywords and a configurable condition separator.
* **Randomness** — `randomint`, `randomdouble`, `randomstring`, `weightedrandomstring`, `weightedrandom`.
* **Text manipulation** — `replace`, `multireplace`, `replaceregex` (alias `regexreplace`), `changecolor`, `changewood`, `inputoutput`, `inputoutputcycle`.
* **Item queries** — `inventoryinfo` (aliases `invinfo`, `iteminfo`): `material`, `amount`, `enchantlevel`, `potiontype`, `custommodeldata`, `armortrim`, `color`, `lore`, `skullowner`, `flightduration`, `dumpitem`, and more.
* **Block queries** — `blockinfo`, `blockat`, `getblockrelative` (alias `getrelative`), `isblocknatural`, `material`, `nbt`, `distance`, `worldenvironment` (alias `dimension`).
* **Player state** — `isgliding`, `isblocking`, `isfrozen`, `cursoritem`.
* **Variables** — `variable` (aliases `var`, `tempvar`), `playervariable` (aliases `playervar`, `pvar`), and their `...default` forms.
* **Other** — `executein`, `raytrace`, `armorset`, `armorsetlowestlevel`, `slottovanilla`.

## BlockUtils — `%blockutils_...%`

Queries blocks relative to a coordinate. Format:

```
%blockutils_<function>_<world>,<x>,<y>,<z>,...%
```

* **getrelative** — `<world>,<x>,<y>,<z>,<BlockFace>,<amount>,<info>` — moves `amount` blocks in the given `BlockFace` direction and returns the requested info (`material`/`mat`, `coords`/`coord`, `x`, `y`, `z`).
* **getrelativeonlyair** — same as `getrelative`, used when the relative target should be air.

## PlayerUtils — `%playerutils_...%`

Queries data about a player.

* **Movement** — `velocity` (alias `speed`), `falldistance`, `isflying`, `issprinting`, `iscrouching`, `vehicle`, `vehicleuuid`.
* **World/time** — `playertime`, `ptimeisday`, `ptimeisnight`, `isthundering`, `israining`, `nearestbiome`, `worldenvironment`.
* **Targeting** — `facing`, `getblock`, `raytrace`, `nearentity`, `distance`.
* **Misc** — `xplevel`, `isblocking`, `hasnbtitem`, `vanillascale`, `relational`.

{% hint style="info" %}
Function and argument names above are taken from the current source. The exact arguments each function expects can be confirmed in the plugin's placeholder classes.
{% endhint %}
