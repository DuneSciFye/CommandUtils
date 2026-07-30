---
description: Breaks a whole connected vein of blocks
---

# Break In Vein

Usage: /breakinvein \<[World](../arguments/world-argument.md)> \<[Location](../arguments/location-argument.md)> \[\<[Whitelisted Blocks](../arguments/whitelisted-blocks.md)>] \[\<Max Blocks>]

Usage: /breakinvein \<[World](../arguments/world-argument.md)> \<[Location](../arguments/location-argument.md)> \<Player> \[\<[Whitelisted Blocks](../arguments/whitelisted-blocks.md)>] \[\<Trigger Block Break Event>] \[\<Max Blocks>] \[\<Check Claim>] \[\<Auto Pickup>] \[\<Break Original Block>] \[\<Silk Touch>]

* World - The world the location is in
* Location - Coordinates of the block the vein starts from
* Player - The player credited with the break. Their held item decides the drops
* Whitelisted Blocks _(optional)_ - Which blocks count as part of the vein. Defaults to blocks of the same type as the starting block
* Trigger Block Break Event _(optional)_ - Fires a `BlockBreakEvent` for each block so other plugins react. Defaults to `true`
* Max Blocks _(optional)_ - Stops after this many drops. Defaults to `80`
* Check Claim _(optional)_ - Stops the vein at land it can't be broken in. Defaults to `false`
* Auto Pickup _(optional)_ - Puts drops straight into the player's inventory, dropping only the overflow. Defaults to `false`
* Break Original Block _(optional)_ - Whether the starting block is broken too. Defaults to `true`. Set to `false` when the block has already been broken by the player
* Silk Touch _(optional)_ - Drops the blocks themselves instead of their normal drops. Defaults to `false`

The vein spreads to all 26 neighbours of each matching block, including diagonals, so ore blobs are picked up in one pass.

The first form takes no player: it breaks the vein with no held item, no claim check and no block break events — useful from the console or a command block.

{% hint style="info" %}
Defaults for `Max Blocks`, `Check Claim` and `Trigger Block Break Event` can be changed under `Commands.BreakInVein` in [config.yml](../config.md).
{% endhint %}

### Examples

Vein mine an ore blob, up to 80 blocks:

```
/breakinvein world 100 32 -30
```

A vein-mining pickaxe: the player already broke the block, so skip it and pick up the drops:

```
/breakinvein %world% %block_x% %block_y% %block_z% %player_name% "#coal_ores #iron_ores #gold_ores #diamond_ores" true 64 true true false
```

Cut down a whole tree without dropping saplings from leaves:

```
/breakinvein world 100 64 -30 @p "#logs" true 200
```
