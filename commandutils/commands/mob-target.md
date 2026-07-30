---
description: Makes mobs attack a random one of several targets
---

# Mob Target

Usage: /mobtarget \<Entities> \[\<Targets>]

* Entities - The mobs to redirect
* Targets _(optional)_ - Who they may attack. Each mob picks one at random. Omit to clear their target

Only creatures — mobs that pathfind and attack — are affected. Passive animals will run rather than fight.

Giving several targets spreads the mobs across them, which is what makes a defence wave feel less like everything chasing one player. To point every mob at the same target, use [Set Mob Target](set-mob-target.md).

### Examples

Send every zombie nearby after a random player:

```
/mobtarget @e[type=zombie,distance=..30] @a[distance=..30]
```

Make the mobs forget their target:

```
/mobtarget @e[type=zombie,distance=..30]
```
