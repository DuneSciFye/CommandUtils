---
description: Draws a line of particles from an entity's eyes and runs commands along it
---

# Ray Trace Particle

Usage: /raytraceparticle \<Particle> \<Length> \<Spacing> \<Period> \<Entity> \[\<Commands>] \[\<Command Separator>] \[\<X Placeholder>] \[\<Y Placeholder>] \[\<Z Placeholder>]

* Particle - The particle to draw with
* Length - How many particles are drawn
* Spacing - Distance between them, in blocks. `Length × Spacing` is the reach
* Period - Ticks between each particle. `0` draws the whole line instantly
* Entity - The entity the line starts from, drawn from its eyes in the direction it faces
* Commands _(optional)_ - Commands run at every point along the line
* Command Separator _(optional)_ - Replaces `,,`
* X / Y / Z Placeholder _(optional)_ - Text replaced with the point's coordinates. Default `%particle_x%`, `%particle_y%`, `%particle_z%`

With a `Period` above zero the beam travels outward one point at a time, which reads as a projectile. With `0` the whole line appears at once, which reads as a laser.

The line goes straight through blocks and entities — nothing stops it. Combine it with commands to add the hit detection.

### Examples

An instant 20 block laser:

```
/raytraceparticle end_rod 40 0.5 0 Steve
```

A travelling beam that damages whatever it passes:

```
/raytraceparticle flame 30 0.5 1 Steve "damage @e[x=%particle_x%,y=%particle_y%,z=%particle_z%,distance=..1,limit=1] 2"
```

Custom placeholders, so the outer plugin leaves them alone:

```
/raytraceparticle soul_fire_flame 20 0.5 1 Steve "silentparticle explosion {x} {y} {z}" ,, {x} {y} {z}
```
