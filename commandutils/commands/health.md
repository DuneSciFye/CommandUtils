---
description: Adds, removes or sets entity health
---

# Health

Usage: /health \<add | remove | set> \<Entities> \<Amount>

* Entities - The entities to affect
* Amount - Half hearts. `2` is one heart

| Function | Effect |
| --- | --- |
| `add` | Heals, capped at the entity's max health |
| `remove` | Reduces health, never below `0` |
| `set` | Sets health directly, clamped between `0` and max health |

Health is changed directly, so no damage event fires — armour, resistance and death messages from a killer are all skipped. Non-living entities in the selection are ignored.

### Examples

Heal a player by 3 hearts:

```
/health add @p 6
```

Take half a heart from every zombie nearby:

```
/health remove @e[type=zombie,distance=..10] 1
```

Set a player to 1 heart:

```
/health set Steve 2
```
