---
description: Sets how long entities burn
---

# Set Fire Ticks

Usage: /setfireticks \<Entities> \<Duration>

* Entities - The entities to affect
* Duration - How long they burn, e.g. `5s`, `100t`. `0` puts the fire out

Sets the burn timer directly, so it replaces any fire already on the entity rather than adding to it. Fire-resistant mobs and players with the effect still take no damage. Non-living entities in the selection are ignored.

### Examples

Set a mob on fire for 5 seconds:

```
/setfireticks @e[type=zombie,limit=1,sort=nearest] 5s
```

Extinguish everyone nearby:

```
/setfireticks @a[distance=..10] 0t
```
