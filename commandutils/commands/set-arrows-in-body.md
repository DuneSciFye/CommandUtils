---
description: Sets how many arrows are stuck in entities
---

# Set Arrows In Body

Usage: /setarrowsinbody \<Entities> \<Number of Arrows>

* Entities - The entities to affect
* Number of Arrows - How many arrows stick out. `0` removes them all

Cosmetic only — the arrows are just rendered on the model and deal no damage. They fall off over time as usual.

### Examples

Make a mob look like a pincushion:

```
/setarrowsinbody @e[type=zombie,limit=1,sort=nearest] 10
```

Clear the arrows off a player:

```
/setarrowsinbody Steve 0
```
