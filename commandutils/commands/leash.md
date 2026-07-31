---
description: Leashes animals to a player, up to a limit
---

# Leash

Usage: /leash \<Player> \<Targets> \<Max> \[\<Drop Leash>]

* Player - The player who holds the leash
* Targets - The animals to leash. A selector matching several animals leashes them all in one go
* Max - How many animals this player may hold at once. Extra animals are skipped once they are at the limit
* Drop Leash _(optional)_ - Whether a lead item drops when the leash comes off. Defaults to `true`

Only animals can be leashed. The count is checked against the animals currently leashed to that player, so `Max` acts as a per-player cap for the whole herd, and it is tracked while the command runs so a single selector cannot go over it.

Running the command on an animal that is **already leashed** takes the leash off instead. No lead item drops in that case, since the command never consumed one to put the leash on. Toggling off works even when the player is at the `Max` limit, and frees a slot for the rest of the selection.

Setting `Drop Leash` to `false` marks the animal so no lead ever drops from it — whether the leash breaks, the animal is unleashed by hand, or it dies. That is what makes leads that shouldn't be duplicated possible.

### Examples

Leash every cow within five blocks, up to four animals per player:

```
/leash Steve @e[type=cow,distance=..5] 4
```

Toggle the nearest animal on or off:

```
/leash %player_name% @e[type=!player,limit=1,sort=nearest,distance=..5] 2
```

A custom lead item that never gives the lead back:

```
/leash %player_name% @e[type=!player,limit=1,sort=nearest,distance=..5] 2 false
```
