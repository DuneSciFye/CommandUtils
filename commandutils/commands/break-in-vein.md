---
description: Vein-mines connected blocks of the same type
---

# Break In Vein

Usage: /breakinvein \<[world](../arguments/world-argument.md)> \<[location](../arguments/block-location-argument.md)> \[\<Whitelisted Blocks>] \[\<Max Blocks>]

Usage: /breakinvein \<[world](../arguments/world-argument.md)> \<[location](../arguments/block-location-argument.md)> \<Player> \[\<Whitelisted Blocks>] \[\<Trigger Block Break Event>] \[\<Max Blocks>] \[\<Check Claim>] \[\<Auto Pickup>] \[\<Break Original Block>] \[\<Silk Touch>]

* World - The world the location is in
* Location - Coordinates of the block/location
* Whitelisted Blocks _(optional)_ - Block predicate / whitelist of blocks that may be affected. Accepts a config-defined predicate name
* Max Blocks _(optional)_ - Maximum number of blocks to break
* Player - The player to affect
* Trigger Block Break Event _(optional)_ - Whether to fire a BlockBreakEvent for each block
* Check Claim _(optional)_ - Whether to respect land claims / region protections
* Auto Pickup _(optional)_ - Whether drops go straight into the player's inventory
* Break Original Block _(optional)_ - Whether the starting block is also broken
* Silk Touch _(optional)_ - Whether to break with Silk Touch behaviour
