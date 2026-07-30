---
description: Steals potion effects from entities onto yourself
---

# Copy Effects

Usage: /copyeffects \<Entities To Copy From> \<Potion Types> \[\<Amplifier>] \[\<Duration>] \[\<Remove Effect From Targets>] \[\<Ignore Infinite Duration Effects>] \[\<Max Number of Effects>]

* Entities To Copy From - The entities whose effects are read
* Potion Types - Which effects may be copied, e.g. `"strength speed regeneration"`
* Amplifier _(optional)_ - Force the copied effects to this level. Defaults to keeping the original
* Duration _(optional)_ - Force the copied effects to this length. Defaults to keeping the remaining time
* Remove Effect From Targets _(optional)_ - Take the effect away from the source. Defaults to `false`
* Ignore Infinite Duration Effects _(optional)_ - Skip effects with no timer. Defaults to `false`
* Max Number of Effects _(optional)_ - Stop after copying this many. Defaults to no limit

Runs as the player who executed it, so call it through `/execute as <player>` from the console or a command block. That player receives the effects.

Each entity's effects are shuffled before copying, so a limit of `1` takes a random one rather than always the same.

### Examples

Copy any buff from the nearest mob:

```
/copyeffects @e[limit=1,sort=nearest,distance=..5] "strength speed regeneration resistance"
```

Steal one buff, removing it from the victim:

```
/copyeffects @e[type=player,limit=1,sort=nearest,distance=..3] "strength speed" 0 10s true false 1
```

Copy at a fixed strength and length, ignoring permanent effects:

```
/copyeffects @a[distance=..10] "speed jump_boost" 1 30s false true
```
