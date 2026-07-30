---
description: Stops a player from sprinting for a while
---

# Disable Sprint

Usage: /disablesprint \<Player> \<Duration>

* Player - The player to affect
* Duration - How long sprinting is blocked, e.g. `5s`, `1m`, `100t`

Sprinting is blocked by holding the player's hunger at 6, the level the game refuses to sprint at. Their original food level is restored when the duration ends.

{% hint style="info" %}
Because it works through hunger, the player's food bar visibly drops while the effect is active.
{% endhint %}

### Examples

Slow a fleeing player for 5 seconds:

```
/disablesprint Steve 5s
```

A heavy armour set that stops sprinting while worn, refreshed every second:

```
/disablesprint %player_name% 2s
```
