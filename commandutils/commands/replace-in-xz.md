---
description: Swaps blocks in a flat area in front of the player
---

# Replace In XZ

Usage: /replaceinxz \<[World](../arguments/world-argument.md)> \<[Block Location](../arguments/block-location-argument.md)> \<Player> \<X> \<Z> \<[Whitelisted Blocks](../arguments/whitelisted-blocks.md)> \<Blocks To Replace To> \[\<Apply Physics>] \[\<Time>]

* World - The world the location is in
* Location - Coordinates of the centre block
* Player - Whose facing direction is used, and whose claim permissions are checked
* X - Half the width, left and right of the player's view
* Z - Depth into the surface
* Whitelisted Blocks - Which blocks may be replaced
* Blocks To Replace To - One or more materials. With several, one is picked at random per block
* Apply Physics _(optional)_ - Whether neighbours update, so torches pop off and water flows. Defaults to `true`
* Time _(optional)_ - Reverts each block to what it was after this long. Omit to make the change permanent

The same as [Replace In XYZ](replace-in-xyz.md) with the height fixed to a single layer, and looking up or down is ignored — only the four cardinal directions matter. Good for path and farmland tools.

### Examples

A path tool that converts a 5×3 patch of grass:

```
/replaceinxz %world% %block_x% %block_y% %block_z% %player_name% 2 3 grass_block dirt_path
```

Till a 3-wide strip into farmland:

```
/replaceinxz world 100 64 -30 @p 1 3 "dirt grass_block" farmland
```
