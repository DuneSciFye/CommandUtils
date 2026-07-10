package me.dunescifye.commandutils.commands;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import dev.jorel.commandapi.arguments.FloatArgument;
import dev.jorel.commandapi.arguments.LiteralArgument;
import me.dunescifye.commandutils.CommandUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.UUID;

import static me.dunescifye.commandutils.utils.ArgumentUtils.*;

@CommandInfo(requiredPlugins = {"ProtocolLib"})
public class SetVisualHeartsCommand extends Command implements Listener {

    private static final HashMap<UUID, Float> spoofedHealth = new HashMap<>();

    @SuppressWarnings({"ConstantConditions", "null"})
    @Override
    public void register() {
        ProtocolLibrary.getProtocolManager().addPacketListener(
            new PacketAdapter(CommandUtils.getInstance(), ListenerPriority.HIGH, PacketType.Play.Server.UPDATE_HEALTH) {
                @Override
                public void onPacketSending(PacketEvent event) {
                    Float health = spoofedHealth.get(event.getPlayer().getUniqueId());
                    if (health != null) {
                        event.getPacket().getFloat().write(0, health);
                    }
                }
            }
        );

        // A visual health of 0 (or below) makes the client render the death screen, which locks
        // out movement and blocks chat/commands, so the player has no way to undo it themselves.
        // Over 20 has no effect
        FloatArgument heartsArg = new FloatArgument("Hearts", 1, 20);
        LiteralArgument resetArg = new LiteralArgument("reset");

        createCommand()
            .withArguments(playerArg(), heartsArg)
            .executes((sender, args) -> {
                Player player = args.getUnchecked(PLAYER_NAME);
                float hearts = args.getByArgument(heartsArg);
                setVisualHealth(player, hearts);
            })
            .register(this.getNamespace());

        createCommand()
            .withArguments(playerArg(), resetArg)
            .executes((sender, args) -> {
                Player player = args.getUnchecked(PLAYER_NAME);
                resetVisualHealth(player);
            })
            .register(this.getNamespace());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        spoofedHealth.remove(event.getPlayer().getUniqueId());
    }

    private static void setVisualHealth(Player player, float health) {
        spoofedHealth.put(player.getUniqueId(), health);
        sendHealth(player, health);
    }

    private static void resetVisualHealth(Player player) {
        spoofedHealth.remove(player.getUniqueId());
        sendHealth(player, (float) player.getHealth());
    }

    private static void sendHealth(Player player, float health) {
        ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
        PacketContainer packet = protocolManager.createPacket(PacketType.Play.Server.UPDATE_HEALTH);
        packet.getFloat().write(0, health);
        packet.getIntegers().write(0, player.getFoodLevel());
        packet.getFloat().write(1, (float) player.getSaturation());

        try {
            protocolManager.sendServerPacket(player, packet);
        } catch (Exception ignored) {
        }
    }
}
