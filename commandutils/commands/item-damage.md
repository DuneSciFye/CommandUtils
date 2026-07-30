---
description: Changes an item's durability damage
---

# Item Damage

Usage: /itemdamage \<Player> \<[Slot](../arguments/slot-argument.md)> \<add | set | remove> \<Amount>

* Player - The player holding the item
* Slot - Slot of the item
* Amount - Points of damage

Damage counts **up** from `0` (undamaged) to the item's maximum durability, so `add` wears the item down and `remove` repairs it. Items that can't take damage are ignored.

Unbreaking is not applied — the amount given is the amount taken. The item is not destroyed when the damage passes its maximum, it just shows an empty durability bar.

### Examples

Wear a tool down by 10 points:

```
/itemdamage Steve mainhand add 10
```

Fully repair the held item:

```
/itemdamage Steve mainhand set 0
```

Repair 50 points:

```
/itemdamage Steve mainhand remove 50
```
