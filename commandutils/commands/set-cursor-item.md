---
description: Puts an item on the player's mouse cursor
---

# Set Cursor Item

Usage: /setcursoritem \<Player> \<Item> \[\<Amount>]

* Player - The player to affect
* Item - The item to place on the cursor
* Amount _(optional)_ - Stack size, `0`–`64`. Defaults to `1`

The cursor is the slot an item sits in while being dragged in an open inventory. Anything already there is replaced, so set `air` to clear it.

### Examples

Put a diamond on the cursor:

```
/setcursoritem Steve diamond 16
```

Clear whatever the player is holding on their cursor:

```
/setcursoritem Steve air
```
