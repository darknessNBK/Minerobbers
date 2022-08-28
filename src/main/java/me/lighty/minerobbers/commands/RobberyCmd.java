package me.lighty.minerobbers.commands;

import me.lighty.minerobbers.objects.ATM;
import me.lighty.minerobbers.objects.Store;
import me.lighty.minerobbers.utils.Methods;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class RobberyCmd implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage("You must be a player to use this command.");
            return true;
        }
        Player player = (Player) sender;
        if(!player.hasPermission("minerobbers.robbery")) {
            player.sendMessage("You do not have permission to use this command.");
            return true;
        }

        Store nearStore = Methods.getNearestStore(player.getLocation());
        if(nearStore != null) {
            nearStore.rob(player);
            return true;
        } else {
            ATM atm = Methods.getNearestATM(player.getLocation());
            if(atm != null) {
                atm.robATM(player);
                return true;
            } else {
                player.sendMessage(Methods.chatColor("&cThere are no stores or ATMs nearby."));
                return true;
            }
        }
    }
}
