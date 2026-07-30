---
description: Swaps blocks in the direction the player is looking
---

# Replace In Facing

Usage: /replaceinfacing \<[World](../arguments/world-argument.md)> \<[Block Location](../arguments/block-location-argument.md)> \<Player> \<Radius> \<Depth> \<Blocks To Replace From> \<Blocks To Replace To> \[\<Apply Physics>]

* World - The world the location is in
* Location - Coordinates of the centre block
* Player - Whose facing direction is used
* Radius - Half the width and height of the wall. `1` is 3×3, `2` is 5×5
* Depth - How many blocks deep to reach into the surface
* Blocks To Replace From - List of materials that may be replaced
* Blocks To Replace To - List of materials to replace them with. With several, one is picked at random per block
* Apply Physics _(optional)_ - Whether neighbours update, so torches pop off and water flows. Defaults to `true`

Both block lists are plain material lists — tags and `!` exclusions are not supported here. Use [Replace In Radius](replace-in-radius.md) if you need a full [whitelist](../arguments/whitelisted-blocks.md).

{% hint style="info" %}
No claim check is done, so this command replaces blocks anywhere the coordinates point.
{% endhint %}

### Examples

Pave a 5×5 wall of dirt into stone bricks:

```
/replaceinfacing world 100 64 -30 @p 2 1 dirt stone_bricks
```

Turn stone into a random mix of two blocks, without triggering physics:

```
/replaceinfacing world 100 64 -30 @p 2 2 stone "andesite diorite" false
```
