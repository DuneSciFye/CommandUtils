---
description: Adds, changes or deletes one line of an item's lore
---

# Item Lore

Usage: /itemlore \<append | set | remove> \<Player> \<[Slot](../arguments/slot-argument.md)> \<Line> \<Content>

* Player - The player holding the item
* Slot - Slot of the item
* Line - Line number, starting at `1`. A number past the end appends to the bottom
* Content - The text. `&` colour codes work

| Function | Effect |
| --- | --- |
| `append` | Inserts a new line at that position, pushing the rest down |
| `set` | Overwrites the line already there |
| `remove` | Deletes the line. `Content` is ignored but still required |

Italics are turned off so the text shows exactly as written.

To change part of a line instead of the whole line, use [Replace Lore](replace-lore.md).

### Examples

Add a line to the bottom of the lore:

```
/itemlore append Steve mainhand 99 &7Forged in the nether
```

Overwrite the first line with a counter:

```
/itemlore set Steve mainhand 1 &7Kills: &f120
```

Delete the second line:

```
/itemlore remove Steve mainhand 2 x
```
