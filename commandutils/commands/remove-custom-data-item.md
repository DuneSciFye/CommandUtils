---
description: Takes items carrying a custom NBT tag
---

# Remove Custom Data Item

Usage: /removecustomitem \<Player> \<Key> \<Value> \[\<Max Amount>] \[\<Check Chest>]

* Player - The player to take from
* Key - The NBT tag name, with no namespace
* Value - The value to match. Text, decimals and whole numbers are all checked
* Max Amount _(optional)_ - Stop after removing this many. Defaults to no limit
* Check Chest _(optional)_ - Also take from the container the player has open. Defaults to `false`

Reads raw item NBT rather than the plugin data container, which is what most third-party custom item plugins write to. The cursor slot is checked along with the inventory.

For values written by [Set Item NBT](set-item-nbt.md), use [Remove NBT Item](remove-nbt-item.md) instead.

### Examples

Take one item tagged as a quest token:

```
/removecustomitem Steve quest_id dragon_slayer 1
```

Take every matching item, including from the open container:

```
/removecustomitem Steve tier 3 999 true
```
