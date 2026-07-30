---
description: Swaps blocks in a cube, but only where the neighbouring blocks match
---

# Replace In Radius If Block Relative

Usage: /replaceinradiusifblockrelative \<[World](../arguments/world-argument.md)> \<[Location](../arguments/location-argument.md)> \<Player> \<Radius> \<Blocks To Replace From> \<Blocks To Replace To> \<Block Faces> \<Blocks Relative> \[\<Remove Item>]

Usage: /replaceinradiusifblockrelative \<[World](../arguments/world-argument.md)> \<[Location](../arguments/location-argument.md)> \<Player> \<Radius> \<Blocks To Replace From> \<Block To Replace To> \<Block Faces> \<Blocks Relative> \[\<Remove Item>]

* World - The world the location is in
* Location - Coordinates of the centre block
* Player - Whose claim permissions are checked, and whose inventory `Remove Item` is taken from
* Radius - Cube radius. `1` is 3×3×3, `2` is 5×5×5
* Blocks To Replace From - [Whitelist](../arguments/whitelisted-blocks.md) of blocks that may be replaced
* Blocks To Replace To - List of materials. With several, one is picked at random per block
* Block To Replace To - A single block with block data, keeping facing, half, waterlogged and so on
* Block Faces - Which sides to check, e.g. `UP`, `"NORTH EAST SOUTH WEST"`
* Blocks Relative - [Whitelist](../arguments/whitelisted-blocks.md) that the block on **every** listed face must match
* Remove Item _(optional)_ - Item taken from the player's inventory for each block replaced. The command stops when they run out

A block is only replaced when it matches `Blocks To Replace From` **and** each face in `Block Faces` has a block matching `Blocks Relative`. That makes it easy to target only exposed surfaces — for example dirt with air above it.

### Examples

Grow grass on dirt that is open to the sky, consuming one bone meal per block:

```
/replaceinradiusifblockrelative world 100 64 -30 @p 3 dirt grass_block up air bone_meal
```

Turn stone into moss only where it is exposed on all four sides:

```
/replaceinradiusifblockrelative world 100 64 -30 @p 2 stone moss_block "NORTH EAST SOUTH WEST" air
```

Place a directional block, keeping its block data:

```
/replaceinradiusifblockrelative world 100 64 -30 @p 2 dirt "campfire[facing=north,lit=true]" up air
```
