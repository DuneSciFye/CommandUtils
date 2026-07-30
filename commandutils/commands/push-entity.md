---
description: Throws entities towards a point or another entity
---

# Push Entity

Usage: /pushentity \<Entities> \<[Location](../arguments/location-argument.md)> \[\<Multiplier>]

Usage: /pushentity \<Entities> \<Target> \[\<Multiplier>]

* Entities - The entities to push
* Location - The point to push them towards
* Target - The entity to push them towards
* Multiplier _(optional)_ - Push strength in blocks per tick. Defaults to `1.0`

Each entity's velocity is replaced with a straight line towards the destination at the given speed, so distance doesn't change the strength — a mob 50 blocks away is thrown just as hard as one right next to it. A negative multiplier pushes them away instead.

Entities in a different world are skipped.

### Examples

Pull every mob within 10 blocks towards a point:

```
/pushentity @e[type=!player,distance=..10] 100 64 -30 1.5
```

A grappling hook that yanks a player to you:

```
/pushentity Alex Steve 2
```

Knock everyone away from a boss:

```
/pushentity @a[distance=..8] @e[type=wither,limit=1] -2
```
