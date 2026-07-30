---
description: Spawns a firework at a location that hurts nobody
---

# Spawn No Damage Firework

Usage: /spawnnodamagefirework \<[World](../arguments/world-argument.md)> \<[Location](../arguments/location-argument.md)> \<Ticks To Detonate> \[\<No Damage Player>]

* World - The world the location is in
* Location - Where the firework spawns
* Ticks To Detonate - Ticks before it bursts. `0` detonates instantly
* No Damage Player _(optional)_ - Protect only this player, leaving the burst harmful to everyone else

The firework gets a random colour and, by default, damages nobody at all. Naming a player narrows that down so it can still be used as an attack that spares its owner.

Unlike [Launch Firework](launch-firework.md), this one spawns at a location instead of flying out of a player.

### Examples

A harmless celebration burst:

```
/spawnnodamagefirework world 100 70 -30 0
```

A firework attack that won't hurt the player who fired it:

```
/spawnnodamagefirework world 100 70 -30 20 Steve
```
