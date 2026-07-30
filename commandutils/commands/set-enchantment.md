---
description: Sets an enchantment on an item, above the normal limits
---

# Set Enchantment

Usage: /setenchantment \<[Slot](../arguments/slot-argument.md)> \<Enchantment> \<Level>

* Slot - Slot of the item
* Enchantment - The enchantment to apply
* Level - The level. `0` removes it

Runs as the player who executed it, so call it through `/execute as <player>` from the console or a command block.

The enchantment is applied unsafely: levels above the vanilla maximum are allowed, and so are enchantments that don't normally fit the item. Any existing level of that enchantment is replaced rather than stacked.

### Examples

Sharpness 10 on the held sword:

```
/setenchantment mainhand sharpness 10
```

Efficiency on something that normally can't have it:

```
/setenchantment mainhand efficiency 5
```

Remove an enchantment:

```
/setenchantment mainhand fire_aspect 0
```
