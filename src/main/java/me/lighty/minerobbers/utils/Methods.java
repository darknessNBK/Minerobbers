package me.lighty.minerobbers.utils;

import me.lighty.minerobbers.MinerobbersPlugin;
import me.lighty.minerobbers.objects.Store;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.lang.String;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Methods {
    public static String chatColor(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static List<String> chatColorList(List<String> message) {
        List<String> newMessage = new ArrayList<>();
        for (String s : message) {
            newMessage.add(chatColor(s));
        }
        return newMessage;
    }

    public static String locationToString(Location location) {
        return location.getWorld().getName() + "," + location.getBlockX() + "," + location.getBlockY() + "," + location.getBlockZ();
    }

    public static Location stringToLocation(String location) {
        String[] split = location.split(",");
        return new Location(Bukkit.getWorld(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]), Integer.parseInt(split[3]));
    }

    public static boolean isInt(String string) {
        try {
            Integer.parseInt(string);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean doesStoreExist(int storeID) {
        for (Store store : MinerobbersPlugin.getStores()) {
            if (store.getStoreID() == storeID) {
                return true;
            }
        }
        return false;
    }

    public static Store getStore(Integer storeID) {
        for (Store store : MinerobbersPlugin.getStores()) {
            if (store.getStoreID() == storeID) {
                return store;
            }
        }
        return null;
    }

    public static int findPerfectID() {
        int id = 0;
        while (doesStoreExist(id)) {
            id++;
        }
        return id;
    }

    public static void loadAllStoresFromConfig() {
        int storeSize = MinerobbersPlugin.getStoresJson().getAll("stores").size();
        for (int i = 0; i < storeSize + 1; i++) {
           if(MinerobbersPlugin.getStoresJson().contains("stores." + i + ".owner")) {
               Location location = stringToLocation(MinerobbersPlugin.getStoresJson().getString("stores." + i + ".location"));
               String name = MinerobbersPlugin.getStoresJson().getString("stores." + i + ".name");

               String owner = MinerobbersPlugin.getStoresJson().getString("stores." + i + ".owner");
               String lastRobber = MinerobbersPlugin.getStoresJson().getString("stores." + i + ".lastRobber");

               int price = MinerobbersPlugin.getStoresJson().getInt("stores." + i + ".price");
               int minimumRobPrice = MinerobbersPlugin.getStoresJson().getInt("stores." + i + ".minimumRobPrice");
               int maximumRobPrice = MinerobbersPlugin.getStoresJson().getInt("stores." + i + ".maximumRobPrice");
               int lastRobbed = MinerobbersPlugin.getStoresJson().getInt("stores." + i + ".lastRobbed");

               Store store = new Store(i, owner, location, name, price, minimumRobPrice, maximumRobPrice, lastRobbed, lastRobber);
               store.saveStore();
           }
        }
    }

    public static String nullUUID() {
        return "00000000-0000-0000-0000-000000000000";
    }

    public static List<Store> getPaginatedStores(int page) {
        List<Store> stores = new ArrayList<>();
        int start = (page - 1) * 5;
        int end = page * 5;
        for (int i = start; i < end; i++) {
            if (MinerobbersPlugin.getStoresJson().contains("stores." + i + ".owner")) {
                stores.add(getStore(i));
            }
        }
        return stores;
    }

    public static TextComponent pageNextAndBackStore(int page) {
        TextComponent next = new TextComponent("[>] ");
        next.setBold(true);
        next.setColor(net.md_5.bungee.api.ChatColor.GREEN);
        next.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/minerobbers store list " + (page + 1)));
        TextComponent back = new TextComponent("[<] ");
        back.setBold(true);
        back.setColor(net.md_5.bungee.api.ChatColor.RED);
        back.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/minerobbers store list " + (page - 1)));
        TextComponent pageTxt = new TextComponent("Page " + page + " ");
        pageTxt.setBold(false);
        pageTxt.setColor(net.md_5.bungee.api.ChatColor.YELLOW);
        back.addExtra(pageTxt);
        back.addExtra(next);
        return back;
    }

    public static void clearPlayerChat(Player player) {
        for (int i = 0; i < 100; i++) {
            player.sendMessage("");
        }
    }

    public static void createHolograms() {
        for (Store store : MinerobbersPlugin.getStores()) {
            store.createHologram();
        }
    }
}