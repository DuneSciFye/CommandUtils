---
description: Sets entities' powder snow freeze level
---

# Set Freeze Ticks

Usage: /setfreezeticks \<Entities> \<Freeze Ticks>

* Entities - The entities to affect
* Freeze Ticks - Ticks of freezing. `0` clears it

The screen frost overlay ramps up as the value rises. At `140` the entity is fully frozen and starts taking freeze damage, exactly as if it had been standing in powder snow. The value ticks back down on its own once the entity is out of the snow.

### Examples

Fully freeze a player:

```
/setfreezeticks Steve 140
```

Apply light frost for the visual effect only:

```
/setfreezeticks @a[distance=..5] 60
```

Thaw everyone out:

```
/setfreezeticks @a 0
```
