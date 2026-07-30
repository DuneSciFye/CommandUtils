---
description: Break In Facing, with every block logged to CoreProtect
---

# Break In Facing Log CoreProtect

Usage: /breakinfacinglogcoreprotect \<[World](../arguments/world-argument.md)> \<[Location](../arguments/location-argument.md)> \<Player> \<Radius> \<Depth> \[\<forcedrop>]

Usage: /breakinfacinglogcoreprotect \<[World](../arguments/world-argument.md)> \<[Location](../arguments/location-argument.md)> \<Player> \<Radius> \<Depth> \<[Whitelisted Blocks](../arguments/whitelisted-blocks.md)> \[\<forcedrop>]

Usage: /breakinfacinglogcoreprotect \<[World](../arguments/world-argument.md)> \<[Location](../arguments/location-argument.md)> \<Player> \<Radius> \<Depth> \<[Whitelisted Blocks](../arguments/whitelisted-blocks.md)> \<Drop>

* World - The world the location is in
* Location - Coordinates of the centre block
* Player - Whose facing direction is used, and who the CoreProtect entries are attributed to
* Radius - Half the width and height of the wall. `1` is 3×3, `2` is 5×5
* Depth - How many blocks deep to dig into the surface
* Whitelisted Blocks - Which blocks may be broken
* Drop - Drops this item once per broken block instead of the blocks' normal drops
* forcedrop _(optional)_ - Drops the block itself, as if mined with Silk Touch

Identical to [Break In Facing](break-in-facing.md), except each broken block is written to CoreProtect as a removal by that player, so it shows up in `/co inspect` and can be rolled back.

{% hint style="warning" %}
Requires **CoreProtect**. Without it the command is not registered.
{% endhint %}

### Examples

Log a 3×3 dig two blocks deep:

```
/breakinfacinglogcoreprotect world 100 64 -30 @p 1 2
```

Only mine ores, and record them:

```
/breakinfacinglogcoreprotect world 100 64 -30 @p 1 1 "#coal_ores #iron_ores #diamond_ores"
```
