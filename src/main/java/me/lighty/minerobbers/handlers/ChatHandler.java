package me.lighty.minerobbers.handlers;

import lombok.Getter;
import me.lighty.minerobbers.objects.Store;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class ChatHandler implements Listener {

    @Getter private Store store;
    @Getter private Player player;
    @Getter private ChatType type;

    public ChatHandler(Store store, Player player, ChatType type) {
        this.store = store;
        this.player = player;
        this.type = type;
    }

    public enum ChatType {
        NAME,
        PRICE,
        OWNER,
        MINIMUMROBPRICE,
        MAXIMUMROBPRICE
    }
}
