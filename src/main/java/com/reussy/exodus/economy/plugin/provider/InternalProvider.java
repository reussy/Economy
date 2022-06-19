package com.reussy.exodus.economy.plugin.provider;

import com.reussy.exodus.economy.plugin.EconomyPlugin;
import com.reussy.exodus.economy.plugin.cache.PlayerEconomyCache;
import com.reussy.exodus.economy.plugin.cache.PlayerEconomyProperties;
import com.reussy.exodus.economy.plugin.utils.Message;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class InternalProvider implements IEconomyProvider{

    private final EconomyPlugin plugin;
    private final Message message;
    public InternalProvider(EconomyPlugin plugin){
        this.plugin = plugin;
        this.message = new Message();
    }

    @Override
    public double get(Player player) {
        return plugin.getPlayerCache().get(player.getUniqueId()).getMoney();
    }

    @Override
    public void give(CommandSender player, Player target, double amount) {

        //Save the new money balance into player cache
        plugin.getPlayerCache().get(target.getUniqueId()).setMoney(plugin.getPlayerCache().get(target.getUniqueId()).getMoney() + amount);

        message.send(target, "&aYou have received &6" + amount + " coins &eof &bConsole");
        player.sendMessage(message.colorize("&b" + target.getName() + " &ahas received &6" + amount + " coins!"));

    }

    @Override
    public void pay(Player player, Player target, double amount) {

        if (amount > plugin.getPlayerCache().get(player.getUniqueId()).getMoney()){
            message.send(player, "&cYou don't have enough money for that.");
            return;
        }

        //Save the new money balance into target cache.
        plugin.getPlayerCache().get(target.getUniqueId()).setMoney(plugin.getPlayerCache().get(target.getUniqueId()).getMoney() + amount);

        //Save the new money balance into player cache.
        plugin.getPlayerCache().get(player.getUniqueId()).setMoney(plugin.getPlayerCache().get(player.getUniqueId()).getMoney() - amount);

        message.send(player, "&cYou have paid &6" + amount + " coins &cto &b" + target.getName() + " &eNew balance: &6" + this.get(player));
        message.send(target, "&aYou have received &6" + amount + " coins &eof &b" + player.getName());
    }

    @Override
    public void remove(CommandSender player, Player target, double amount) {

        if (plugin.getPlayerCache().get(target.getUniqueId()).getMoney() < amount){
            player.sendMessage(message.colorize("&cThe player don't have enough money."));
            return;
        }

        //Save the new money balance into player cache.
        plugin.getPlayerCache().get(target.getUniqueId()).setMoney(plugin.getPlayerCache().get(target.getUniqueId()).getMoney() - amount);

        message.send(target, "&6" + amount + " coins &chave been removed from the &bConsole.");
        player.sendMessage(message.colorize("&6" + amount + " coins &aremoved to &b" + target.getName()));
    }

    @Override
    public void set(CommandSender player, Player target, double amount) {

        //Save the new money balance into target cache.
        plugin.getPlayerCache().get(target.getUniqueId()).setMoney(amount);

        message.send(target, "&eYour new money balance is &6" + amount + " coins");
        player.sendMessage(message.colorize("&aThe new balance of &b" + target.getName() + " &aare &6" + amount + " coins"));
    }
}
