---
description: Adds or removes attribute modifiers on an item
---

# Item Attribute

Usage: /itemattribute \<add> \<Player> \<[Slot](../arguments/slot-argument.md)> \<Attribute> \<ID> \<Value> \<Operation> \<Equipment Slot> \[\<Add Default Attributes>] \[\<Namespace> \<Key> \<Content>]

Usage: /itemattribute \<remove> \<Player> \<[Slot](../arguments/slot-argument.md)> \<Attribute> \<ID>

* Player - The player holding the item
* Slot - Slot of the item
* Attribute - The attribute, e.g. `generic.attack_damage`, `generic.max_health`
* ID - Name for this modifier. Reuse it to replace or remove the modifier later
* Value - How much the attribute changes by
* Operation - `ADD_NUMBER`, `ADD_SCALAR` or `MULTIPLY_SCALAR_1`
* Equipment Slot - Where the item must be for the modifier to count, e.g. `MAINHAND`, `ARMOR`, `ANY`
* Add Default Attributes _(optional)_ - Also writes the item's built-in attributes onto it, so they aren't lost when the item gains its first explicit modifier
* Namespace / Key / Content _(optional)_ - Only apply the change if the item already carries this [NBT value](../arguments/namespacedkeys.md)

| Operation | Effect |
| --- | --- |
| `ADD_NUMBER` | Adds the value to the base |
| `ADD_SCALAR` | Adds a percentage of the base |
| `MULTIPLY_SCALAR_1` | Multiplies the running total |

{% hint style="info" %}
Once an item has any explicit modifier, vanilla stops showing its built-in ones. Pass `Add Default Attributes` as `true` the first time so a sword doesn't lose its base damage.
{% endhint %}

{% hint style="warning" %}
Requires Minecraft 1.21.1 or newer. On older versions the command is not registered.
{% endhint %}

### Examples

Add 5 attack damage to the held sword:

```
/itemattribute add Steve mainhand generic.attack_damage bonus_dmg 5 ADD_NUMBER MAINHAND true
```

Give armour extra health while it is worn:

```
/itemattribute add Steve 103 generic.max_health helm_hp 4 ADD_NUMBER ARMOR true
```

Only apply the modifier to an item tagged as upgraded:

```
/itemattribute add Steve mainhand generic.attack_speed swift 0.2 ADD_SCALAR MAINHAND true myplugin tier upgraded
```

Remove a modifier by its ID:

```
/itemattribute remove Steve mainhand generic.attack_damage bonus_dmg
```
