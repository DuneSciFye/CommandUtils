---
description: Highlights matching blocks in a radius with particles
---

# Highlight Blocks

Usage: /highlightblocks \<[world](../arguments/world-argument.md)> \<[location](../arguments/block-location-argument.md)> \<Radius> \<Block> \<Particle> \[\<Particle Count>] \[\<Particle Offset>] \[\<Particle Speed>] \[\<Number Of Intervals>] \[\<Particle Spawn Interval>]

Usage: /highlightblocks \<[location](../arguments/block-location-argument.md)> \<Radius> \<Block> \<Particle> \[\<Particle Count>] \[\<Particle Offset>] \[\<Particle Speed>] \[\<Number Of Intervals>] \[\<Particle Spawn Interval>]

Usage: /highlightblocks \<[world](../arguments/world-argument.md)> \<[location](../arguments/block-location-argument.md)> \<Radius> \<Whitelisted Blocks> \<Particle> \[\<Particle Count>] \[\<Particle Offset>] \[\<Particle Speed>] \[\<Number Of Intervals>] \[\<Particle Spawn Interval>]

* World - The world the location is in
* Location - Coordinates of the block/location
* Radius - Radius of the effect
* Block - Block predicate to match
* Particle - The particle to display
* Particle Count _(optional)_ - Number of particles
* Particle Offset _(optional)_ - Random spread of the particles
* Particle Speed _(optional)_ - Speed of the particles
* Number Of Intervals _(optional)_ - Number of times to repeat
* Particle Spawn Interval _(optional)_ - Ticks between each particle spawn
* Whitelisted Blocks - Block predicate / whitelist of blocks that may be affected. Accepts a config-defined predicate name
