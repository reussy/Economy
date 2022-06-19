package com.reussy.exodus.economy.plugin.utils;

import com.reussy.exodus.economy.plugin.EconomyPlugin;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class Configuration {

    private final EconomyPlugin plugin;
    private final File config;
    private final YamlConfiguration yamlConfiguration;

    public Configuration(EconomyPlugin plugin){
        this.plugin = plugin;
        this.config = new File(plugin.getDataFolder(), "config.yml");
        this.yamlConfiguration = YamlConfiguration.loadConfiguration(config);
        create();
    }

    private void create(){
        if (!config.exists()) plugin.saveResource("config.yml", false);
    }

    public YamlConfiguration get() {
        return yamlConfiguration;
    }
}
