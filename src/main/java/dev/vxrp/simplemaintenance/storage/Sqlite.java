package dev.vxrp.simplemaintenance.storage;

import java.sql.*;

public class Sqlite {
    private final Connection connection;
    public Sqlite(String path) throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:" + path);
        try (Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS Maintenance (" +
                    "maintenance BOOLEAN, excluded VARCHAR)");
        }
    }
    public void setMaintenance(boolean state) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Maintenance (maintenance) VALUES (?)")) {
            preparedStatement.setBoolean(1, state);
            preparedStatement.executeUpdate();
        }
    }
    public void updateMaintenance(boolean state) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement("UPDATE Maintenance SET maintenance = ?")) {
            preparedStatement.setBoolean(1, state);
            preparedStatement.executeUpdate();
        }
    }
    public boolean getMaintenance() throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT maintenance FROM Maintenance")) {
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.getBoolean("maintenance");
        }
    }
}
