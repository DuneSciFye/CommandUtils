---
description: Reads or changes a player's saturation
---

# Saturation

Usage: /saturation \<set | add | remove | get> \<Player> \<Amount>

* Player - The player to affect
* Amount - Saturation value. Still required for `get`, which ignores it

Saturation is the hidden buffer that drains before hunger does. The game caps it at the player's current food level, so a player at 20 hunger can hold 20 saturation.

{% hint style="info" %}
`add` and `remove` work from the player's **food level**, not their current saturation — `add` sets saturation to food level plus the amount, `remove` to food level minus it. Use `set` when you want an exact value.
{% endhint %}

### Examples

Fill a player's saturation:

```
/saturation set Steve 20
```

Empty it so hunger starts dropping straight away:

```
/saturation set Steve 0
```

Read the current value:

```
/saturation get Steve 0
```
