---
description: Fires a projectile that breaks blocks as it flies
---

# Spawn Block Breaker

Usage: /spawnblockbreaker \<[Location](../arguments/location-argument.md)> \[\<Yaw> \<Pitch>] \[\<Item>] \[\<Vector Multiplier>] \[\<Radius> \<Period> \<Max Time>] \[\<[Whitelisted Blocks](../arguments/whitelisted-blocks.md)>]

Usage: /spawnblockbreaker \<Player> \[\<Item>] \[\<Vector Multiplier>] \[\<Radius> \<Period> \<Max Time>] \[\<[Whitelisted Blocks](../arguments/whitelisted-blocks.md)>] \[\<Check Claim>] \[\<Generate Block Break Event>] \[\<Auto Pickup>]

* Location - Where the projectile starts
* Yaw / Pitch - The direction it flies. `0 0` is south and level
* Player - Fires from this player's position, in the direction they are looking
* Item _(optional)_ - The item shown as the flying projectile
* Vector Multiplier _(optional)_ - Launch speed. Defaults to `1.0`
* Radius _(optional)_ - Cube radius of blocks broken around the projectile each tick
* Period _(optional)_ - How often it breaks blocks, e.g. `1t`
* Max Time _(optional)_ - How long it keeps breaking before stopping
* Whitelisted Blocks _(optional)_ - Which blocks it may break
* Check Claim _(optional)_ - Respects claims and regions. Defaults to `false`
* Generate Block Break Event _(optional)_ - Breaks blocks as the player, so other plugins see it. Defaults to `false`
* Auto Pickup _(optional)_ - Reserved for future use

The projectile is a snowball with a custom item texture. It carves a tunnel through everything in its path, and drops the blocks it breaks on the ground.

{% hint style="warning" %}
With no whitelist this breaks every block it passes through, bedrock aside. Always pass one on a live server.
{% endhint %}

### Examples

A drill that tunnels 3 blocks wide for 5 seconds:

```
/spawnblockbreaker Steve netherite_pickaxe 1.5 1 1t 5s pickaxe
```

A slow boring projectile fired from fixed coordinates:

```
/spawnblockbreaker world 100 64 -30 90 0 tnt 0.5 1 2t 10s "#mineable/pickaxe"
```

Respect claims and fire block break events, so other plugins react:

```
/spawnblockbreaker Steve netherite_pickaxe 1.5 1 1t 5s pickaxe true true
```
