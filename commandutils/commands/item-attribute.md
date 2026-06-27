---
description: Adds or removes attribute modifiers on an item
---

# Item Attribute

Usage: /itemattribute \<add> \<Player> \<[slot](../arguments/slot-argument.md)> \<Attribute> \<ID> \<Value> \<Operation> \<Equipment Slot> \[\<Add Default Attributes>] \[\<Namespace>]

Usage: /itemattribute \<remove> \<Player> \<[slot](../arguments/slot-argument.md)> \<Attribute> \<ID>

* Player - The player to affect
* Slot - Slot of the item
* Attribute - The attribute to modify
* ID - A unique identifier
* Value - The numerical value
* Operation - How the modifier is applied (add_number, add_scalar, multiply_scalar_1)
* Equipment Slot - The equipment slot the modifier applies to
* Add Default Attributes _(optional)_ - Whether to add the item's default attributes
* Namespace _(optional)_ - Namespace of the NBT key

> Requires Minecraft 1.21.1 or newer.
