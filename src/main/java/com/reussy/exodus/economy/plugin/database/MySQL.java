package com.reussy.exodus.economy.plugin.database;

import com.reussy.exodus.economy.plugin.EconomyPlugin;
import com.reussy.exodus.economy.plugin.cache.PlayerEconomyProperties;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.*;
import java.util.UUID;

public class MySQL implements IDatabase{

    private EconomyPlugin plugin;
    private HikariConfig hikariConfig = new HikariConfig();
    private HikariDataSource hikariDataSource;
    private String host;
    private int port;
    private String username;
    private String password;
    private String database;
    private boolean ssl;

    public MySQL(EconomyPlugin plugin){
        this.plugin = plugin;
        this.host = plugin.getConfiguration().get().getString("database.host");
        this.port = plugin.getConfiguration().get().getInt("database.port");
        this.username = plugin.getConfiguration().get().getString("database.username");
        this.password = plugin.getConfiguration().get().getString("database.password");
        this.database = plugin.getConfiguration().get().getString("database.database");
        this.ssl = plugin.getConfiguration().get().getBoolean("database.use-ssl");
        connect();
    }

    private void connect(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException ignored) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
            } catch (ClassNotFoundException ignored1) {}
        }

        hikariConfig.setJdbcUrl("jdbc:mysql://" + host + ":" + port + "/" + database + "?useSSL=" + ssl);
        hikariConfig.setPoolName("Economy-MySQLPool");
        hikariConfig.setUsername(username);
        hikariConfig.setPassword(password);
        hikariDataSource = new HikariDataSource(hikariConfig);
    }

    @Override
    public void createTables() {

        String sql = "CREATE TABLE IF NOT EXISTS `server_economy `" + "(`uuid` VARCHAR(80) NOT NULL, `money` DOUBLE NOT NULL);";

        try (Connection connection = hikariDataSource.getConnection()){
            try(Statement statement = connection.prepareStatement(sql)){
                statement.executeUpdate(sql);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean hasData(UUID uuid) {

        String sql = "SELECT * FROM `server_economy` WHERE uuid=?;";

        try(Connection connection = hikariDataSource.getConnection()){
            try(PreparedStatement statement = connection.prepareStatement(sql)){
                statement.setString(1, uuid.toString());
                try(ResultSet resultSet = statement.executeQuery()){
                    return resultSet.next();
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public PlayerEconomyProperties load(UUID uuid) {

        PlayerEconomyProperties playerEconomyProperties = new PlayerEconomyProperties(uuid);

        if (!hasData(uuid)) {
            playerEconomyProperties.setMoney(0);
            return playerEconomyProperties;
        }

        String sql = "SELECT money FROM `server_economy` WHERE uuid=?;";
        try (Connection connection = hikariDataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, uuid.toString());
                try (ResultSet result = statement.executeQuery()) {
                    if (result.next()) {
                        playerEconomyProperties.setMoney(result.getLong("money"));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return playerEconomyProperties;
    }

    @Override
    public void save(PlayerEconomyProperties playerEconomyProperties) {

        String sql;
        try (Connection connection = hikariDataSource.getConnection()) {
            if (hasData(playerEconomyProperties.getUUID())) {
                sql = "UPDATE `server_economy` SET money=? WHERE uuid=?;";
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setDouble(1, playerEconomyProperties.getMoney());
                    statement.setString(2, playerEconomyProperties.getUUID().toString());
                    statement.executeUpdate();
                }
            } else {
                sql = "INSERT INTO `server_economy` (uuid, money) VALUES (?, ?);";
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setString(1, playerEconomyProperties.getUUID().toString());
                    statement.setDouble(2, playerEconomyProperties.getMoney());
                    statement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
