---
description: Makes the player glide without an elytra
---

# Set Gliding

Usage: /setgliding \[\<Is Gliding>]

* Is Gliding _(optional)_ - `true` starts gliding, `false` stops. Defaults to `true`

Runs as the player who executed it, so call it through `/execute as <player>` from the console or a command block.

Gliding normally cancels itself the moment the server notices there is no elytra. This command keeps it going until the player lands, which is what makes rocket, dash and hang-glider items possible without giving out an elytra.

### Examples

Start gliding:

```
/setgliding
```

Stop gliding mid-air:

```
/setgliding false
```

Launch a player upward and let them glide down:

```
/execute as Steve run modifyvelocity Steve 0 1.5 0
/execute as Steve run setgliding
```
