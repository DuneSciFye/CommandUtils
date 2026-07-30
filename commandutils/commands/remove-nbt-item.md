---
description: Takes items carrying a specific plugin NBT value
---

# Remove NBT Item

Usage: /removenbtitem \<Player> \<Namespace> \<Key> \<Value> \[\<Max Amount>] \[\<Check Chest>]

* Player - The player to take from
* Namespace - Namespace of the [NamespacedKey](../arguments/namespacedkeys.md)
* Key - Key of the [NamespacedKey](../arguments/namespacedkeys.md)
* Value - The value to match. Text, decimals and whole numbers are all checked
* Max Amount _(optional)_ - Stop after removing this many. Defaults to no limit
* Check Chest _(optional)_ - Also take from the container the player has open. Defaults to `false`

Matches on the persistent data container — the values written by [Set Item NBT](set-item-nbt.md) or by other plugins such as ExecutableItems. The cursor slot is checked along with the inventory.

For NBT stored outside the plugin container, use [Remove Custom Data Item](remove-custom-data-item.md).

### Examples

Take one raid key:

```
/removenbtitem Steve myplugin type raid_key 1
```

Take every ExecutableItem with a given ID, including from an open chest:

```
/removenbtitem Steve executableitems ei-id my_item 999 true
```
