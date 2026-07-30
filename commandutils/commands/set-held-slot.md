---
description: Switches which hotbar slot the player is holding
---

# Set Held Slot

Usage: /setheldslot \<Slot>

* Slot - Hotbar slot, `0` (leftmost) to `8` (rightmost)

Runs as the player who executed it, so call it through `/execute as <player>` from the console or a command block.

To stop them switching away again, follow it with [Lock Held Slot](lock-held-slot.md).

### Examples

Switch to the first hotbar slot:

```
/setheldslot 0
```

Force a weapon into hand at the start of a duel:

```
/execute as Steve run setheldslot 0
```
