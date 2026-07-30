---
description: Points mobs at one target
---

# Set Mob Target

Usage: /setmobtarget \<Entities> \<Target>

* Entities - The mobs to redirect
* Target - The living entity they all attack

Only creatures — mobs that pathfind and attack — are affected, and the target must be a living entity.

For spreading mobs across several targets at random, use [Mob Target](mob-target.md).

### Examples

Send every mob nearby after one player:

```
/setmobtarget @e[type=!player,distance=..30] Steve
```

Turn a boss's minions on the boss:

```
/setmobtarget @e[type=zombie,tag=minion] @e[type=wither,limit=1]
```
