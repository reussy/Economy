package com.reussy.exodus.economy.plugin;

import com.reussy.exodus.economy.plugin.cache.PlayerCacheLoader;
import com.reussy.exodus.economy.plugin.cache.PlayerEconomyCache;
import com.reussy.exodus.economy.plugin.cache.PlayerEconomyProperties;
import com.reussy.exodus.economy.plugin.commands.AdminCommand;
import com.reussy.exodus.economy.plugin.commands.UserCommand;
import com.reussy.exodus.economy.plugin.database.IDatabase;
import com.reussy.exodus.economy.plugin.database.MySQL;
import com.reussy.exodus.economy.plugin.provider.IEconomyProvider;
import com.reussy.exodus.economy.plugin.provider.InternalProvider;
import com.reussy.exodus.economy.plugin.utils.Configuration;
import com.reussy.exodus.economy.plugin.utils.Message;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.UUID;

public final class EconomyPlugin extends JavaPlugin {

    private Configuration configuration;
    private IDatabase database;
    private IEconomyProvider economyProvider;
    private PlayerEconomyCache playerEconomyCache;

    @Override
    public void onEnable() {

        this.configuration = new Configuration(this);
        this.database = new MySQL(this);
        this.playerEconomyCache = new PlayerEconomyCache();
        this.economyProvider = new InternalProvider(this);
        Bukkit.getPluginManager().registerEvents(new PlayerCacheLoader(this), this);
        commands();

        //Useful for plugman or /reload
        for (Player player : Bukkit.getOnlinePlayers()){
            UUID uuid = player.getUniqueId();
            PlayerEconomyProperties playerEconomyProperties = this.getDatabase().load(uuid);
            this.getPlayerCache().load(uuid, playerEconomyProperties);
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void commands(){

        try {
            Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            bukkitCommandMap.setAccessible(true);
            CommandMap commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());
            commandMap.register("money", new UserCommand(this, "money"));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        try {
            Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            bukkitCommandMap.setAccessible(true);
            CommandMap commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());
            commandMap.register("money-admin", new AdminCommand(this, "money-admin"));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public IDatabase getDatabase() {
        return database;
    }

    public IEconomyProvider getEconomyProvider() {
        return economyProvider;
    }

    public PlayerEconomyCache getPlayerCache() {
        return playerEconomyCache;
    }

}
