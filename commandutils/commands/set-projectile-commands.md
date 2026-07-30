---
description: Attaches commands to a projectile that fire when it hits
---

# Set Projectile Commands

Usage: /setprojectilecommands \<Projectile> \<on_block_hit | on_entity_hit> \<Commands>

* Projectile - The projectile entity to tag. It must already exist
* on_block_hit - Runs when it lands on a block
* on_entity_hit - Runs when it strikes an entity
* Commands - Commands to run, separated by `,,`

`{projectile_uuid}` in the commands is replaced with the projectile's UUID, so the projectile itself can be targeted with an `@e` selector from inside the command.

Both triggers can be set on the same projectile with two calls. Nothing is saved, so the commands are lost if the server restarts before the projectile lands.

### Examples

An arrow that calls down lightning where it lands:

```
/setprojectilecommands @e[type=arrow,limit=1,sort=nearest] on_block_hit execute at @e[type=arrow,limit=1] run spawnnodamagelightning world ~ ~ ~
```

A snowball that freezes whatever it strikes:

```
/setprojectilecommands @e[type=snowball,limit=1,sort=nearest] on_entity_hit execute at @e[type=snowball,limit=1] run setfreezeticks @e[distance=..2,limit=1] 140
```
