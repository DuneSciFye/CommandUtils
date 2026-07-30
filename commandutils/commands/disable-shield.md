---
description: Breaks players' shield guard and blocks it for a while
---

# Disable Shield

Usage: /disableshield \<Players> \<Duration>

* Players - The players to affect
* Duration - How long shields stay disabled, e.g. `5s`, `1m`, `100t`

Anyone currently blocking is forced to lower their shield, a cooldown is shown on the shield item, and further right-clicks with a shield are ignored until the timer ends — the same behaviour as an axe hit, but on demand and for any length of time.

### Examples

Disable the shields of everyone within 5 blocks for 5 seconds:

```
/disableshield @a[distance=..5] 5s
```

A boss attack that disables shields for 10 seconds:

```
/disableshield @a[distance=..15] 10s
```
