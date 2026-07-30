---
description: Loads or unloads a crossbow
---

# Load Crossbow

Usage: /loadcrossbow \<Player> \<[Slot](../arguments/slot-argument.md)> \[\<Loaded>] \[\<Interact With Inventory>]

* Player - The player holding the crossbow
* Slot - Slot of the crossbow
* Loaded _(optional)_ - `true` charges it with an arrow, `false` empties it. Defaults to `true`
* Interact With Inventory _(optional)_ - Whether the arrow comes from, or goes back into, the player's inventory. Defaults to `true`

With inventory interaction on, loading takes one arrow from the player and does nothing if they have none; unloading gives the arrow back, dropping it if there's no room. With it off, the arrow is created and destroyed out of thin air.

Items that aren't crossbows are ignored.

### Examples

Charge the held crossbow, using one of the player's arrows:

```
/loadcrossbow Steve mainhand
```

Charge it for free, without touching the inventory:

```
/loadcrossbow Steve mainhand true false
```

Empty it and take the arrow away:

```
/loadcrossbow Steve mainhand false false
```
