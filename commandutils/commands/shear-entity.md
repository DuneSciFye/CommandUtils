---
description: Shears entities as if with shears
---

# Shear Entity

Usage: /shearentity \<Entities>

* Entities - The entities to affect

Sheep drop wool, mooshrooms turn into cows, snow golems lose their pumpkin, bogged drop mushrooms. Entities that can't be sheared right now — already-sheared sheep, lambs — are skipped.

When run as a player, a shear event is fired so other plugins see it as a normal shearing.

### Examples

Shear every sheep within 10 blocks:

```
/shearentity @e[type=sheep,distance=..10]
```

Shear the nearest entity:

```
/shearentity @e[limit=1,sort=nearest]
```
