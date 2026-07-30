---
description: Reads, changes or resets how fast a player flies
---

# Flight Speed

Usage: /flightspeed \<set> \<Player> \<Flight Speed>

Usage: /flightspeed \<get> \<Player>

Usage: /flightspeed \<reset> \<Player>

* Player - The player to affect
* Flight Speed - A value between `0` and `1`. The vanilla default is `0.1`

`reset` puts the speed back to `0.1`. `0` leaves the player able to fly but unable to move.

### Examples

Double the default flight speed:

```
/flightspeed set Steve 0.2
```

Freeze a flying player in place:

```
/flightspeed set Steve 0
```

Check and restore:

```
/flightspeed get Steve
/flightspeed reset Steve
```
