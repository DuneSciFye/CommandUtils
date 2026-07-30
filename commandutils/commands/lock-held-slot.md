---
description: Stops the player switching hotbar slots for a while
---

# Lock Held Slot

Usage: /lockheldslot \<Duration> \[\<Slot>]

* Duration - How long the slot is locked, e.g. `5s`, `1m`, `100t`
* Slot _(optional)_ - Hotbar slot `0`–`8` to switch to first. Omit to lock whatever they are holding

Runs as the player who executed it, so call it through `/execute as <player>` from the console or a command block.

Scroll and number-key switching are both blocked. Running the command again replaces the previous timer rather than stacking.

### Examples

Lock the current slot for 3 seconds:

```
/lockheldslot 3s
```

Force the player onto their first hotbar slot and hold them there:

```
/execute as Steve run lockheldslot 10s 0
```
