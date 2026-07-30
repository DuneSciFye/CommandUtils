---
description: Changes an entity's velocity
---

# Modify Velocity

Usage: /modifyvelocity \<Entity> \<set | add | subtract | multiply | divide> \<Amount>

* Entity - The entity to affect
* Amount - The value applied to every axis

| Function | Effect |
| --- | --- |
| `add` | Adds the amount to X, Y and Z |
| `subtract` | Subtracts it from X, Y and Z |
| `multiply` | Scales the whole velocity — direction unchanged, speed multiplied |
| `divide` | Scales it down the same way |
| `set` | Rescales the velocity so its largest component equals the amount |

Velocity is measured in blocks per tick. A normal walk is about `0.2`, a jump about `0.42` upward.

`multiply` is the one to reach for when boosting an existing movement; [Multiply Velocity](multiply-velocity.md) does the same thing with a shorter syntax.

### Examples

Double a player's current speed:

```
/modifyvelocity Steve multiply 2
```

Give everything nearby an upward nudge:

```
/modifyvelocity @e[distance=..5,limit=1] add 0.5
```

Halve a projectile's speed:

```
/modifyvelocity @e[type=arrow,limit=1,sort=nearest] divide 2
```
