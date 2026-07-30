---
description: Holds a player in place for a while
---

# Stun

Usage: /stun \<Player> \<Duration> \[\<Allow Fall>]

* Player - The player to affect
* Duration - How long the stun lasts, e.g. `5s`, `1m`, `100t`
* Allow Fall _(optional)_ - Whether the player can still fall. Defaults to `true`

Walking, jumping and teleporting are all blocked. The player can still look around, attack and use items.

With `Allow Fall` set to `false` they are frozen mid-air as well, which is useful for suspending someone during a cutscene. Running the command again replaces the previous timer.

### Examples

Stun a player for 3 seconds:

```
/stun Steve 3s
```

Freeze them completely, including in mid-air:

```
/stun Steve 5s false
```

Stun everyone caught in an area attack:

```
/stun @p[distance=..6] 2s
```
