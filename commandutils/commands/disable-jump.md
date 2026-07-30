---
description: Stops a player from jumping for a while
---

# Disable Jump

Usage: /disablejump \<Player> \<Duration>

* Player - The player to affect
* Duration - How long jumping is blocked, e.g. `5s`, `1m`, `100t`

The player can still walk, sprint and fall — only the jump itself is cancelled. Running the command again on the same player replaces the previous timer rather than stacking.

To stop movement entirely, use [Stun](stun.md).

### Examples

Root a player for 3 seconds:

```
/disablejump Steve 3s
```

An ensnaring arrow effect:

```
/disablejump @p[distance=..2] 2s
```
