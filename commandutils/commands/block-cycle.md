---
description: Advances a copper block's oxidation stage, or toggles its wax
---

# Block Cycle

Usage: /blockcycle \<oxidize> \<[World](../arguments/world-argument.md)> \<[Location](../arguments/block-location-argument.md)>

Usage: /blockcycle \<wax> \<[World](../arguments/world-argument.md)> \<[Location](../arguments/block-location-argument.md)>

* oxidize - Advances the block one oxidation stage
* wax - Toggles the block between waxed and unwaxed
* World - The world the location is in
* Location - Coordinates of the block

`oxidize` steps through `copper → exposed → weathered → oxidized → copper`, wrapping back around at the end. Waxed blocks cycle through the waxed variants and stay waxed.

Block data is carried over, so stairs keep their shape, half and facing, slabs keep their type, doors keep their hinge and open state, and waterlogged blocks stay waterlogged. Every copper variant is supported: blocks, cut, chiselled, grates, bulbs, doors, trapdoors, stairs and slabs.

{% hint style="info" %}
Doors are handled as a whole — running the command on either half updates both.
{% endhint %}

### Examples

Age a copper block by one stage:

```
/blockcycle oxidize world 0 100 0
```

Wax or unwax a block:

```
/blockcycle wax minigames 64 48 -291
```

Age the block a player is looking at, from an item:

```
/blockcycle oxidize %world% %block_x% %block_y% %block_z%
```
