package dev.vxrp.simplemaintenance.events;

import com.destroystokyo.paper.event.server.PaperServerListPingEvent;
import dev.vxrp.simplemaintenance.SimpleMaintenance;
import dev.vxrp.simplemaintenance.util.MOTDManager;
import net.kyori.adventure.text.Component;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.sql.SQLException;
import java.util.Objects;

public class ListPingEvent implements Listener {
    private final SimpleMaintenance plugin;
    public ListPingEvent(SimpleMaintenance simpleMaintenance) {
        this.plugin = simpleMaintenance;
    }
    @EventHandler
    public void onListPing(PaperServerListPingEvent e) throws SQLException {
        Component maintenanceMotd = MOTDManager.motd(Objects.requireNonNull(plugin.getConfig().getString("maintenance_motd")));
        Component basicMotd = MOTDManager.motd(Objects.requireNonNull(plugin.getConfig().getString("basic_motd")));
        if (plugin.getSqlite().getMaintenance()) {
            e.setHidePlayers(true);
            e.motd(maintenanceMotd);
        } else if (!plugin.getSqlite().getMaintenance()) {
            e.motd(basicMotd);
        }
    }
}
