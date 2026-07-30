---
description: Deletes blocks in the direction the player is looking, with no drops
---

# Remove In Facing

Usage: /removeinfacing \<[World](../arguments/world-argument.md)> \<[Block Location](../arguments/block-location-argument.md)> \<Radius> \<Depth> \<Player>

Usage: /removeinfacing \<[World](../arguments/world-argument.md)> \<[Block Location](../arguments/block-location-argument.md)> \<Radius> \<Depth> \<Player> \<Whitelisted Blocks>

Usage: /removeinfacing \<[World](../arguments/world-argument.md)> \<[Block Location](../arguments/block-location-argument.md)> \<Radius> \<Depth> \<Player> \<whitelist> \<Command Defined Whitelist>

* World - The world the location is in
* Location - Coordinates of the centre block
* Radius - Half the width and height of the wall. `1` is 3×3, `2` is 5×5
* Depth - How many blocks deep to reach into the surface
* Player - Whose facing direction is used, and whose claim permissions are checked
* Whitelisted Blocks - The name of a whitelist defined in [config.yml](../config.md)
* whitelist - Literal keyword, required before an inline list
* Command Defined Whitelist - An inline list of materials and tags, e.g. `"#leaves !oak_leaves"`

Same shape as [Break In Facing](break-in-facing.md), but the blocks are simply deleted — no drops, no block break event.

{% hint style="info" %}
This command splits the whitelist into two forms: a bare config name, or the `whitelist` keyword followed by an inline list. See [Whitelisted Blocks](../arguments/whitelisted-blocks.md) for what the entries mean.
{% endhint %}

### Examples

Delete a 3×3 wall one block deep:

```
/removeinfacing world 100 64 -30 1 1 @p
```

Delete only leaves, using the config whitelist named `axe`:

```
/removeinfacing world 100 64 -30 2 1 @p axe
```

Delete only glass, listed inline:

```
/removeinfacing world 100 64 -30 2 1 @p whitelist "glass glass_pane"
```
