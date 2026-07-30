---
description: Turns a player's flight on or off
---

# Set Flight

Usage: /setflight \<Player> \<Flying>

* Player - The player to affect
* Flying - `true` grants flight and starts it, `false` removes it

Both the permission to fly and the flying state itself are set, so `true` puts the player in the air right away and `false` drops them.

Change how fast they fly with [Flight Speed](flight-speed.md).

### Examples

```
/setflight Steve true
```

```
/setflight @a[distance=..10] false
```
