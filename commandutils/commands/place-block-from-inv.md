---
description: Places a block, paying for it from the player's inventory
---

# Place Block From Inv

Usage: /placeblockfrominv \<[World](../arguments/world-argument.md)> \<[Location](../arguments/location-argument.md)> \<Material> \[\<Consume from Inventory>] \[\<Trigger Block Place Event>]

* World - The world the location is in
* Location - Where the block goes. Must currently be air
* Material - The block to place
* Consume from Inventory _(optional)_ - Takes one matching item from the player. Defaults to `true`
* Trigger Block Place Event _(optional)_ - Fires a block place event first, so protection plugins can cancel it. Defaults to `true`

Runs as the player who executed it, so call it through `/execute as <player>` from the console or a command block.

Only plain items count towards the cost — a stack with a custom name or NBT is skipped. If the player has none, nothing is placed.

Use [Place Block From Slot](place-block-from-slot.md) to take from one specific slot instead of searching the inventory.

### Examples

A bridge-building item that consumes the player's own blocks:

```
/placeblockfrominv %world% %block_x% %block_y% %block_z% cobblestone
```

Place a block for free, ignoring protections:

```
/placeblockfrominv world 100 64 -30 stone false false
```
