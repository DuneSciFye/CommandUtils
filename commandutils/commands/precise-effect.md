---
description: Applies a potion effect with full control over its flags
---

# Precise Effect

Usage: /preciseeffect \<Entities> \<Effect> \[\<Duration>] \[\<Level>] \[\<Hide Particles>] \[\<Ambient>]

Usage: /preciseeffect \<Entities> \<Effect> \[\<infinite>] \[\<Level>] \[\<Hide Particles>] \[\<Ambient>]

* Entities - The entities to affect
* Effect - The potion effect
* Duration _(optional)_ - How long it lasts, e.g. `30s`, `1m`, `100t`. Defaults to `30s`
* infinite - Applies the effect with no timer
* Level _(optional)_ - Amplifier. `0` is level I, `1` is level II. Defaults to `0`
* Hide Particles _(optional)_ - Hides the swirling particles. Defaults to `false`
* Ambient _(optional)_ - Uses the faded beacon-style particles. Defaults to `false`

Vanilla's `/effect` only takes whole seconds; here durations are written in the usual [duration format](../arguments/README.md#duration), so `10t` works.

### Examples

Two seconds of slowness — too short for `/effect`:

```
/preciseeffect @p slowness 40t 2
```

A hidden permanent buff:

```
/preciseeffect Steve night_vision infinite 0 true
```

Strength II for a minute on everyone nearby:

```
/preciseeffect @a[distance=..10] strength 1m 1
```
