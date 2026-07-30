---
description: Breaks a block and multiplies its drops
---

# Break Block Multiply Drops

Usage: /breakblockmultiplydrops \<[World](../arguments/world-argument.md)> \<Location> \<Player> \<Drops Multiplier>

* World - The world the location is in
* Location - Coordinates of the block
* Player - The player whose held item decides the drops (enchantments such as Fortune and Silk Touch apply)
* Drops Multiplier - How many copies of the drops to spawn. `0` breaks the block with no drops

The block is set to air directly, so no block break event is fired and no other plugin sees the break. Drops spawn at the given location.

### Examples

Break a block and drop triple loot:

```
/breakblockmultiplydrops world 100 64 -30 @p 3
```

Break a block and delete its drops:

```
/breakblockmultiplydrops world 100 64 -30 @p 0
```
