---
description: Toggles a player constantly floating up when submerged in water
---

# Set Water Float

Usage: /setwaterfloat \<Player> \<Enabled> \[\<Max Speed>]

* Player - The player to affect
* Enabled - Whether the player floats up in water
* Max Speed _(optional)_ - Top speed the rise accelerates to, in blocks per tick. Defaults to `1.8`,
  the speed a vanilla bubble column reaches

While the player's head is under water they are pushed upward exactly as a soul sand bubble column
pushes, with no block placed and nothing visible to anyone.

* The player accelerates by a tenth of a block per tick until they hit the max speed, so a lower max
  speed also means a shorter climb to it.

* The push stops as soon as the head breaks the surface, so the player bobs there instead of being
  thrown out of the water.
* Swimming down works against it briefly, the same as swimming down a real bubble column.
* Players in creative flight or spectator mode are left alone.
* The ability is not saved: it is cleared when the player disconnects or when the server stops.

### Examples

Let a player rise out of deep water:

```
/setwaterfloat Steve true
```

A gentler float, for a diving suit:

```
/setwaterfloat Steve true 0.4
```

Turn it off:

```
/setwaterfloat Steve false
```
