package com.reussy.exodus.economy.plugin.cache;

import java.util.UUID;

public class PlayerEconomyProperties {

    private final UUID uuid;
    private double money;

    public PlayerEconomyProperties(UUID uuid){
        this.uuid = uuid;
    }

    public UUID getUUID() {
        return uuid;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }
}
