---
description: Adds a number to an existing numeric NBT value on an item
---

# Add Item NBT

Usage: /additemnbt \<Player> \<[Slot](../arguments/slot-argument.md)> \<Namespace> \<Key> \[\<Content>]

* Player - The player holding the item
* Slot - Slot of the item
* Namespace - Namespace of the [NamespacedKey](../arguments/namespacedkeys.md)
* Key - Key of the [NamespacedKey](../arguments/namespacedkeys.md)
* Content _(optional)_ - Number to add. Negative numbers subtract

{% hint style="info" %}
The key must already exist on the item and be stored as a **double**. If the key is missing, or `Content` is not a number, the command does nothing. Use [Set Item NBT](set-item-nbt.md) to create the key first.
{% endhint %}

### Examples

Add 1 to the `score:usage` counter on the held item:

```
/additemnbt @p mainhand score usage 1
```

Subtract 0.5 from `myplugin:charge` on the item in slot 3:

```
/additemnbt @p 3 myplugin charge -0.5
```
