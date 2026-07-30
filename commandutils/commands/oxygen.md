---
description: Reads or changes a player's remaining air
---

# Oxygen

Usage: /oxygen \<set | add | remove | get> \<Player> \<Amount>

* Player - The player to affect
* Amount - Air in ticks. A full bar is `300`. Still required for `get`, which ignores it

Air only drains while the player's head is under water. Setting it above 300 gives them a longer breath than normal; setting it negative starts drowning damage immediately.

### Examples

Refill a player's air:

```
/oxygen set Steve 300
```

Give a diving item a bigger lung capacity:

```
/oxygen set Steve 900
```

Drain 5 seconds of air:

```
/oxygen remove Steve 100
```

Read the current value:

```
/oxygen get Steve 0
```
