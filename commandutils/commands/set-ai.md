---
description: Turns entity AI on or off
---

# Set AI

Usage: /setai \<Entities> \<Has AI>

* Entities - The entities to affect
* Has AI - `false` freezes them, `true` wakes them back up

Entities without AI stop moving, attacking and pathfinding, but stay solid, can still be damaged and still take knockback. Non-living entities in the selection are ignored.

### Examples

Freeze every zombie in the world:

```
/setai @e[type=zombie] false
```

Freeze mobs near a boss arena while a cutscene plays:

```
/setai @e[type=!player,distance=..20] false
```

Wake them up again:

```
/setai @e[type=!player,distance=..20] true
```
