---
description: Toggles a player's ability to walk on water
---

# Set Water Walk

Usage: /setwaterwalk \<Player> \<Enabled>

* Player - The player to affect
* Enabled - Whether the player can walk on water

The water is made solid with invisible entities placed on the surface around the player, hidden from
everyone else, so nobody else sees or stands on them and no blocks are changed.

* Sneaking drops the player into the water.
* Once fully submerged, the player has to reach land before the surface holds them again.
* The ability is not saved: it is cleared when the player disconnects or when the server stops.

### Examples

```
/setwaterwalk Steve true
```

Turn it off:

```
/setwaterwalk Steve false
```

Boots that grant it while worn, refreshed on a loop:

```
/setwaterwalk %player_name% true
```
