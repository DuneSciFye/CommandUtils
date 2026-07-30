---
description: Copies one property from a template item onto an item in a slot
---

# Set Item

Usage: /setitem \<Player> \<[Slot](../arguments/slot-argument.md)> \<Item> \[\<Function>]

* Player - The player holding the item
* Slot - Slot of the item to change
* Item - The template item the property is taken from
* Function _(optional)_ - Which property to copy

| Function | Copies |
| --- | --- |
| `material` | The item type, keeping the existing name, lore and NBT |
| `custommodeldata` | The custom model data value |
| `attributemodifiers` | All attribute modifiers |
| `equippable` | The equippable component, so an item can be worn in a new slot |
| `fireworkcolor` | The burst and fade colours, keeping shape, flicker, trail and power |
| `max_reach` | The maximum attack range |
| _(omitted)_ | Material, custom model data and attribute modifiers together |

The point is to change one part of an item in place. A tool keeps its name, lore, enchantments and durability while its texture, stats or type are swapped.

### Examples

Upgrade a pickaxe's material, keeping everything else:

```
/setitem Steve mainhand netherite_pickaxe material
```

Retexture an item by copying custom model data:

```
/setitem Steve mainhand "paper[custom_model_data={floats:[5]}]" custommodeldata
```

Give an item longer reach:

```
/setitem Steve mainhand "trident[attack_range={max_reach:6}]" max_reach
```
