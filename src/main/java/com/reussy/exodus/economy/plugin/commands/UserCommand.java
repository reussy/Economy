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

public class UserCommand extends BukkitCommand {

    private final EconomyPlugin plugin;
    private final Message message;

    public UserCommand(EconomyPlugin plugin, @NotNull String name) {
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

        if (!(sender instanceof Player player)) {
            sender.sendMessage(message.colorize("&cOnly players!"));
            return true;
        }

        if (args.length == 0) {
            message.send(player, "&eYour money in the balance is &6" + plugin.getEconomyProvider().get(player));
            return true;
        }

        if (args[0].equalsIgnoreCase("pay")) {

            if (args.length < 3){
                message.send(player, "&cIncorrect usage: /money pay (player) (amount)");
                return true;
            }

            Player target = Bukkit.getPlayer(args[1]);
            double amount = Double.parseDouble(args[2]);

            if (target == null){
                message.send(player, "&cThe player " + args[1] + " &cdoes not exist!");
                return true;
            }

            plugin.getEconomyProvider().pay(player, target, amount);
            return true;
        }

        player.sendMessage(message.colorize("&eHelp Menu for Commands"));
        player.sendMessage(message.colorize("&e/money &8- &7View your money balance."));
        player.sendMessage(message.colorize("&e/money pay (player) (amount) &8- &7Pay to other player."));
        return false;
    }
}
