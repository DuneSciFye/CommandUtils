---
description: Stops a block from falling, or lets it fall again
---

# Block Gravity

Usage: /blockgravity \<[World](../arguments/world-argument.md)> \<[Location](../arguments/block-location-argument.md)> \[\<Gravity Enabled>] \[\<Radius>]

* World - The world the location is in
* Location - Coordinates of the block
* Gravity Enabled _(optional)_ - `false` disables falling, `true` restores it. Omit to toggle
* Radius _(optional)_ - Cube radius around the location. Defaults to `0` (single block)

Gravity is stored on the block itself, so it survives restarts and stays with the block until it is changed back or broken. Affected sand, gravel, concrete powder and anvils stay in place with nothing supporting them.

### Examples

Make a single gravel block float:

```
/blockgravity world 100 64 -30 false
```

Freeze a 5×5×5 cube of falling blocks:

```
/blockgravity world 100 64 -30 false 2
```

Toggle gravity back on:

```
/blockgravity world 100 64 -30
```
