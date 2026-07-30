---
description: Gives potion effects that stack by ID and fall back to each other
---

# Effect

Usage: /effect \<give> \<Player> \<Effect> \<Duration> \<Level> \[\<ID>] \[\<Particles>] \[\<Ambient>] \[\<Icon>]

Usage: /effect \<remove> \<Player> \<ID>

Usage: /effect \<list> \<Player> \[\<Effect>]

Usage: /effect \<clear> \<Player> \[\<Effect>]

* Player - The player to affect
* Effect - The potion effect
* Duration - How long it lasts, e.g. `30s`, `1m`, `100t`, or `infinite`
* Level - Amplifier. `0` is level I, `1` is level II
* ID _(optional)_ - Name for this effect, so it can be removed later. Generated automatically if left out
* Particles _(optional)_ - Shows the swirling particles. Defaults to `true`
* Ambient _(optional)_ - Uses the faded beacon-style particles. Defaults to `false`
* Icon _(optional)_ - Shows the effect icon in the HUD. Defaults to `true`

Vanilla keeps only one effect of each type: a weaker one applied over a stronger one is thrown away, and a stronger one applied over a weaker one erases it for good. This command keeps every entry instead.

For each effect type, all tracked entries are ranked by level, then by which arrived most recently, and only the winner is actually applied. Remove or expire the winner and the next one takes over with whatever time it had left.

Effects the player already had, from vanilla or another plugin, are folded into the stack the first time this command touches that type, so nothing gets lost. Once no entry from this command remains for a type, tracking is dropped and vanilla takes over again.

| Function | Effect |
| --- | --- |
| `give` | Adds an entry. Reusing an ID replaces that entry |
| `remove` | Deletes one entry by ID, letting the next one take over |
| `list` | Shows the tracked entries, marking the active one with `*` |
| `clear` | Deletes every entry, or every entry of one type |

### Examples

An armour set buff that doesn't stomp on a potion the player drank:

```
/effect give Steve strength infinite 1 armor_set
/effect remove Steve armor_set
```

A short, stronger burst on top of a long weaker buff — the burst wins, then the original resumes:

```
/effect give Steve speed 5m 0 boots
/effect give Steve speed 10s 2 dash
```

Inspect and clear:

```
/effect list Steve speed
/effect clear Steve speed
```
