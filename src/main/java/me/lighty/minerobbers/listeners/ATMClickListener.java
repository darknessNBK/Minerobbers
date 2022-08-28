package me.lighty.minerobbers.listeners;

import com.github.unldenis.hologram.event.PlayerHologramInteractEvent;
import me.lighty.minerobbers.objects.ATM;
import me.lighty.minerobbers.utils.Methods;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ATMClickListener implements Listener {
    @EventHandler
    public void onATMClick(PlayerHologramInteractEvent e) {
        ATM atm = Methods.getAtmByHologram(e.getHologram());
        Bukkit.broadcastMessage("yea");
        if(atm == null) return;

        Player player = e.getPlayer();
        if(!atm.canRob()) {
            int secondsLeft = 60 - Methods.getUnixTime() - atm.getLastRobbery();
            player.sendMessage(Methods.chatColor("&cYou can't rob this ATM for another " + secondsLeft + " seconds."));
            return;
        } else {
            atm.robATM(player);
            player.sendMessage(Methods.chatColor("&aYou have robbed this ATM!"));
        }
    }
}
