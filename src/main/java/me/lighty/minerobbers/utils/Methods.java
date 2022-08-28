package me.lighty.minerobbers.utils;

import com.github.unldenis.hologram.Hologram;
import me.lighty.minerobbers.MinerobbersPlugin;
import me.lighty.minerobbers.objects.ATM;
import me.lighty.minerobbers.objects.Store;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.lang.String;
import java.lang.constant.Constable;
import java.text.SimpleDateFormat;
import java.util.*;

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

    public static boolean doesATMExist(int atmID) {
        for (ATM atm : MinerobbersPlugin.getAtms()) {
            if (atm.getID() == atmID) {
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

    public static ATM getATM(Integer atmID) {
        for (ATM atm : MinerobbersPlugin.getAtms()) {
            if (atm.getID() == atmID) {
                return atm;
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

    public static int findPerfectATMID() {
        int id = 0;
        while (doesATMExist(id)) {
            id++;
        }
        return id;
    }

    public static void loadAllStoresFromConfig() {
        int storeSize = 100;

        for (int i = 0; i < storeSize + 1; i++) {
           if(MinerobbersPlugin.getStoresJson().get("stores." + i) != null) {
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

    public static void loadAllATMsFromConfig() {
        int atmSize = 100;
        for (int i = 0; i < atmSize + 1; i++) {
            if(MinerobbersPlugin.getAtmsJson().get("atms." + i) != null) {
                int atmID = MinerobbersPlugin.getAtmsJson().getInt("atms." + i + ".id");
                int atmLastRobbed = MinerobbersPlugin.getAtmsJson().getInt("atms." + i + ".lastRobbed");
                UUID atmLastRobber = UUID.fromString(MinerobbersPlugin.getAtmsJson().getString("atms." + i + ".lastRobber"));
                Location atmLocation = stringToLocation(MinerobbersPlugin.getAtmsJson().getString("atms." + i + ".location"));

                ATM atm = new ATM(atmID, atmLocation, atmLastRobber, atmLastRobbed);
                atm.saveATM();
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

    public static int getUnixTime() {
        return (int) (System.currentTimeMillis() / 1000L);
    }

    public static double secondsToMinutes(int seconds) {
        return seconds / 60.0;
    }

    public static String secondsOrMinutes(int seconds) {
        if (seconds > 60) {
            return String.format("%.2f", secondsToMinutes(seconds)) + " minutes";
        } else {
            return seconds + " seconds";
        }
    }

    public static ATM getAtmByHologram(Hologram hologram) {
        for (ATM atm : MinerobbersPlugin.getAtms()) {
            if (atm.getHologram().equals(hologram)) {
                return atm;
            }
        }
        return null;
    }

    public static ATM getNearestATM(Location loc) {
        ATM nearestATM = null;
        double nearestDistance = 3;
        for (ATM atm : MinerobbersPlugin.getAtms()) {
            double distance = loc.distance(atm.getLocation());
            if (distance < nearestDistance) {
                nearestDistance = distance;
                nearestATM = atm;
            }
        }
        return nearestATM;
    }

    public static Store getNearestStore(Location loc) {
        Store nearestStore = null;
        double nearestDistance = 3;
        for (Store store : MinerobbersPlugin.getStores()) {
            double distance = loc.distance(store.storeLOCATION);
            if (distance < nearestDistance) {
                nearestDistance = distance;
                nearestStore = store;
            }
        }
        return nearestStore;
    }

    public static void giveMoneyOverTime(Player player, int money, int seconds, Location mustBeNear) {
        int moneyPerSecond = money / seconds;
        int startTime = getUnixTime();
        new BukkitRunnable() {
            @Override
            public void run() {
                if(mustBeNear.distance(player.getLocation()) < 3) {
                    int totalEarned = moneyPerSecond * (getUnixTime() - startTime);
                    player.sendTitle(Methods.chatColor("&a&l+" + totalEarned + "&7&l$"), null, 0, 20, 0);
                    // give money player here
                    if(startTime + seconds < getUnixTime()) {
                        player.sendTitle(Methods.chatColor("&aYou earned &a&l+" + money + "&7&l$ &ain total!"), null, 0, 40, 0);
                        cancel();
                    }
                } else {
                    player.sendTitle(Methods.chatColor("&c&lYou left the robbery area"), null, 0, 40, 0);
                    cancel();
                }
            }
        }.runTaskTimer(MinerobbersPlugin.getInstance(), 0, 20);
    }

    public static int getRandomIntFromRange(int min, int max) {
        return (int) (Math.random() * ((max - min) + 1)) + min;
    }
}
