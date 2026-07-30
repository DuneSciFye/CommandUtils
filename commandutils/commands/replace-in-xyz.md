---
description: Swaps blocks in a cuboid sized and oriented to the player's view
---

# Replace In XYZ

Usage: /replaceinxyz \<[World](../arguments/world-argument.md)> \<[Location](../arguments/location-argument.md)> \<Player> \<X> \<Y> \<Z> \<[Whitelisted Blocks](../arguments/whitelisted-blocks.md)> \<Blocks To Replace To> \[\<Apply Physics>] \[\<Time>]

* World - The world the location is in
* Location - Coordinates of the centre block
* Player - Whose facing direction is used, and whose claim permissions are checked
* X - Half the width, left and right of the player's view
* Y - Half the height
* Z - Depth into the surface
* Whitelisted Blocks - Which blocks may be replaced
* Blocks To Replace To - One or more materials. With several, one is picked at random per block
* Apply Physics _(optional)_ - Whether neighbours update, so torches pop off and water flows. Defaults to `true`
* Time _(optional)_ - Reverts each block to what it was after this long. Omit to make the change permanent

Same cuboid as [Break In XYZ](break-in-xyz.md): it rotates with the player, so `X` is always sideways and `Z` is always forward.

### Examples

Pave a 5 wide, 1 tall strip of the ground in front of the player:

```
/replaceinxyz world 100 64 -30 @p 2 0 3 dirt stone_path
```

Temporarily wall off a 5×3 opening for 30 seconds:

```
/replaceinxyz world 100 64 -30 @p 2 1 1 air barrier true 30s
```
