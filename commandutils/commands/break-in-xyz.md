---
description: Breaks a cuboid of blocks sized and oriented to the player's view
---

# Break In XYZ

Usage: /breakinxyz \<[World](../arguments/world-argument.md)> \<[Location](../arguments/location-argument.md)> \<Player> \<X> \<Y> \<Z> \[\<[Whitelisted Blocks](../arguments/whitelisted-blocks.md)>]

* World - The world the location is in
* Location - Coordinates of the centre block
* Player - Whose facing direction is used. Their held item decides the drops and their claim permissions are checked
* X - Half the width, left and right of the player's view
* Y - Half the height
* Z - Depth into the surface being mined
* Whitelisted Blocks _(optional)_ - Which blocks may be broken. Omit to break everything

Like [Break In Facing](break-in-facing.md), but width, height and depth are set separately, so the area can be a slab, a pillar or a tunnel instead of a square. The cuboid rotates with the player, so `X` is always sideways and `Z` is always forward.

### Examples

A 5 wide, 3 tall, 1 deep wall:

```
/breakinxyz world 100 64 -30 @p 2 1 1
```

A 1×1 tunnel 10 blocks deep:

```
/breakinxyz world 100 64 -30 @p 0 0 10
```

A wide, flat sweep that only takes dirt-type blocks:

```
/breakinxyz world 100 64 -30 @p 3 0 1 shovel
```
