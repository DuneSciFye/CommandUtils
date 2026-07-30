---
description: Detailed information on the Location Argument
---

# Location Argument

The Location Argument accepts decimal coordinates, which makes it suitable for particles, projectiles and entity positions. Commands that act on whole blocks use the [Block Location Argument](block-location-argument.md) instead.

Format: `x y z`

* Plain numbers are absolute coordinates — `100.5 64.0 -30.5`
* `~` is relative to the sender — `~ ~2 ~`
* `^` is relative to the direction the sender is facing — `^ ^ ^3`

{% hint style="info" %}
Command blocks and console are at their own position, so `~` is only useful when the command runs as a player. Most setups pass explicit coordinates from placeholders such as `%player_x%` or `%block_x%`.
{% endhint %}

### Examples

```
/spawnnodamagelightning world 100.5 64 -30.5
```

```
/raytraceparticle world ~ ~1.5 ~ flame 20 0.5
```
