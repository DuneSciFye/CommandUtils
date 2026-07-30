---
description: Spawns particles without needing a player at the location
---

# Silent Particle

Usage: /silentparticle \<Particle> \<[Location](../arguments/location-argument.md)> \[\<X Offset> \<Y Offset> \<Z Offset>] \[\<Speed>] \[\<Amount>] \[\<Force>]

* Particle - The particle type, with its data where the particle needs one, e.g. `dust 1 0 0 1`
* Location - Where the particles spawn
* X / Y / Z Offset _(optional)_ - How far the particles scatter on each axis. Defaults to `0`
* Speed _(optional)_ - Particle speed, or its variant for particles that use the field differently. Defaults to `1.0`
* Amount _(optional)_ - How many particles. Defaults to `1`
* Force _(optional)_ - Shows the particles even to players who have particles turned down or are far away. Defaults to `false`

Unlike `/particle`, this does not need a viewer or a player context, so it works cleanly from the console, command blocks and loops.

{% hint style="info" %}
Setting `Amount` to `0` makes the offsets act as a direction vector instead of a spread — the way vanilla directional particles work.
{% endhint %}

### Examples

A single flame:

```
/silentparticle flame 100 64 -30
```

A puff of 30 smoke particles spread over half a block:

```
/silentparticle campfire_cosy_smoke 100 64 -30 0.5 0.5 0.5 0.02 30
```

Coloured dust everyone can see:

```
/silentparticle dust 1 0 0 1 100 64 -30 0.3 0.3 0.3 0 20 true
```
