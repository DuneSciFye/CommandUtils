---
description: Creates a player's head and gives or drops it
---

# Get Player Head

Usage: /getplayerhead \<Player> \[\<Target>]

Usage: /getplayerhead \<Player> \<[World](../arguments/world-argument.md)> \<[Location](../arguments/location-argument.md)>

* Player - Whose face the head shows
* Target _(optional)_ - Who receives it. Defaults to whoever ran the command
* World / Location - Drop the head here instead of giving it to anyone

The head is textured from the player's real skin, so it keeps working after skin changes.

### Examples

Give yourself a player's head:

```
/getplayerhead Alex
```

Give it to someone else:

```
/getplayerhead Alex Steve
```

Drop a head where a player died:

```
/getplayerhead Steve world 100 64 -30
```
