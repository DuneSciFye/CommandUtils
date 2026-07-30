---
description: Deletes entities from the world
---

# Remove Entity

Usage: /removeentity \<Entities>

* Entities - The entities to remove

The entities disappear with no death animation, no drops and no death message. Players in the selection are not affected.

Unlike `/kill`, nothing dies — this is a clean despawn, which is what you want for summoned display entities, markers and leftover projectiles.

### Examples

Clear dropped items in a 20 block radius:

```
/removeentity @e[type=item,distance=..20]
```

Delete the armour stands used by an effect:

```
/removeentity @e[type=armor_stand,tag=cutscene]
```
