---
description: Makes matching blocks in a radius glow for a set time
---

# Glow Blocks

Usage: /glowblocks \<[World](../arguments/world-argument.md)> \<[Location](../arguments/location-argument.md)> \<Radius> \<Block> \<Duration>

Usage: /glowblocks \<[Location](../arguments/location-argument.md)> \<Radius> \<Block> \<Duration>

Usage: /glowblocks \<[World](../arguments/world-argument.md)> \<[Location](../arguments/location-argument.md)> \<Radius> \<[Whitelisted Blocks](../arguments/whitelisted-blocks.md)> \<Duration>

* World - The world the location is in. Omit to use the sender's world
* Location - Centre of the search
* Radius - Cube radius to search
* Block - A single block predicate, e.g. `diamond_ore` or `#coal_ores`
* Whitelisted Blocks - A full whitelist, for matching several types at once
* Duration - How long the glow lasts, e.g. `10s`, `1m` or `200t`

Blocks can't glow by themselves, so each match gets a glowing block display placed over it. The outline is visible through walls, like the glowing effect on entities, and each display is removed when the duration ends, or earlier if its block is mined, broken or replaced.

### Examples

Show diamond ore within 8 blocks of the player for 10 seconds:

```
/glowblocks %world% %player_x% %player_y% %player_z% 8 diamond_ore 10s
```
