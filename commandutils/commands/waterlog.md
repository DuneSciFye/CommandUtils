---
description: Adds or removes the water inside waterloggable blocks
---

# Waterlog

Usage: /waterlog \<[World](../arguments/world-argument.md)> \<[Block Location](../arguments/block-location-argument.md)> \[\<Waterlogged State>] \[\<Radius>]

* World - The world the location is in
* Location - Coordinates of the centre block
* Waterlogged State _(optional)_ - `true` fills the block with water, `false` drains it. Defaults to `true`
* Radius _(optional)_ - Cube radius. `1` is 3×3×3, `2` is 5×5×5. Defaults to `0` (single block)

Only blocks that can be waterlogged — stairs, slabs, fences, walls, signs, coral, trapdoors — are touched. Everything else in the radius is skipped.

### Examples

Waterlog a single slab:

```
/waterlog world 100 64 -30
```

Drain a 5×5×5 area of waterlogged blocks:

```
/waterlog world 100 64 -30 false 2
```
