---
description: Selects blocks in a radius and runs functions on each
---

# Select Blocks

Usage: /selectblocks \<[world](../arguments/world-argument.md)> \<[location](../arguments/block-location-argument.md)> \<Player> \<Radius> \<Whitelisted Blocks> \<Command Separator> \<Placeholder Surrounder> \<Custom Placeholders> \<Functions>

* World - The world the location is in
* Location - Coordinates of the block/location
* Player - The player to affect
* Radius - Radius of the effect
* Whitelisted Blocks - Block predicate / whitelist of blocks that may be affected. Accepts a config-defined predicate name
* Command Separator - The string used to separate individual commands
* Placeholder Surrounder - The character used to surround placeholders (e.g. %)
* Custom Placeholders - Whether CommandUtils custom placeholders are enabled
* Functions - The functions / commands to run on each match
