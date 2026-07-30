---
description: Runs a list of functions on every matching block in front of the player
---

# Select Blocks Facing

Usage: /selectblocksfacing \<[World](../arguments/world-argument.md)> \<[Location](../arguments/location-argument.md)> \<Player> \<Radius> \<Depth> \<[Whitelisted Blocks](../arguments/whitelisted-blocks.md)> \<Command Separator> \<Placeholder Surrounder> \<Custom Placeholders> \<Functions>

* World - The world the location is in
* Location - Coordinates of the centre block
* Player - Whose facing direction is used. The actions run as them, and their claim permissions are checked
* Radius - Half the width and height of the wall. `1` is 3×3, `2` is 5×5
* Depth - How many blocks deep to reach into the surface
* Whitelisted Blocks - Which blocks are selected
* Command Separator - String that separates the functions, e.g. `;`
* Placeholder Surrounder - Character used in place of `%` in the function list. Use `""` to disable placeholder handling
* Custom Placeholders - Kept for compatibility; placeholder handling is driven by `Placeholder Surrounder`
* Functions - The functions to run on each block, in order

Identical to [Select Blocks](select-blocks.md), except the selection is the wall the player is looking at rather than a cube. See that page for the full function list.

### Examples

A 3×3 excavator that smelts what it mines:

```
/selectblocksfacing %world% %block_x% %block_y% %block_z% %player_name% 1 1 pickaxe ; % true BLOCK:BREAK;ITEM:SMELT;ITEM:AUTO_PICKUP;ITEM:DROP
```

A 5×5, 2 deep tunneller that silk-touches everything:

```
/selectblocksfacing world 100 64 -30 @p 2 2 pickaxe ; % true BLOCK:SILK_TOUCH;ITEM:DROP
```
