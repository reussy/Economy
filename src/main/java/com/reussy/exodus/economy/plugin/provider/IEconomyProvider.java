package com.reussy.exodus.economy.plugin.provider;

import com.reussy.exodus.economy.plugin.cache.PlayerEconomyProperties;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public interface IEconomyProvider {

    double get(Player player);

    void give(CommandSender player, Player target, double amount);

    void pay(Player player, Player target, double amount);

    void remove(CommandSender player, Player target, double amount);

    void set(CommandSender player, Player target, double amount);

}
