---
description: Bone meals blocks
---

# Bone Meal Block

> Usage: /bonemealblock <[World](../arguments/world-argument.md)> <[Block Location](../arguments/block-location-argument.md)> \[\<Amount>] \[\<Radius>] \[\<Affect Target Block>]

* World - The world of the block
* Block Location - Coordinates of the block
* Amount - Number of times to bonemeal
  * Defaults to 1
* Radius - Radius of bonemeal effect
  * Defaults to 0
* Affect Target Block - If the designated block at the coordinates should be affected
  * Defaults to true
  * Useful if the center block is already being bonemealed.

{% hint style="info" %}
Example of an bone meal that bone meals in a 5x5:

Attach this command to a bone meal item:

```
/bonemealblock %world% %block_x% %block_y% %block_z% 1 2 false
```
{% endhint %}

