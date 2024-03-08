package dev.vxrp.simplemaintenance;

import dev.vxrp.simplemaintenance.commands.MaintenanceCommand;
import dev.vxrp.simplemaintenance.events.OnJoinEvent;
import dev.vxrp.simplemaintenance.storage.Sqlite;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.util.Objects;

public final class SimpleMaintenance extends JavaPlugin {
    private Sqlite sqlite;
    @Override
    public void onEnable() {
        events();
        config();
        database();
        commands();
        try {
            sqlite.setMaintenance(false);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void commands() {
        Objects.requireNonNull(getCommand("simplemaintenance")).setExecutor(new MaintenanceCommand(this));
    }
    public void events() {
        getServer().getPluginManager().registerEvents(new OnJoinEvent(this), this);
    }
    public void database() {
        try {
            sqlite = new Sqlite(getDataFolder().getAbsolutePath() + "/data.db");
        } catch (SQLException e) {
            System.out.println("Connection to database failed!" + e.getMessage());
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }
    public void config() {
        saveDefaultConfig();
    }
    public Sqlite getSqlite() {
        return sqlite;
    }
}