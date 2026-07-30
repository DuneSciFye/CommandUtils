---
description: Shows a line of text above the player's hotbar
---

# Send Action Bar

Usage: /sendactionbar \<Player> \<Content>

* Player - The player to show it to
* Content - The text. `&` colour codes work

The action bar fades on its own after a few seconds. Sending a new one replaces what's there, so a [Loop](loop.md) can keep it on screen as a live readout.

### Examples

```
/sendactionbar Steve &cLow health!
```

A mana bar refreshed twice a second:

```
/loop add mana-Steve 999999 0t 10t sendactionbar Steve &bMana: &f$myplugin_mana$
```
