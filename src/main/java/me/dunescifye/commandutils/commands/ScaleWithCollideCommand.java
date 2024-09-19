package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.FloatArgument;
import dev.jorel.commandapi.arguments.PlayerArgument;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

public class ScaleWithCollideCommand extends Command implements Registerable {
    @SuppressWarnings("ConstantConditions")
    @Override
    public void register() {

        if (!this.getEnabled()) return;

        PlayerArgument playerArg = new PlayerArgument("Player");
        FloatArgument scaleArg = new FloatArgument("Scale");

        new CommandAPICommand("scalewithcollide")
            .withArguments(playerArg)
            .withArguments(scaleArg)
            .executes((sender, args) -> {
                Player p = args.getByArgument(playerArg);
                float scale = args.getByArgument(scaleArg);

                scaleWillCollide(scale, p);
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());
    }

    private final Vector PLAYER_DEFAULT_BOUNDING_BOX = new Vector(0.6, 1.8, 0.6);

    private boolean scaleWillCollide(float scale, Player player) {
//        double centerX = boxToScale.getCenterX();
//        double centerZ = boxToScale.getCenterZ();
//
//        double halfWidthX = (PLAYER_DEFAULT_BOUNDING_BOX.getX() * baseScale) / 2;
//        double halfWidthZ = (PLAYER_DEFAULT_BOUNDING_BOX.getZ() * baseScale) / 2;
//
//        double newHalfWidthX = halfWidthX * scale;
//        double newHalfWidthZ = halfWidthZ * scale;
//
//        double minY = boxToScale.getMinY();
//
//        double newHeight = (PLAYER_DEFAULT_BOUNDING_BOX.getY() * baseScale) * scale;
//        double maxY2 = minY + newHeight;
//
//        double minX2 = centerX - newHalfWidthX;
//        double minZ2 = centerZ - newHalfWidthZ;
//        double maxX2 = centerX + newHalfWidthX;
//        double maxZ2 = centerZ + newHalfWidthZ;
//        left this in to make the calculations easier to read
        double baseScale = player.getAttribute(Attribute.GENERIC_SCALE).getBaseValue(); // set this to 1 if you're scaling with base value
        BoundingBox boxToScale = player.getBoundingBox().clone();
        boxToScale.resize(
            boxToScale.getCenterX() - ((PLAYER_DEFAULT_BOUNDING_BOX.getX() * baseScale) * scale) / 2,
            boxToScale.getMinY(),
            boxToScale.getCenterZ() - ((PLAYER_DEFAULT_BOUNDING_BOX.getZ() * baseScale) * scale) / 2,
            boxToScale.getCenterX() + ((PLAYER_DEFAULT_BOUNDING_BOX.getX() * baseScale) * scale) / 2,
            boxToScale.getMinY() + ((PLAYER_DEFAULT_BOUNDING_BOX.getY() * baseScale) * scale),
            boxToScale.getCenterZ() + ((PLAYER_DEFAULT_BOUNDING_BOX.getZ() * baseScale) * scale) / 2
        );
        return player.getWorld().hasCollisionsIn(boxToScale);
    }
}
