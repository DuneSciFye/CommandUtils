---
description: Fires a coloured firework from a player
---

# Launch Firework

Usage: /launchfirework \<Player> \<Ticks To Detonate> \<No Damage> \<RGB> \[\<Firework Power>]

Usage: /launchfirework \<Player> \[\<Ticks To Detonate>] \[\<No Damage>] \[\<Red RGB> \<Green RGB> \<Blue RGB>] \[\<Firework Power>]

* Player - The player it launches from
* Ticks To Detonate - Ticks before it bursts. `0` detonates instantly. Defaults to `20`
* No Damage - `true` makes the burst harmless. Defaults to `false`
* RGB - The colour as one packed number, e.g. `16711680` for red
* Red / Green / Blue RGB _(optional)_ - The colour as three values `0`–`255`. Each one left out is random
* Firework Power _(optional)_ - Flight power. Defaults to `1`

The firework is launched at an angle from the player's eyes, in the direction they are looking, rather than straight up.

Aliases: `spawnfirework`, `summonfirework`.

### Examples

A random-coloured firework:

```
/launchfirework Steve
```

A harmless red firework that bursts after half a second:

```
/launchfirework Steve 10 true 255 0 0
```

The same colour written as a packed value, with more power:

```
/launchfirework Steve 10 true 16711680 2
```

An instant burst at the player, for a hit effect:

```
/launchfirework Steve 0 true 0 255 255
```
