---
description: Runs a list of functions on dropped items in a radius
---

# Select Items

Usage: /selectitems \<[World](../arguments/world-argument.md)> \<[Location](../arguments/location-argument.md)> \<Player> \<Radius> \<Materials> \<Command Separator> \<Placeholder Surrounder> \<Custom Placeholders> \<Functions>

* World - The world the location is in
* Location - Coordinates of the centre
* Player - The player the actions run as, and whose claim permissions are checked
* Radius - How far from the centre to look, in blocks
* Materials - Which item types to affect. Pass `""` to affect every dropped item
* Command Separator - String that separates the functions, e.g. `;`
* Placeholder Surrounder - Character used in place of `%` in the function list
* Custom Placeholders - Kept for compatibility
* Functions - The functions to run on each item, in order

Works on item entities lying on the ground, not on inventories.

## Functions

| Function | Effect |
| --- | --- |
| `ITEM:REMOVE` | Deletes the item entity |
| `ITEM:SMELT` | Turns the item into its furnace result, in place |
| `ITEM:AUTO_PICKUP` | Copies the item into the player's inventory |
| `ITEM:AUTO_PICKUP_AND_REMOVE` | Puts the item in the player's inventory and removes the entity only if it fully fit |
| `ITEM:DROP` | Re-drops the item at its own location |
| `ITEM:DROP <player>` | Re-drops it at that player, by name or UUID |
| `ITEM:DROP <x> <y> <z>` | Re-drops it at those coordinates |
| `ITEM:DROP <world> <x> <y> <z>` | Re-drops it at those coordinates in that world |

Anything else in the list is run as a console command.

### Examples

Magnet effect — pull every dropped item within 8 blocks into the player's inventory:

```
/selectitems %world% %player_x% %player_y% %player_z% %player_name% 8 "" ; % true ITEM:AUTO_PICKUP_AND_REMOVE
```

Auto-smelt raw ore lying near the player:

```
/selectitems world 100 64 -30 @p 5 "raw_iron raw_gold raw_copper" ; % true ITEM:SMELT
```

Clear dropped cobblestone in a 10 block radius:

```
/selectitems world 100 64 -30 @p 10 cobblestone ; % true ITEM:REMOVE
```
