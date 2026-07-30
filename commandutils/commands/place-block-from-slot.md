---
description: Places the block sitting in a given slot
---

# Place Block From Slot

Usage: /placeblockfromslot \<[World](../arguments/world-argument.md)> \<[Location](../arguments/location-argument.md)> \<[Slot](../arguments/slot-argument.md)> \[\<Consume from Inventory>] \[\<Trigger Block Place Event>]

* World - The world the location is in
* Location - Where the block goes. Must currently be air
* Slot - Slot to take the block from
* Consume from Inventory _(optional)_ - Takes one item from that slot. Defaults to `true`
* Trigger Block Place Event _(optional)_ - Fires a block place event first, so protection plugins can cancel it. Defaults to `true`

Runs as the player who executed it, so call it through `/execute as <player>` from the console or a command block.

Nothing happens if the slot is empty or holds something that isn't a block.

Unlike [Place Block From Inv](place-block-from-inv.md), the slot is fixed, so the player controls exactly which block is used by choosing what to put there.

### Examples

Place whatever block is in the player's offhand:

```
/placeblockfromslot %world% %block_x% %block_y% %block_z% offhand
```

Place from hotbar slot 8 without consuming it:

```
/placeblockfromslot world 100 64 -30 8 false
```
