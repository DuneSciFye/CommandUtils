---
description: Adds, removes or sets a player's hunger
---

# Food

Usage: /food \<add | remove | set> \<Player> \<Amount> \[\<Allow Overflow>]

* Player - The player to affect
* Amount - Hunger points. `2` is one drumstick
* Allow Overflow _(optional)_ - Allows values above 20 or below 0. Defaults to `false`

Without overflow the result is clamped to the normal 0–20 range.

### Examples

Feed a player two drumsticks:

```
/food add Steve 4
```

Drain a player's hunger completely:

```
/food set Steve 0
```

Give a temporary buffer above full hunger:

```
/food set Steve 30 true
```
