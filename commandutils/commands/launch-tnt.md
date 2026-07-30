---
description: Throws primed TNT from a player, or drops it at a location
---

# Launch TNT

Usage: /launchtnt \<Player> \[\<Break Blocks>]

Usage: /launchtnt \<[World](../arguments/world-argument.md)> \<[Location](../arguments/location-argument.md)> \[\<Break Blocks>]

* Player - The player to throw from. The TNT flies in the direction they are looking
* World - The world the location is in
* Location - Where the TNT is spawned. It drops straight down
* Break Blocks _(optional)_ - Despite the name, `true` **stops** the explosion breaking blocks. Defaults to `false`

The TNT is primed as normal, so it explodes after the usual 4 seconds and still damages entities either way.

### Examples

Throw a stick of TNT:

```
/launchtnt Steve
```

Throw TNT that damages players but leaves the terrain alone:

```
/launchtnt Steve true
```

Drop TNT at a fixed point:

```
/launchtnt world 100 70 -30
```
