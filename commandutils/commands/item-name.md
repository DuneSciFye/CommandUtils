---
description: Renames an item, or clears its name
---

# Item Name

Usage: /itemname \<set> \<[Slot](../arguments/slot-argument.md)> \<Name>

Usage: /itemname \<reset> \<[Slot](../arguments/slot-argument.md)>

* Slot - Slot of the item
* Name - The new name. `&` colour codes work

Runs as the player who executed it, so call it through `/execute as <player>` from the console or a command block.

The italics vanilla adds to renamed items are turned off, so the name shows exactly as written. `reset` puts the default item name back.

### Examples

Rename the held item:

```
/itemname set mainhand &6Excalibur
```

Rename with several colours:

```
/itemname set mainhand &c&lBlood &4&lReaper
```

Clear a custom name:

```
/itemname reset mainhand
```
