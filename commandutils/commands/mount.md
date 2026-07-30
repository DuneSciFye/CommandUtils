---
description: Puts one entity on top of another
---

# Mount

Usage: /mount \<Rider> \<Mounted>

* Rider - The entity that gets on
* Mounted - The entity being ridden

Works with any pair of entities — a player on an armour stand, a zombie on a chicken, an item frame on a boat. The rider is removed from whatever it was riding before.

### Examples

Put a player on the nearest horse:

```
/mount Steve @e[type=horse,limit=1,sort=nearest]
```

Build a chicken jockey:

```
/mount @e[type=zombie,limit=1] @e[type=chicken,limit=1]
```
