package dev.vxrp.simplemaintenance.events;

import dev.vxrp.simplemaintenance.SimpleMaintenance;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.SQLException;
public class OnJoinEvent implements Listener {
    private final SimpleMaintenance plugin;
    public OnJoinEvent(SimpleMaintenance simpleMaintenance) {
        this.plugin = simpleMaintenance;
    }
    @EventHandler
    public void onJoin(PlayerJoinEvent e) throws SQLException {
        Player player = e.getPlayer();
        var mm = MiniMessage.miniMessage();
        if (plugin.getSqlite().getMaintenance()) {
            if (player.hasPermission("sm.bypass")) return;
            player.kick(mm.deserialize("<red>" + plugin.getConfig().getString("kick_message")));
        }
    }
}
