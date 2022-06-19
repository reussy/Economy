package com.reussy.exodus.economy.plugin.cache;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerEconomyCache {

    private final Map<UUID, PlayerEconomyProperties> cache;

    public PlayerEconomyCache(){
        this.cache = new HashMap<>();
    }

    public void load(UUID uuid, PlayerEconomyProperties playerEconomyProperties){
        cache.put(uuid, playerEconomyProperties);
    }

    public void destroy(UUID uuid){
        cache.remove(uuid);
    }

    public PlayerEconomyProperties get(UUID uuid){

        return cache.get(uuid);
    }
}
