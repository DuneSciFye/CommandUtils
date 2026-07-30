---
description: Swaps a block for another one and puts it back after a while
---

# Set Temp Block

Usage: /settempblock \<[World](../arguments/world-argument.md)> \<[Block Location](../arguments/block-location-argument.md)> \<Block State> \<Duration> \[\<Show Breaking>] \[\<Drop Block>] \[\<[Whitelisted Blocks](../arguments/whitelisted-blocks.md)>]

* World - The world the location is in
* Location - Coordinates of the block
* Block State - The block to place, with optional block data such as `oak_stairs[facing=east]`
* Duration - How long it stays, e.g. `5s`, `1m`, `100t`
* Show Breaking _(optional)_ - Plays the cracking animation as the timer runs down, and block particles when it reverts. Defaults to `false`
* Drop Block _(optional)_ - Drops the temporary block's items when it reverts. Defaults to `false`
* Whitelisted Blocks _(optional)_ - Only replaces the block if it matches. Nothing happens otherwise

The block that was there is remembered and restored exactly, block data included. No physics update is applied in either direction, so neighbours are left alone.

Run through `/execute as <player>`, the command checks that player's claim permissions before placing anything.

### Examples

Block a doorway with cobblestone for 5 seconds:

```
/settempblock world 100 64 -30 cobblestone 5s
```

A crumbling platform that cracks apart over 3 seconds:

```
/settempblock world 100 64 -30 stone 3s true
```

Grow a temporary crop that can be harvested before it disappears:

```
/settempblock world 100 64 -30 wheat[age=7] 30s false true
```

Only cover the block if it is air, leaving terrain untouched:

```
/settempblock world 100 64 -30 ice 10s false false air
```
