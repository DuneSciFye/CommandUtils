---
description: Launches a projectile from a player, with optional particle trails and commands
---

# Launch Projectile

Usage: /launchprojectile \<Projectile> \[\<Max Alive Time>] \[\<Particle> \<Period>]

Usage: /launchprojectile \<Projectile> \[\<Max Alive Time>] \[\<Delay> \<Period> \<Command Separator> \<Commands>]

Usage: /launchprojectile \<Projectile> \<Max Alive Time> \<Command Separator> \<Commands>

* Projectile - `ARROW`, `SNOWBALL`, `WIND_CHARGE`, `DRAGONFIREBALL`, `FIREWORK_ROCKET` or `WITHER_SKULL`
* Max Alive Time _(optional)_ - The projectile is removed after this long. Defaults to `30s`
* Particle - A particle spawned at the projectile every period, for a trail
* Delay - How long before the first command run
* Period - How long between particle spawns or command runs
* Command Separator - String that separates the commands
* Commands - Commands to run

Runs as the player who executed it, so call it through `/execute as <player>` from the console or a command block. The projectile is launched from their eyes in the direction they are looking, and they are credited as the shooter.

The third form runs its commands **once**, when the projectile lands or dies — that's the one for impact effects. The second form runs them repeatedly while it is in flight.

## Placeholders

Available inside `Commands`:

| Placeholder | Value |
| --- | --- |
| `{projectile_x}` `{projectile_y}` `{projectile_z}` | Current position |
| `{projectile_uuid}` | The projectile's UUID |

### Examples

A snowball with a flame trail:

```
/launchprojectile SNOWBALL 10s flame 1t
```

An arrow that explodes into lightning where it lands:

```
/launchprojectile ARROW 30s ,, spawnnodamagelightning world {projectile_x} {projectile_y} {projectile_z}
```

A wind charge that leaves a trail of particles behind it:

```
/launchprojectile WIND_CHARGE 5s 0t 1t ,, silentparticle cloud {projectile_x} {projectile_y} {projectile_z} 0 0 0 0 3
```
