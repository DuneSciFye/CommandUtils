---
description: Strikes cosmetic lightning that damages nothing
---

# Spawn No Damage Lightning

Usage: /spawnnodamagelightning \<[World](../arguments/world-argument.md)> \<[Location](../arguments/location-argument.md)>

* World - The world the location is in
* Location - Where the bolt strikes

The flash and thunder happen as normal, but nothing takes damage. Fires are still not started, so it is safe to use over builds.

### Examples

```
/spawnnodamagelightning world 100 64 -30
```

Lightning on a mob when it dies:

```
/spawnnodamagelightning %world% %player_x% %player_y% %player_z%
```
