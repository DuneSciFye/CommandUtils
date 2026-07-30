---
description: Shows a player a fake health bar
---

# Set Visual Hearts

Usage: /setvisualhearts \<Player> \<Hearts>

Usage: /setvisualhearts \<Player> \<reset>

* Player - The player who sees the fake bar
* Hearts - Health to display, `1` to `20`. `20` is a full bar
* reset - Goes back to showing their real health

Only the display changes. Real health, damage and death are untouched, and nobody else sees anything different.

The spoof stays until it is reset or the player logs off.

{% hint style="warning" %}
Requires **ProtocolLib**. Without it the command is not registered.
{% endhint %}

### Examples

Make a player look nearly dead:

```
/setvisualhearts Steve 1
```

Hide chip damage during a boss fight by pinning the bar full:

```
/setvisualhearts Steve 20
```

Show their real health again:

```
/setvisualhearts Steve reset
```
