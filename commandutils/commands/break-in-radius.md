---
description: Breaks blocks in a cube around a location
---

# Break In Radius

Usage: /breakinradius \<[World](../arguments/world-argument.md)> \<[Location](../arguments/location-argument.md)> \<Player> \<Radius>

Usage: /breakinradius \<[World](../arguments/world-argument.md)> \<[Location](../arguments/location-argument.md)> \<Player> \<Radius> \<[Whitelisted Blocks](../arguments/whitelisted-blocks.md)>

Usage: /breakinradius \<[World](../arguments/world-argument.md)> \<[Location](../arguments/location-argument.md)> \<Player> \<Radius> \<[Whitelisted Blocks](../arguments/whitelisted-blocks.md)> \<Drop>

Usage: /breakinradius \<[World](../arguments/world-argument.md)> \<[Location](../arguments/location-argument.md)> \<Player> \<Radius> \<[Whitelisted Blocks](../arguments/whitelisted-blocks.md)> \<forcedrop>

* World - The world the location is in
* Location - Coordinates of the centre block
* Player - The player credited with the break. Their held item decides the drops and their claim permissions are checked
* Radius - Cube radius. `1` is 3×3×3, `2` is 5×5×5
* Whitelisted Blocks - Which blocks may be broken
* Drop - Drops this item once per broken block instead of the blocks' normal drops
* forcedrop - Drops the block itself, as if mined with Silk Touch

Blocks are set to air without firing a block break event. All drops are merged into full stacks and dropped at the centre location.

### Examples

Clear a 3×3×3 cube:

```
/breakinradius world 100 64 -30 @p 1
```

A hammer that only breaks stone-type blocks in a 5×5×5:

```
/breakinradius %world% %block_x% %block_y% %block_z% %player_name% 2 pickaxe
```

Break leaves in a 5×5×5 but drop one apple per leaf block:

```
/breakinradius world 100 64 -30 @p 2 "#leaves" apple
```

Silk-touch every log in a 3×3×3:

```
/breakinradius world 100 64 -30 @p 1 "#logs" forcedrop
```
