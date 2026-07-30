---
description: Fires a wither skull in a chosen direction
---

# Spawn Wither Skull

Usage: /spawnwitherskull \<[World](../arguments/world-argument.md)> \<[Location](../arguments/location-argument.md)> \[\<Yaw>] \[\<Pitch>] \[\<Velocity Multiplier>] \[\<Break Blocks>]

* World - The world the location is in
* Location - Where the skull spawns
* Yaw _(optional)_ - Horizontal direction in degrees. `0` south, `90` west, `180` north, `-90` east. Defaults to `0`
* Pitch _(optional)_ - Vertical direction. `-90` straight up, `90` straight down. Defaults to `0`
* Velocity Multiplier _(optional)_ - Scales the skull's speed. Defaults to `1.0`
* Break Blocks _(optional)_ - Whether the explosion damages terrain. Defaults to `true`

With `Break Blocks` set to `false` the skull also stops damaging armour stands, which makes it usable in builds and arenas.

### Examples

Fire a skull north:

```
/spawnwitherskull world 100 70 -30 180 0
```

A fast skull aimed slightly downward that leaves the terrain intact:

```
/spawnwitherskull world 100 70 -30 90 20 3 false
```
