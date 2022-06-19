package com.reussy.exodus.economy.plugin.database;

import com.reussy.exodus.economy.plugin.cache.PlayerEconomyProperties;

import java.util.UUID;

public interface IDatabase {

    void createTables();

    boolean hasData(UUID uuid);

    PlayerEconomyProperties load(UUID uuid);

    void save(PlayerEconomyProperties playerEconomyProperties);
}
