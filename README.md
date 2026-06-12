# CommandUtils

[![Latest Release](https://img.shields.io/github/v/release/DuneSciFye/CommandUtils?label=release)](https://github.com/DuneSciFye/CommandUtils/releases/latest)
[![SpigotMC](https://img.shields.io/badge/SpigotMC-resource-orange)](https://www.spigotmc.org/resources/commandutils.117733/)
[![Minecraft](https://img.shields.io/badge/Minecraft-1.20%2B-brightgreen)](https://papermc.io/)

**CommandUtils** is a Paper/Spigot plugin that adds 100+ highly configurable commands and a rich set of PlaceholderAPI placeholders. It's built for server owners and pack developers who want fine-grained control over blocks, items, entities, players, and command scripting — without writing a plugin of their own.

Commands are designed to be called from command blocks, datapacks, other plugins (such as ExecutableItems/ExecutableBlocks), and console, making CommandUtils a powerful glue layer for custom server mechanics.

## Features

- **100+ commands** spanning block manipulation, item editing, entity control, player attributes, projectiles, messaging, and command scripting.
- **PlaceholderAPI integration** with three expansions: `stringutils`, `blockutils`, and `playerutils`.
- **Deep configuration** — per-command toggles, whitelists/blacklists for block operations, custom argument separators, and more in `config.yml`.
- **Soft integrations** with GriefPrevention, WorldGuard, Factions, CoreProtect, ExecutableBlocks, LibsDisguises, and PlaceholderAPI — used automatically when present.
- **Auto-registered commands** with annotation-based metadata (version gating, required plugins, custom names/aliases).

## Installation

1. Download the latest `CommandUtils.jar` from the [GitHub Releases](https://github.com/DuneSciFye/CommandUtils/releases/latest) page or [SpigotMC](https://www.spigotmc.org/resources/commandutils.117733/).
2. Drop the jar into your server's `plugins/` folder.
3. Restart the server (or use a plugin manager such as Plugman to load it).
4. Edit the generated `plugins/CommandUtils/config.yml` to enable/disable commands and tweak settings, then reload.

**Requirements:** Paper (or a fork) running Minecraft **1.20+** and **Java 21**.

### Optional integrations

These plugins are detected automatically and unlock related functionality when installed:

| Plugin | Used for |
| --- | --- |
| PlaceholderAPI | Registering the `stringutils` / `blockutils` / `playerutils` placeholder expansions |
| GriefPrevention | Respecting land claims in block-break commands |
| WorldGuard | Respecting region protections in block operations |
| Factions (Saber) | Respecting faction territory |
| CoreProtect | Logging block changes for rollback (e.g. `/breakinfacinglogcoreprotect`) |
| ExecutableBlocks | Block-related custom mechanics |
| LibsDisguises | Disguise-related placeholders |

## Commands

Every command can be individually enabled or disabled in `config.yml`. Commands support tab-completion that shows the expected arguments in-game. Below is the full command set grouped by purpose.

### Block manipulation
`blockcycle`, `blockgravity`, `blockprison`, `bonemealblock`, `breakandreplant`, `breakblockmultiplydrops`, `breakinfacing`, `breakinfacinglogcoreprotect`, `breakinradius`, `breakinvein`, `breakinxyz`, `highlightblocks`, `placeblockfrominv`, `placeblockfromslot`, `removeinfacing`, `removeinradius`, `replaceinfacing`, `replaceinradius`, `replaceinradiusifblockrelative`, `replaceinxyz`, `replaceinxz`, `selectblocks`, `selectblocksfacing`, `spawnblockbreaker`, `waterlog`

### Items & inventory
`additemnbt`, `give`, `getplayerhead`, `itemattribute`, `itemcooldown`, `itemdamage`, `itemlore`, `itemname`, `loadcrossbow`, `lockheldslot`, `mixinventory`, `placeblockfrominv`, `placeblockfromslot`, `preventmixinventory`, `removecustomdataitem`, `removeitem`, `removenbtitem`, `replacelore`, `replaceloreregex`, `selectitems`, `setarmortrim`, `setcursoritem`, `setenchantment`, `setheldslot`, `setitem`, `setitemnbt`, `smeltitem`, `unsetitemnbt`

### Player attributes & state
`copyeffects`, `damage`, `disablejump`, `disablesprint`, `effect`, `flightspeed`, `food`, `health`, `lifesteal`, `modifyvelocity`, `mount`, `multiplyvelocity`, `overrideeffect`, `oxygen`, `preciseeffect`, `pushentity`, `saturation`, `setai`, `setarrowsinbody`, `setcompasstracking`, `setfireticks`, `setflight`, `setfreezeticks`, `setgliding`, `swappositions`

### Entities & mobs
`changevillagerprofession`, `curevillager`, `mobdrops`, `mobtarget`, `mobtargetteam`, `refreshvillagertrades`, `removeentity`, `setmobtarget`, `setvillagertrade`, `shearentity`, `silentsummon`, `zombifyvillager`

### Projectiles, particles & spawning
`launchfirework`, `launchprojectile`, `launchtnt`, `raytraceparticle`, `setprojectilecommands`, `settntsource`, `silentparticle`, `spawnguardianbeam`, `spawnnodamageevokerfang`, `spawnnodamagefirework`, `spawnnodamagelightning`, `spawnwitherskull`

### Messaging & UI
`broadcastmessage`, `sendactionbar`, `sendbossbar`, `sendconditionmessage`, `sendmessage`

### Command scripting & control flow
`chancerandomrun`, `cooldowncommand`, `if`, `loop`, `metadata`, `parseplaceholder`, `preciseif`, `runcommandfor`, `runcommandlater`, `runcommandwhen`, `tempplayervar`, `tempvar`, `trimcommand`, `weightedrandom`, `while`

> Tip: command names, aliases, and which commands are registered can be customized in `config.yml`. Some commands are gated by Minecraft version or require an optional plugin to be installed.

### Picture of commands in game
![Commands in game](https://github.com/user-attachments/assets/b45c83ab-943c-4642-a210-c6050692aa4a)

## Placeholders

When PlaceholderAPI is installed, CommandUtils registers three expansions:

| Identifier | Purpose |
| --- | --- |
| `%stringutils_...%` | String manipulation, conditionals (`if`/`elseif`/`else`), and temporary variables |
| `%blockutils_...%` | Querying blocks and the world |
| `%playerutils_...%` | Querying player data and state |

Each expansion can be toggled and tuned under the `Placeholders` section of `config.yml` (custom argument separators, condition keywords, etc.).

## Configuration

The plugin generates `plugins/CommandUtils/config.yml` on first run. Highlights:

- **Whitelists/Blacklists** — define reusable block sets (by tag, material, or exclusion) for block-break and replace commands. Supports tags (`#mineable/axe`), exclusions (`!BARREL`), and tag exclusions (`!#all_signs`).
- **Placeholders** — enable/disable each expansion and configure separators and keywords.
- **Commands** — enable/disable each command and override its name/aliases.

## Building from source

Requires JDK 21.

```bash
git clone https://github.com/DuneSciFye/CommandUtils.git
cd CommandUtils
./gradlew shadowJar
```

The shaded jar is produced at `build/libs/CommandUtils.jar`.

## Support & contributing

- **Suggestions / bugs:** open an issue on the [GitHub Issues](https://github.com/DuneSciFye/CommandUtils/issues) tab, or ask on our Discord.
- **Pull requests** are welcome.

## License

See the repository for license details.
