---
description: Deletes blocks in a cube, with no drops
---

# Remove In Radius

Usage: /removeinradius \<[World](../arguments/world-argument.md)> \<[Location](../arguments/location-argument.md)> \<Radius> \<Player>

Usage: /removeinradius \<[World](../arguments/world-argument.md)> \<[Location](../arguments/location-argument.md)> \<Radius> \<Player> \<[Whitelisted Blocks](../arguments/whitelisted-blocks.md)>

* World - The world the location is in
* Location - Coordinates of the centre block
* Radius - Cube radius. `1` is 3×3×3, `2` is 5×5×5
* Player - The player whose claim permissions are checked
* Whitelisted Blocks - Which blocks may be removed

Blocks are set to air with no drops and no block break event. Use [Break In Radius](break-in-radius.md) if you want the drops.

### Examples

Clear a 5×5×5 area:

```
/removeinradius world 100 64 -30 2 @p
```

Strip only leaves out of a 9×9×9 area:

```
/removeinradius world 100 64 -30 4 @p "#leaves"
```
