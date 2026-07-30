---
description: All of the commands provided by the plugin
icon: slash-forward
---

# Commands

{% hint style="info" %}
Arguments in \<arrows> are required; arguments in \[\<brackets>] are optional. When a page lists more than one **Usage** line, each line is a separate valid form of the command.
{% endhint %}

Every command can be enabled, disabled or aliased in [config.yml](../config.md), and every one of them is built to be run from command blocks, datapacks, the console, and plugins such as ExecutableItems and ExecutableBlocks.

## Things worth knowing

* Arguments that appear everywhere — worlds, locations, slots, block whitelists, durations — are documented once under [Arguments](../arguments/).
* Commands that take a **Player** run their protection checks against that player, so claims and regions are respected.
* Several commands run **as the sender** rather than taking a player argument. Call those with `/execute as <player> run ...`.
* State kept in memory — loops, cooldowns, variables, multipliers — does not survive a restart. Pages say so where it matters.

## Breaking blocks

| Command | Shape |
| --- | --- |
| [Break In Radius](break-in-radius.md) | A cube around a point |
| [Break In Facing](break-in-facing.md) | A wall in front of the player |
| [Break In XYZ](break-in-xyz.md) | A cuboid oriented to the player's view |
| [Break In Vein](break-in-vein.md) | A connected vein of the same block |
| [Break And Replant](break-and-replant.md) | Crops, harvested and replanted |
| [Break Block Multiply Drops](break-block-multiply-drops.md) | One block, multiplied loot |
| [Break In Facing Log CoreProtect](break-in-facing-log-coreprotect.md) | Break In Facing, logged to CoreProtect |
| [Remove In Radius](remove-in-radius.md) / [Remove In Facing](remove-in-facing.md) | The same shapes, with no drops |
| [Spawn Block Breaker](spawn-block-breaker.md) | A projectile that tunnels as it flies |

## Changing blocks

| Command | Purpose |
| --- | --- |
| [Replace In Radius](replace-in-radius.md) / [Replace In Facing](replace-in-facing.md) / [Replace In XYZ](replace-in-xyz.md) / [Replace In XZ](replace-in-xz.md) | Swap blocks in an area |
| [Replace In Radius If Block Relative](replace-in-radius-if-block-relative.md) | Swap only where the neighbours match |
| [Set Temp Block](set-temp-block.md) | Place a block that reverts itself |
| [Block Prison](block-prison.md) | Cage a player in temporary blocks |
| [Block Cycle](block-cycle.md) | Oxidise or wax copper |
| [Block Gravity](block-gravity.md) | Stop blocks falling |
| [Waterlog](waterlog.md) | Add or remove water inside blocks |
| [Bone Meal Block](bone-meal-block.md) | Bone meal an area |
| [Place Block From Inv](place-block-from-inv.md) / [Place Block From Slot](place-block-from-slot.md) | Place a block a player pays for |
| [Highlight Blocks](highlight-blocks.md) | Mark matching blocks with particles |

## Custom tools

| Command | Purpose |
| --- | --- |
| [Select Blocks](select-blocks.md) | Run a list of functions on each block in a cube |
| [Select Blocks Facing](select-blocks-facing.md) | The same, on the wall in front of the player |
| [Select Items](select-items.md) | Run functions on dropped items |
| [Smelt Item](smelt-item.md) | Cook dropped items where they lie |

## Control flow

| Command | Purpose |
| --- | --- |
| [If](if.md) / [Precise If](precise-if.md) | Run commands when a condition holds |
| [While](while.md) | Repeat for as long as a condition holds |
| [Loop](loop.md) | Repeat a set number of times |
| [Run Command Later](run-command-later.md) | Run after a delay |
| [Run Command When](run-command-when.md) | Wait for a condition, then run once |
| [Run Command For](run-command-for.md) | Run once per player |
| [Cooldown Command](cooldown-command.md) | Run only if a cooldown has expired |
| [Weighted Random](weighted-random.md) / [Chance Random Run](chance-random-run.md) | Run by chance |
| [Temp Var](temp-var.md) / [Temp Player Var](temp-player-var.md) | Store values between commands |
| [Parse Placeholder](parse-placeholder.md) / [Trim Command](trim-command.md) | Placeholder and command helpers |

## Players

| Command | Purpose |
| --- | --- |
| [Health](health.md) / [Food](food.md) / [Saturation](saturation.md) / [Oxygen](oxygen.md) | Vitals |
| [Set Visual Hearts](set-visual-hearts.md) | Show a fake health bar |
| [Set Flight](set-flight.md) / [Flight Speed](flight-speed.md) / [Set Gliding](set-gliding.md) | Movement in the air |
| [Stun](stun.md) / [Disable Jump](disable-jump.md) / [Disable Sprint](disable-sprint.md) / [Disable Shield](disable-shield.md) | Restrict a player |
| [Set Water Walk](set-water-walk.md) / [Set Water Float](set-water-float.md) | Movement in water |
| [Set Held Slot](set-held-slot.md) / [Lock Held Slot](lock-held-slot.md) | Control the hotbar |
| [Mix Inventory](mix-inventory.md) / [Prevent Mix Inventory](prevent-mix-inventory.md) | Scramble an inventory, or ward it |
| [Send Message](send-message.md) / [Broadcast Message](broadcast-message.md) / [Send Action Bar](send-action-bar.md) / [Send Boss Bar](send-boss-bar.md) | Talk to players |

## Entities

| Command | Purpose |
| --- | --- |
| [Set AI](set-ai.md) / [Set Mob Target](set-mob-target.md) / [Mob Target](mob-target.md) / [Mob Target Team](mob-target-team.md) | Mob behaviour |
| [Set Fire Ticks](set-fire-ticks.md) / [Set Freeze Ticks](set-freeze-ticks.md) / [Set Arrows In Body](set-arrows-in-body.md) | Entity state |
| [Push Entity](push-entity.md) / [Modify Velocity](modify-velocity.md) / [Multiply Velocity](multiply-velocity.md) | Movement |
| [Mount](mount.md) / [Swap Positions](swap-positions.md) / [Leash](leash.md) / [Shear Entity](shear-entity.md) / [Remove Entity](remove-entity.md) | Handling entities |
| [Mob Drops](mob-drops.md) / [Mob Drop Multiplier](mob-drop-multiplier.md) / [Xp Drop Multiplier](xp-drop-multiplier.md) | Loot |
| [Meta Data](meta-data.md) | Tag an entity |
| [Life Steal](life-steal.md) | Damage and heal in one step |

## Villagers

[Change Villager Profession](change-villager-profession.md) · [Set Villager Trade](set-villager-trade.md) · [Refresh Villager Trades](refresh-villager-trades.md) · [Cure Villager](cure-villager.md) · [Zombify Villager](zombify-villager.md)

## Items

| Command | Purpose |
| --- | --- |
| [Give](give.md) / [Remove Item](remove-item.md) / [Set Cursor Item](set-cursor-item.md) | Move items around |
| [Item Name](item-name.md) / [Item Lore](item-lore.md) / [Replace Lore](replace-lore.md) / [Replace Lore Regex](replace-lore-regex.md) | Text on items |
| [Set Item](set-item.md) / [Item Attribute](item-attribute.md) / [Set Enchantment](set-enchantment.md) / [Set Armor Trim](set-armor-trim.md) | Item properties |
| [Item Damage](item-damage.md) / [Item Cooldown](item-cooldown.md) / [Load Crossbow](load-crossbow.md) | Item state |
| [Set Item NBT](set-item-nbt.md) / [Add Item NBT](add-item-nbt.md) / [Unset Item NBT](unset-item-nbt.md) | Custom data |
| [Remove NBT Item](remove-nbt-item.md) / [Remove Custom Data Item](remove-custom-data-item.md) | Take items by their data |
| [Get Player Head](get-player-head.md) / [Set Compass Tracking](set-compass-tracking.md) | Special items |

## Effects and visuals

| Command | Purpose |
| --- | --- |
| [Effect](effect.md) / [Precise Effect](precise-effect.md) / [Override Effect](override-effect.md) / [Copy Effects](copy-effects.md) | Potion effects |
| [Silent Particle](silent-particle.md) / [Ray Trace Particle](ray-trace-particle.md) / [Spawn Guardian Beam](spawn-guardian-beam.md) | Particles and beams |
| [Launch Firework](launch-firework.md) / [Spawn No Damage Firework](spawn-no-damage-firework.md) | Fireworks |
| [Spawn No Damage Lightning](spawn-no-damage-lightning.md) / [Spawn No Damage Evoker Fang](spawn-no-damage-evoker-fang.md) | Harmless attack visuals |

## Projectiles and explosives

[Launch Projectile](launch-projectile.md) · [Set Projectile Commands](set-projectile-commands.md) · [Launch TNT](launch-tnt.md) · [Set TNT Source](set-tnt-source.md) · [Spawn Wither Skull](spawn-wither-skull.md) · [Silent Summon](silent-summon.md)
