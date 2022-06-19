package com.reussy.exodus.economy.plugin.commands;

import com.reussy.exodus.economy.plugin.EconomyPlugin;
import com.reussy.exodus.economy.plugin.cache.PlayerEconomyProperties;
import com.reussy.exodus.economy.plugin.provider.IEconomyProvider;
import com.reussy.exodus.economy.plugin.provider.InternalProvider;
import com.reussy.exodus.economy.plugin.utils.Message;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class AdminCommand extends BukkitCommand {

    private final EconomyPlugin plugin;
    private final Message message;
    public AdminCommand(EconomyPlugin plugin, @NotNull String name) {
        super(name);
        this.plugin = plugin;
        this.message = new Message();
    }

    /**
     * Executes the command, returning its success
     *
     * @param sender       Source object which is executing this command
     * @param commandLabel The alias of the command used
     * @param args         All arguments passed to the command, split via ' '
     * @return true if the command was successful, otherwise false
     */
    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {

        if (args.length == 0){
            sender.sendMessage(message.colorize("&eHelp Menu for Commands"));
            sender.sendMessage(message.colorize("&e/money-admin give (player) (amount) &8- &7Give money to other player."));
            sender.sendMessage(message.colorize("&e/money-admin remove (player) (amount) &8- &7Remove money from the player."));
            sender.sendMessage(message.colorize("&e/money-admin set (player) &8- &7Edit the player balance."));
            return true;
        }

        double amount;
        Player target;

        switch (args[0]) {

            case "give" -> {

                if (args.length < 3){
                    sender.sendMessage(message.colorize("&cIncorrect usage: /money-admin give (player) (amount)"));
                    return true;
                }

                target = Bukkit.getPlayer(args[1]);
                if (target == null){
                    sender.sendMessage(message.colorize("&cThe player " + args[1] + " &cdoes not exist!"));
                    return true;
                }

                amount = Math.abs(Double.parseDouble(args[2]));

                plugin.getEconomyProvider().give(sender, target, amount);
            }
            case "remove" -> {

                if (args.length < 3) {
                    sender.sendMessage(message.colorize("&cIncorrect usage: /money-admin remove (player) (amount)"));
                    return true;
                }

                target = Bukkit.getPlayer(args[1]);
                if (target == null){
                    sender.sendMessage(message.colorize("&cThe player " + args[1] + " &cdoes not exist!"));
                    return true;
                }

                amount = Math.abs(Double.parseDouble(args[2]));

                plugin.getEconomyProvider().remove(sender, target, amount);
            }
            case "set" -> {

                if (args.length < 3){
                    sender.sendMessage(message.colorize("&cIncorrect usage: /money-admin set (player) (amount)"));
                    return true;
                }

                target = Bukkit.getPlayer(args[1]);
                if (target == null){
                    sender.sendMessage(message.colorize("&cThe player " + args[1] + " &cdoes not exist!"));
                    return true;
                }

                amount = Math.abs(Double.parseDouble(args[2]));

                plugin.getEconomyProvider().set(sender, target, amount);
            }

            default ->{
                sender.sendMessage(message.colorize("&eHelp Menu for Commands"));
                sender.sendMessage(message.colorize("&e/money-admin give (player) (amount) &8- &7Give money to other player."));
                sender.sendMessage(message.colorize("&e/money-admin remove (player) (amount) &8- &7Remove money from the player."));
                sender.sendMessage(message.colorize("&e/money-admin set (player) &8- &7Edit the player balance."));
            }
        }

        return true;
    }
}
