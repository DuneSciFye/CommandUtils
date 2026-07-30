---
description: Tags an entity with temporary metadata
---

# Meta Data

Usage: /metadata \<set> \<Entity> \<Key> \<Value>

Usage: /metadata \<remove> \<Entity> \<Key>

Usage: /metadata \<list> \<Entity> \<Key>

* Entity - The entity to tag
* Key - Name of the tag
* Value - The value, stored as text

Metadata lives in memory and is not saved with the entity: it is gone when the server restarts. That makes it right for short-lived state — marking a mob as a boss's minion for the length of a fight — and wrong for anything that has to survive. Use [Set Item NBT](set-item-nbt.md) or scoreboard tags for persistent data.

`list` prints every value stored under that key.

### Examples

Mark a summoned mob:

```
/metadata set @e[type=zombie,limit=1,sort=nearest] boss_minion true
```

Read it back:

```
/metadata list @e[type=zombie,limit=1,sort=nearest] boss_minion
```

Clear the tag:

```
/metadata remove @e[type=zombie,limit=1,sort=nearest] boss_minion
```
