---
description: Spawns evoker fangs that damage nothing
---

# Spawn No Damage Evoker Fang

Usage: /spawnnodamageevokerfang \<[World](../arguments/world-argument.md)> \<[Location](../arguments/location-argument.md)>

* World - The world the location is in
* Location - Where the fangs appear

The fangs snap and play their sound as normal, but hit nobody — useful as a telegraph for an attack that lands a moment later.

### Examples

```
/spawnnodamageevokerfang world 100 64 -30
```

A line of fangs in front of a boss, then real damage a second later:

```
/spawnnodamageevokerfang world 100 64 -30
/runcommandlater run 1s damage @a[x=100,y=64,z=-30,distance=..1] 6
```
