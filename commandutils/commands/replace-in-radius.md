---
description: Swaps blocks in a cube for other blocks, permanently or for a while
---

# Replace In Radius

Usage: /replaceinradius \<[World](../arguments/world-argument.md)> \<[Location](../arguments/location-argument.md)> \<Player> \<Radius> \<[Whitelisted Blocks](../arguments/whitelisted-blocks.md)> \<Blocks To Replace To> \[\<Apply Physics>] \[\<Duration>]

Usage: /replaceinradius \<[World](../arguments/world-argument.md)> \<[Location](../arguments/location-argument.md)> \<Radius> \<[Whitelisted Blocks](../arguments/whitelisted-blocks.md)> \<Blocks To Replace To>

* World - The world the location is in
* Location - Coordinates of the centre block
* Player - The player whose claim permissions are checked
* Radius - Cube radius. `1` is 3×3×3, `2` is 5×5×5
* Whitelisted Blocks - Which blocks may be replaced
* Blocks To Replace To - One or more materials. With several, one is picked at random per block
* Apply Physics _(optional)_ - Whether neighbours update, so torches pop off and water flows. Defaults to `true`
* Duration _(optional)_ - Reverts each block to what it was after this long. Omit to make the change permanent

The second form has no player, so no claim check is done — use it from the console or a command block.

### Examples

Turn all the stone in a 5×5×5 into gold:

```
/replaceinradius world 100 64 -30 @p 2 stone gold_block
```

Scatter three ore types randomly through a 7×7×7 of stone:

```
/replaceinradius world 100 64 -30 @p 3 stone "coal_ore iron_ore gold_ore"
```

Freeze the water in a 9×9×9 into ice for 10 seconds:

```
/replaceinradius world 100 64 -30 @p 4 water ice true 10s
```
