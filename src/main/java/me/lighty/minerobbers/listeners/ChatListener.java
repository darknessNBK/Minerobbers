package me.lighty.minerobbers.listeners;

import me.lighty.minerobbers.MinerobbersPlugin;
import me.lighty.minerobbers.handlers.ChatHandler;
import me.lighty.minerobbers.objects.Store;
import me.lighty.minerobbers.utils.Methods;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {

    @EventHandler
    public void chatEvent(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();
        if(MinerobbersPlugin.getChatHandlers().get(player) == null) return;

        Store store = MinerobbersPlugin.getChatHandlers().get(player).getStore();
        ChatHandler.ChatType type = MinerobbersPlugin.getChatHandlers().get(player).getType();

        if(!e.getPlayer().equals(player)) return;
        e.setCancelled(true);
        String message = e.getMessage();

        if(message.equalsIgnoreCase("cancel")) {
            MinerobbersPlugin.getChatHandlers().remove(player);
            player.sendMessage(Methods.chatColor("&cStore editing cancelled."));
            return;
        }

        if(type == ChatHandler.ChatType.NAME) {
            store.setStoreNAME(message);
        } else if(type == ChatHandler.ChatType.PRICE) {
            try {
                store.setStorePRICE(Integer.parseInt(message));
            } catch (NumberFormatException ex) {
                player.sendMessage(Methods.chatColor("&cPlease enter a valid number."));
                return;
            }
        } else if(type == ChatHandler.ChatType.OWNER) {
            Player newOwner = Bukkit.getPlayer(message);
            if(newOwner == null) {
                player.sendMessage(Methods.chatColor("&cPlayer not found."));
                return;
            }
            store.setStoreOWNER(newOwner.getUniqueId());
        } else if(type == ChatHandler.ChatType.MINIMUMROBPRICE) {
            try {
                store.setStoreMINIMUMROBPRICE(Integer.parseInt(message));
            } catch (NumberFormatException ex) {
                player.sendMessage(Methods.chatColor("&cPlease enter a valid number."));
                return;
            }
        } else if(type == ChatHandler.ChatType.MAXIMUMROBPRICE) {
            try {
                store.setStoreMAXIMUMROBPRICE(Integer.parseInt(message));
            } catch (NumberFormatException ex) {
                player.sendMessage(Methods.chatColor("&cPlease enter a valid number."));
                return;
            }
        }
        store.saveStore();
        player.sendMessage(Methods.chatColor("&aStore updated."));
        MinerobbersPlugin.getChatHandlers().remove(player);
    }
}
