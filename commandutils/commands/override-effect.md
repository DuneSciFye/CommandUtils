---
description: Temporarily replaces a potion effect and puts the old one back afterwards
---

# Override Effect

Usage: /overrideeffect \<override | retrieve | remove> \<ID> \<Player> \<Effect> \[\<Duration>] \[\<Level>] \[\<Hide Particles>] \[\<Ambient>]

Usage: /overrideeffect \<override | retrieve | remove> \<ID> \<Player> \<Effect> \[\<infinite>] \[\<Level>] \[\<Hide Particles>] \[\<Ambient>]

* ID - Name this override is stored under
* Player - The player to affect
* Effect - The potion effect
* Duration _(optional)_ - How long the override lasts. Defaults to `30t`
* infinite - Applies the override with no timer
* Level _(optional)_ - Amplifier. `0` is level I. Defaults to `0`
* Hide Particles _(optional)_ - Hides the swirling particles. Defaults to `false`
* Ambient _(optional)_ - Uses the faded beacon-style particles. Defaults to `false`

| Function | Effect |
| --- | --- |
| `override` | Saves the player's current effect of that type under the ID, then applies the new one |
| `retrieve` | Gives the saved effect back and forgets it |
| `remove` | Forgets the saved effect without restoring it |

This solves the usual problem of an ability overwriting a buff a player already had: apply with `override`, and call `retrieve` when the ability ends so their original speed or strength returns.

{% hint style="warning" %}
Saved effects are held in memory and lost on restart. The restored effect resumes with the duration it had when it was saved, not the time that has passed since.
{% endhint %}

### Examples

Slow a player down during a channel, then give back whatever speed effect they had:

```
/overrideeffect override channel-Steve Steve slowness 5s 2
/overrideeffect retrieve channel-Steve Steve slowness
```

Discard the saved effect instead of restoring it:

```
/overrideeffect remove channel-Steve Steve slowness
```
