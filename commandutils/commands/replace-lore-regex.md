---
description: Rewrites item lore with a regular expression
---

# Replace Lore Regex

Usage: /replaceloreregex \<Player> \<[Slot](../arguments/slot-argument.md)> \<Text To Find> \<New Text>

* Player - The player holding the item
* Slot - Slot of the item
* Text To Find - A [regular expression](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/regex/Pattern.html) to match
* New Text - The replacement. `$1`, `$2` … insert captured groups

Every lore line is matched, which makes it possible to update values without knowing what they currently are — the usual reason to reach for this over [Replace Lore](replace-lore.md).

### Examples

Bump any kill counter, whatever the current number:

```
/replaceloreregex Steve mainhand "Kills: \d+" "Kills: 7"
```

Keep the label and swap only the number, using a capture group:

```
/replaceloreregex Steve mainhand "(Level: )\d+" "$15"
```

Strip every colour code from the lore:

```
/replaceloreregex Steve mainhand "§." ""
```
