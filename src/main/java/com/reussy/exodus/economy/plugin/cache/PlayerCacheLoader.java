package com.reussy.exodus.economy.plugin.cache;

import com.reussy.exodus.economy.plugin.EconomyPlugin;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class PlayerCacheLoader implements Listener {

    private final EconomyPlugin plugin;
    public PlayerCacheLoader(EconomyPlugin plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event){

        UUID uuid = event.getPlayer().getUniqueId();
        PlayerEconomyProperties playerEconomyProperties = plugin.getDatabase().load(uuid);
        plugin.getPlayerCache().load(uuid, playerEconomyProperties);

    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event){

        PlayerEconomyProperties playerEconomyProperties = plugin.getPlayerCache().get(event.getPlayer().getUniqueId());
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> plugin.getDatabase().save(playerEconomyProperties));
        plugin.getPlayerCache().destroy(event.getPlayer().getUniqueId());

    }
}
