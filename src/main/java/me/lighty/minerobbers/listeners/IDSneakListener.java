package me.lighty.minerobbers.listeners;

import me.lighty.minerobbers.objects.ATM;
import me.lighty.minerobbers.objects.Store;
import me.lighty.minerobbers.utils.Methods;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;

public class IDSneakListener implements Listener {

    @EventHandler
    public void onSneakToggle(PlayerToggleSneakEvent e) {
        Player player = e.getPlayer();
        if(!player.hasPermission("minerobbers.admin")) return;
        if(!e.isSneaking()) return;

        Store store = Methods.getNearestStore(player.getLocation());
        if(store != null) {
            player.sendActionBar(Methods.chatColor("&aStore ID: &e" + store.getStoreID()));
        } else {
            ATM atm = Methods.getNearestATM(player.getLocation());
            if(atm != null) {
                player.sendActionBar(Methods.chatColor("&aATM ID: &e" + atm.getID()));
            }
        }

    }

}
