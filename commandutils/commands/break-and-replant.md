---
description: Harvests crops and replants them in one step
---

# Break And Replant

Usage: /breakandreplant \<[World](../arguments/world-argument.md)> \<Location> \<Player> \<Original Block>

Usage: /breakandreplant \<[World](../arguments/world-argument.md)> \<Location> \<Player> \<Radius> \<Original Block>

Usage: /breakandreplant \<[World](../arguments/world-argument.md)> \<Location> \<Player> \<X> \<Y> \<Z> \<Original Block>

* World - The world the location is in
* Location - Coordinates of the centre crop
* Player - The player credited with the harvest. Their held item decides the drops, and their claim permissions are checked
* Original Block - The block restored at the centre location, since the crop that triggered the command is usually already broken
* Radius - Cube radius around the location
* X / Y / Z - Radius along each axis separately, for flat or wall-shaped areas

Only [Ageable](https://jd.papermc.io/paper/1.21.4/org/bukkit/block/data/Ageable.html) blocks (wheat, carrots, potatoes, beetroot, nether wart, …) are harvested. Their age is reset to 0 instead of being broken, so no break particles or sounds play, and one seed is taken out of the drops to pay for the replant. Everything harvested drops at the centre location.

Blocks outside the player's claim are skipped.

### Examples

Harvest and replant a single crop:

```
/breakandreplant world 100 64 -30 @p wheat
```

Harvest a 5×5×5 area:

```
/breakandreplant world 100 64 -30 @p 2 wheat
```

Harvest a flat 7×1×7 field — useful for a hoe that only affects one crop layer:

```
/breakandreplant world 100 64 -30 @p 3 0 3 wheat
```
