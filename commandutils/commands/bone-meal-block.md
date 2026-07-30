---
description: Applies bone meal to blocks in a radius
---

# Bone Meal Block

Usage: /bonemealblock \<[World](../arguments/world-argument.md)> \<[Location](../arguments/block-location-argument.md)> \[\<Amount>] \[\<Radius>] \[\<Affect Target Block>]

* World - The world the location is in
* Location - Coordinates of the centre block
* Amount _(optional)_ - How many bone meals to apply to each block. Defaults to `1`
* Radius _(optional)_ - Cube radius around the location. Defaults to `0` (single block)
* Affect Target Block _(optional)_ - Whether the centre block is also bone mealed. Defaults to `true`

Each application behaves exactly like a player using bone meal on the block from above, so only blocks that accept bone meal (crops, saplings, grass, sea pickles, …) react.

### Examples

Bone meal a single crop:

```
/bonemealblock world 100 64 -30
```

Bone meal a 5×5×5 area three times, skipping the block that was clicked:

```
/bonemealblock world 100 64 -30 3 2 false
```

A bone meal item that fertilises a 5×5 around the clicked block:

```
/bonemealblock %world% %block_x% %block_y% %block_z% 1 2 false
```
