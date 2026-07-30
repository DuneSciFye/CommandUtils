---
description: Summons an entity with no feedback message
---

# Silent Summon

Usage: /silentsummon \<Entity Type> \<[Location](../arguments/location-argument.md)>

* Entity Type - The entity to spawn
* Location - Where it spawns

The entity is spawned with its default data and no confirmation is sent, which keeps command blocks and loops from spamming the console. Use `/summon` when you need NBT.

### Examples

```
/silentsummon zombie 100 64 -30
```

Spawn a marker armour stand for an effect:

```
/silentsummon armor_stand 100 64 -30
```
