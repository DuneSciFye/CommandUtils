---
description: Points a compass at a location or an entity
---

# Set Compass Tracking

Usage: /setcompasstracking \<Player> \<[Slot](../arguments/slot-argument.md)> \<[World](../arguments/world-argument.md)> \<[Location](../arguments/location-argument.md)>

Usage: /setcompasstracking \<Player> \<[Slot](../arguments/slot-argument.md)> \<Target>

* Player - The player holding the compass
* Slot - Which slot the compass is in
* World - The world the location is in
* Location - The point to track
* Target - An entity to point at, using its position at the moment the command runs

The compass is set to lodestone tracking, so it points at the exact coordinates and keeps working in the Nether and the End.

The needle does not follow a moving target on its own. For a manhunt compass, re-run the command on a [Loop](loop.md).

### Examples

Point a compass at spawn:

```
/setcompasstracking Steve mainhand world 0 64 0
```

Track another player:

```
/setcompasstracking Steve mainhand Alex
```

Keep it updated once a second:

```
/loop add hunt-Steve 999999 0t 1s setcompasstracking Steve mainhand Alex
```
