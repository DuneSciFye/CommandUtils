---
description: Marks matching blocks in a radius with particles
---

# Highlight Blocks

Usage: /highlightblocks \<[World](../arguments/world-argument.md)> \<[Location](../arguments/location-argument.md)> \<Radius> \<Block> \<Particle> \[\<Particle Count>] \[\<Particle Offset>] \[\<Particle Speed>] \[\<Number Of Intervals>] \[\<Particle Spawn Interval>]

Usage: /highlightblocks \<[Location](../arguments/location-argument.md)> \<Radius> \<Block> \<Particle> \[...]

Usage: /highlightblocks \<[World](../arguments/world-argument.md)> \<[Location](../arguments/location-argument.md)> \<Radius> \<[Whitelisted Blocks](../arguments/whitelisted-blocks.md)> \<Particle> \[...]

* World - The world the location is in. Omit to use the sender's world
* Location - Centre of the search
* Radius - Cube radius to search
* Block - A single block predicate, e.g. `diamond_ore` or `#coal_ores`
* Whitelisted Blocks - A full whitelist, for matching several types at once
* Particle - The particle drawn at each match
* Particle Count _(optional)_ - Particles per block, per spawn. Defaults to `1`
* Particle Offset _(optional)_ - How far they scatter. Defaults to `0.0`
* Particle Speed _(optional)_ - Particle speed. Defaults to `0.0`
* Number Of Intervals _(optional)_ - How many times the particles are redrawn. Defaults to `40`
* Particle Spawn Interval _(optional)_ - Ticks between redraws. Defaults to `2`

Particles appear at the centre of each matching block. With the defaults, a highlight lasts 40 × 2 = 80 ticks, or 4 seconds.

{% hint style="info" %}
All five optional values can be changed under `Commands.HighlightBlocks` in [config.yml](../config.md).
{% endhint %}

### Examples

An ore-detector item, showing diamonds within 8 blocks:

```
/highlightblocks %world% %player_x% %player_y% %player_z% 8 diamond_ore happy_villager
```

Highlight every ore type at once, for 10 seconds:

```
/highlightblocks world 100 32 -30 10 "#coal_ores #iron_ores #diamond_ores" glow 2 0.2 0 100 2
```
