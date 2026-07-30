---
description: Shows a temporary boss bar to one player
---

# Send Boss Bar

Usage: /sendbossbar \<Player> \<Bossbar ID> \<Bossbar Color> \<Bossbar Progress> \<Ticks To Show> \<Bossbar Content>

* Player - The player who sees the bar
* Bossbar ID - Name for this bar. Reusing it replaces the bar instead of stacking a second one
* Bossbar Color - `PINK`, `BLUE`, `RED`, `GREEN`, `YELLOW`, `PURPLE` or `WHITE`
* Bossbar Progress - How full the bar is, `0.0` to `1.0`
* Ticks To Show - How long it stays before hiding itself
* Bossbar Content - The text on the bar. `&` colour codes work

Bars are tracked per player and per ID, so several can be on screen at once as long as they have different IDs. Sending the same ID again resets its timer.

### Examples

A five second warning bar:

```
/sendbossbar Steve warning RED 1.0 100 &cThe raid begins in 5 seconds
```

A cast bar filled in steps by a loop:

```
/sendbossbar Steve cast BLUE 0.5 40 &bChannelling...
```

Show a boss's health to a player:

```
/sendbossbar Steve boss PURPLE 0.75 60 &5Ancient Wither
```
