---
description: Changes who gets blamed for a TNT explosion
---

# Set TNT Source

Usage: /settntsource \<TNTs> \<Entity Sources>

* TNTs - The primed TNT entities to change
* Entity Sources - Who to credit. With several, one is picked at random per TNT

The source is what protection and logging plugins read to decide whether an explosion is allowed and who caused it. Setting it lets TNT fired by a machine or a boss count as belonging to a specific player.

Entities in the selection that aren't primed TNT are skipped.

### Examples

Credit a player with every piece of TNT nearby:

```
/settntsource @e[type=tnt,distance=..10] Steve
```

Spread the blame across a team at random:

```
/settntsource @e[type=tnt,distance=..20] @a[team=red]
```
