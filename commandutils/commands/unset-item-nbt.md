---
description: Deletes a custom value from an item
---

# Unset Item NBT

Usage: /unsetitemnbt \<Player> \<[Slot](../arguments/slot-argument.md)> \<Namespace> \<Key>

* Player - The player holding the item
* Slot - Slot of the item
* Namespace - Namespace of the [NamespacedKey](../arguments/namespacedkeys.md)
* Key - Key of the [NamespacedKey](../arguments/namespacedkeys.md)

Removes the key set by [Set Item NBT](set-item-nbt.md). Nothing happens if the key was never there.

### Examples

```
/unsetitemnbt Steve mainhand myplugin type
```

Strip a used charge marker off an item:

```
/unsetitemnbt Steve mainhand score usage
```
