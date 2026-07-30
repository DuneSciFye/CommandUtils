---
description: Teleports two entities into each other's place
---

# Swap Positions

Usage: /swappositions \<Entity 1> \<Entity 2>

* Entity 1 - First entity
* Entity 2 - Second entity

Both entities keep their own rotation; only the position changes. Works across worlds.

### Examples

Swap two players:

```
/swappositions Steve Alex
```

An ender-pearl style item that swaps the user with what they are looking at:

```
/swappositions %player_name% @e[type=!player,limit=1,sort=nearest,distance=..20]
```
