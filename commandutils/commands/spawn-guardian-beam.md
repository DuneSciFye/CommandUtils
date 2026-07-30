---
description: Draws a guardian laser between two points or entities
---

# Spawn Guardian Beam

Usage: /spawnguardianbeam \<[World](../arguments/world-argument.md)> \<First Location> \<Second Location> \<Duration> \<Distance>

Usage: /spawnguardianbeam \<First Entity> \<Second Entity> \<Duration> \<Distance>

Usage: /spawnguardianbeam \<[World](../arguments/world-argument.md)> \<First Location> \<First Entity> \<Duration> \<Distance>

Usage: /spawnguardianbeam \<First Entity> \<[World](../arguments/world-argument.md)> \<First Location> \<Duration> \<Distance>

* World - The world the locations are in
* First / Second Location - The ends of the beam
* First / Second Entity - Entities to anchor the ends to. The beam follows them as they move
* Duration - How long the beam lasts, in ticks
* Distance - How far away players can be and still see it, in blocks

The beam is sent as packets, so no guardian is spawned and nothing takes damage — it is purely visual.

Mixing a location and an entity anchors one end in place and lets the other end track.

### Examples

A 5 second beam between two points:

```
/spawnguardianbeam world 100 64 -30 110 70 -30 100 64
```

A beam that follows two players:

```
/spawnguardianbeam Steve Alex 200 64
```

A turret that locks onto the nearest mob:

```
/spawnguardianbeam world 100 70 -30 @e[type=!player,limit=1,sort=nearest] 60 48
```
