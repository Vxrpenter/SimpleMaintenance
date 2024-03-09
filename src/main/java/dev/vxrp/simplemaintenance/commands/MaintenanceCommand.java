package dev.vxrp.simplemaintenance.commands;

import dev.vxrp.simplemaintenance.SimpleMaintenance;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.Objects;

public class MaintenanceCommand implements CommandExecutor {
    private final SimpleMaintenance plugin;
    public MaintenanceCommand(SimpleMaintenance simpleMaintenance) {
        this.plugin = simpleMaintenance;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        var mm = MiniMessage.miniMessage();
        int time = plugin.getConfig().getInt("time");

        if (args.length == 0) {
            sender.sendMessage(mm.deserialize("<red>SM <gray>» <gray>Usage: [/sm activate <red>| <gray>deactivate <red>| <gray>info]"));
        } else {
            try {
                if (args[0].equalsIgnoreCase("activate") && !plugin.getSqlite().getMaintenance()) {
                    sender.sendMessage(mm.deserialize("<red>SM <gray>» <gray>Maintenance is now <green>activated<newline>"));
                    plugin.getSqlite().updateMaintenance(true);
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        if (sender != p && !p.hasPermission("sm.bypass")) {
                            p.showTitle(Title.title(mm.deserialize("<gray>Maintenance <green>activated"), mm.deserialize("<gray>You will be disconnected in " +
                                    "<red>"+time+" <red>seconds")));
                            if (plugin.getConfig().getBoolean("maintenance_information.active")) {
                                p.sendMessage(mm.deserialize("                          <gray>« <red>SM <gray>»<newline>"));
                                p.sendMessage(mm.deserialize("<red>SM <gray>* <gray>Additional information on current maintenance --> <red><underlined><hover:show_text:'<gray>Click for additional information'><click:open_url:"
                                        +plugin.getConfig().getString("maintenance_information.link")+">support</click>"));
                                p.sendMessage(mm.deserialize("<newline>                          <gray>« <red>SM <gray>»"));
                            }
                            BukkitScheduler scheduler = Bukkit.getScheduler();
                            scheduler.runTaskLater(plugin, () -> p.kick(mm.deserialize("<red>" + plugin.getConfig().getString("kick_message"))), time* 20L);
                        } else if (p.hasPermission("sm.bypass")) {
                            p.sendMessage(mm.deserialize("<red>SM <gray>» You won't be disconnected because you are permitted to<newline> be online while maintenance is running"));
                            p.showTitle(Title.title(mm.deserialize("<gray>Maintenance <green>activated"), mm.deserialize("<red>You won't be disconnected ")));
                        }
                    }
                }
                if (args[0].equalsIgnoreCase("deactivate") && plugin.getSqlite().getMaintenance()) {
                    sender.sendMessage(mm.deserialize("<red>SM <gray>» <gray>Maintenance is now <red>deactivated"));
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        p.showTitle(Title.title(mm.deserialize("<gray>Maintenance <red>deactivated"), mm.deserialize("<gray>All player are able to join again ")));
                    }
                    plugin.getSqlite().updateMaintenance(false);
                }
                if (args[0].equalsIgnoreCase("info")) {
                    sender.sendMessage(mm.deserialize("                         <gray>« <red>SM <gray>»<newline>"));
                    if (plugin.getSqlite().getMaintenance()) {
                        sender.sendMessage(mm.deserialize("<gray>» Status: <green><hover:show_text:'<gray>Click to <red>deactivate'><click:suggest_command:/sm deactivate>active</click>"));
                    } else if (!plugin.getSqlite().getMaintenance()){
                        sender.sendMessage(mm.deserialize("<gray>» Status: <red><hover:show_text:'<gray>Click to <green>activate'><click:suggest_command:/sm activate>not active</click>"));
                    }
                    sender.sendMessage(mm.deserialize("<gray>» Seconds till a player gets kicked: <red>" + plugin.getConfig().getInt("time")));
                    sender.sendMessage(mm.deserialize("<newline>                         <gray>« <red>SM <gray>»"));
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return false;
    }
}
