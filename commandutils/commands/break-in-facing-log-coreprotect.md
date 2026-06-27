---
description: Breaks blocks in the facing direction and logs them to CoreProtect
---

# Break In Facing Log CoreProtect

Usage: /breakinfacinglogcoreprotect \<[world](../arguments/world-argument.md)> \<[location](../arguments/block-location-argument.md)> \<Player> \<Radius> \<Depth> \[\<forcedrop>]

Usage: /breakinfacinglogcoreprotect \<[world](../arguments/world-argument.md)> \<[location](../arguments/block-location-argument.md)> \<Player> \<Radius> \<Depth> \<Whitelisted Blocks> \[\<forcedrop>]

Usage: /breakinfacinglogcoreprotect \<[world](../arguments/world-argument.md)> \<[location](../arguments/block-location-argument.md)> \<Player> \<Radius> \<Depth> \<Whitelisted Blocks> \<Drop>

* World - The world the location is in
* Location - Coordinates of the block/location
* Player - The player to affect
* Radius - Radius of the effect
* Depth - How far in the facing direction to reach
* Whitelisted Blocks - Block predicate / whitelist of blocks that may be affected. Accepts a config-defined predicate name
* Drop - The item to drop instead of the block's normal drops

> Requires CoreProtect to be installed for block logging.
