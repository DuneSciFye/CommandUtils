---
description: Leashes an animal to a player, up to a limit
---

# Leash

Usage: /leash \<Player> \<Target> \<Max> \[\<Drop Leash>]

* Player - The player who holds the leash
* Target - The animal to leash
* Max - How many animals this player may hold at once. Nothing happens once they are at the limit
* Drop Leash _(optional)_ - Whether a lead item drops when the leash comes off. Defaults to `true`

Only animals can be leashed, and only if they are not already on a lead. The count is checked against the animals currently leashed to that player, so `Max` acts as a per-player cap for the whole herd.

Setting `Drop Leash` to `false` marks the animal so no lead ever drops from it — whether the leash breaks, the animal is unleashed by hand, or it dies. That is what makes leads that shouldn't be duplicated possible.

### Examples

Leash the nearest cow, up to four animals per player:

```
/leash Steve @e[type=cow,limit=1,sort=nearest] 4
```

A custom lead item that never gives the lead back:

```
/leash %player_name% @e[type=!player,limit=1,sort=nearest,distance=..5] 2 false
```
