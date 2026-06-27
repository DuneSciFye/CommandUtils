---
description: Removes matching items from a player's inventory
---

# Remove Item

Usage: /removeitem \<Player> \<Item> \[\<Max Amount>] \[\<Strict>] \[\<Check Chest>] \[\<Commands>] \[\<No Commands If Zero>] \[\<Command Separator>]

Usage: /removeitem \<Material List> \[\<Min Amount>] \[\<Max Amount>] \[\<Vanilla>] \[\<Check Chest>] \[\<Command Separator>] \[\<Commands>]

* Player - The player to affect
* Item - The item to use
* Max Amount _(optional)_ - Maximum amount to remove
* Strict _(optional)_ - Whether item matching must be exact (NBT included)
* Check Chest _(optional)_ - Whether to also check the chest the player is viewing
* Commands _(optional)_ - Commands to run, separated by the command separator
* No Commands If Zero _(optional)_ - Whether to skip running commands when nothing was removed
* Command Separator _(optional)_ - The string used to separate individual commands
* Material List - List of materials
* Min Amount _(optional)_ - Minimum amount required
* Vanilla _(optional)_ - Whether to match vanilla items only
