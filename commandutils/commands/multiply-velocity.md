---
description: Scales an entity's current velocity
---

# Multiply Velocity

Usage: /multiplyvelocity \<Entity> \<Velocity>

* Entity - The entity to affect
* Velocity - The multiplier. `2` doubles the speed, `0.5` halves it, `0` stops it dead

The direction is kept; only the speed changes. An entity that isn't moving stays still, since anything times zero is zero — use [Modify Velocity](modify-velocity.md) or [Push Entity](push-entity.md) to start movement.

### Examples

Double a player's momentum, for a dash item:

```
/multiplyvelocity Steve 2
```

Stop a falling entity in place:

```
/multiplyvelocity @e[type=falling_block,limit=1,sort=nearest] 0
```

Reverse an arrow's flight:

```
/multiplyvelocity @e[type=arrow,limit=1,sort=nearest] -1
```
