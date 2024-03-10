package dev.vxrp.simplemaintenance.events;

import dev.vxrp.simplemaintenance.SimpleMaintenance;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
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
            e.joinMessage(null);
            Bukkit.getScheduler().runTaskLater(plugin, () ->  player.kick(mm.deserialize("<red>" + plugin.getConfig().getString("kick_message"))), 1);
        }
    }
    @EventHandler
    public void onQuit(PlayerQuitEvent e) throws SQLException {
        if (plugin.getSqlite().getMaintenance()) {
            if (e.getPlayer().hasPermission("sm.bypass")) return;
            e.quitMessage(null);
        }
    }
}
