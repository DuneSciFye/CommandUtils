---
description: Breaks a wall of blocks in the direction the player is looking
---

# Break In Facing

Usage: /breakinfacing \<[World](../arguments/world-argument.md)> \<[Location](../arguments/location-argument.md)> \<Player> \<Radius> \<Depth>

Usage: /breakinfacing \<[World](../arguments/world-argument.md)> \<[Location](../arguments/location-argument.md)> \<Player> \<Radius> \<Depth> \<[Whitelisted Blocks](../arguments/whitelisted-blocks.md)>

Usage: /breakinfacing \<[World](../arguments/world-argument.md)> \<[Location](../arguments/location-argument.md)> \<Player> \<Radius> \<Depth> \<[Whitelisted Blocks](../arguments/whitelisted-blocks.md)> \<Drop>

Usage: /breakinfacing \<[World](../arguments/world-argument.md)> \<[Location](../arguments/location-argument.md)> \<Player> \<Radius> \<Depth> \<[Whitelisted Blocks](../arguments/whitelisted-blocks.md)> \<forcedrop>

* World - The world the location is in
* Location - Coordinates of the centre block
* Player - Whose facing direction is used. Their held item decides the drops and their claim permissions are checked
* Radius - Half the width and height of the wall. `1` is 3×3, `2` is 5×5
* Depth - How many blocks deep to dig into the surface. `0` and `1` both mean a single layer
* Whitelisted Blocks - Which blocks may be broken
* Drop - Drops this item once per broken block instead of the blocks' normal drops
* forcedrop - Drops the block itself, as if mined with Silk Touch

The selection is always flat against the player's view: looking up or down mines vertically, looking sideways mines into the wall they face. This is the shape most "excavator" tools want.

Blocks are set to air without firing a block break event. All drops are merged into full stacks and dropped at the centre location.

### Examples

Dig a 3×3 tunnel one block deep:

```
/breakinfacing world 100 64 -30 @p 1 1
```

An excavator shovel: 3×3, 2 deep, only blocks a shovel can mine:

```
/breakinfacing %world% %block_x% %block_y% %block_z% %player_name% 1 2 shovel
```

Break a 5×5 wall of stone and give one cobblestone per block:

```
/breakinfacing world 100 64 -30 @p 2 1 stone cobblestone
```
