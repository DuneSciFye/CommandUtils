---
description: Toggles the gravity of a block
---

# Block Gravity

Usage: /blockgravity <[World](../arguments/world-argument.md)> <[Block Location](../arguments/block-location-argument.md)> \[\<Gravity Enabled>] \[\<Radius>]

* The World of the Block
* The Coordinates of the Block
* Boolean, determines if the block will have gravity
* Radius of how many blocks to affect

{% hint style="info" %}
This command stores data inside the block using CustomBlockData, which stores it in the level folder. A listener listens for the [EntityChangeBlockEvent](https://jd.papermc.io/paper/1.21.4/org/bukkit/event/entity/EntityChangeBlockEvent.html) and cancels it if the block contains our stored data.
{% endhint %}
