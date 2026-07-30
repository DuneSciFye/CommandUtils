---
description: Stores a custom value on an item
---

# Set Item NBT

Usage: /setitemnbt \<Player> \<[Slot](../arguments/slot-argument.md)> \<Namespace> \<Key> \[\<Content>]

* Player - The player holding the item
* Slot - Slot of the item
* Namespace - Namespace of the [NamespacedKey](../arguments/namespacedkeys.md)
* Key - Key of the [NamespacedKey](../arguments/namespacedkeys.md)
* Content _(optional)_ - The value. Numbers are stored as doubles, everything else as text. Defaults to an empty string

The value goes into the item's persistent data container, so it survives being dropped, stored and moved between worlds, and is invisible in-game.

Numeric values can then be changed with [Add Item NBT](add-item-nbt.md), read with `%stringutils_inventoryinfo_...%`, and matched by [Remove NBT Item](remove-nbt-item.md).

### Examples

Tag an item so other commands can recognise it:

```
/setitemnbt Steve mainhand myplugin type raid_key
```

Store a numeric counter:

```
/setitemnbt Steve mainhand score usage 0
```
