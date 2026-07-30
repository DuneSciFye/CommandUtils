---
description: Cages a player inside temporary blocks
---

# Block Prison

Usage: /blockprison \<[World](../arguments/world-argument.md)> \<[Location](../arguments/block-location-argument.md)> \<Player> \<Block State> \<Radius> \<Height> \[\<Duration>] \[\<Floor>]

* World - The world the location is in
* Location - Coordinates of the cage centre (its bottom layer)
* Player - The player the temporary blocks belong to
* Block State - The block the cage is built from, with optional block data
* Radius - Distance from the centre to each wall
* Height - How many blocks tall the cage is
* Duration _(optional)_ - How long the cage lasts, e.g. `5s`, `1m`, `100t`. Defaults to `100t`
* Floor _(optional)_ - Whether a floor is placed one block below the centre. Defaults to `false`

Four walls and a ceiling are built. Only air is replaced, so existing terrain is never overwritten, and the cage disappears on its own when the duration runs out.

{% hint style="warning" %}
Requires **SCore / ExecutableItems** — the blocks are placed through SCore's `SETTEMPBLOCKPOS` activator. Use [Set Temp Block](set-temp-block.md) if you don't run those plugins.
{% endhint %}

### Examples

Trap a player in a 5×5 glass box, 3 blocks tall, for 10 seconds:

```
/blockprison world 100 64 -30 @p glass 2 3 10s
```

The same box with a floor, so the player can't fall out:

```
/blockprison world 100 64 -30 @p glass 2 3 10s true
```
