---
description: Finds and replaces text inside an item's lore
---

# Replace Lore

Usage: /replacelore \<Player> \<Slot> \<Text To Find> \<New Text>

Usage: /replacelore \<Player> \<main | mainhand | off | offhand | cursor> \<Text To Find> \<New Text>

* Player - The player holding the item
* Slot - Slot number, or one of the listed keywords
* Text To Find - The exact text to look for
* New Text - What to put in its place

Every lore line is searched, and every occurrence in a line is replaced. Lines that don't contain the text are left alone, so formatting and colour elsewhere survive.

For pattern matching rather than exact text, use [Replace Lore Regex](replace-lore-regex.md).

### Examples

Update a counter on the held item:

```
/replacelore Steve mainhand "Kills: 5" "Kills: 6"
```

Change a status word:

```
/replacelore Steve 0 Locked Unlocked
```
