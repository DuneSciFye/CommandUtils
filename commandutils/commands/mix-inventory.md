---
description: Shuffles the items in a player's hotbar
---

# Mix Inventory

Usage: /mixinventory \<Slot>

* Slot - The last slot to shuffle, `0`–`40`. `8` shuffles the whole hotbar

Runs as the player who executed it, so call it through `/execute as <player>` from the console or a command block.

Slots from `0` up to and including the one given are shuffled among themselves; the rest of the inventory is untouched.

If the player is protected by [Prevent Mix Inventory](prevent-mix-inventory.md), nothing is shuffled and that command's stored commands run instead.

### Examples

Scramble the hotbar:

```
/mixinventory 8
```

Scramble only the first four slots:

```
/mixinventory 3
```
