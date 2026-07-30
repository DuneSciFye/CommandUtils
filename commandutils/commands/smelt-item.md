---
description: Cooks dropped items where they lie
---

# Smelt Item

Usage: /smeltitem \<Items>

* Items - The dropped item entities to smelt

Each item entity is swapped for its furnace result — raw iron becomes an iron ingot, sand becomes glass. Items with no smelting recipe, and entities that aren't dropped items, are left alone. Stack sizes carry over.

To smelt items as part of a mining tool, [Select Items](select-items.md) and [Select Blocks](select-blocks.md) both have an `ITEM:SMELT` function.

### Examples

Auto-smelt everything dropped near a player:

```
/smeltitem @e[type=item,distance=..5]
```

Smelt the drops from a specific mining spot:

```
/smeltitem @e[type=item,x=100,y=64,z=-30,distance=..2]
```
