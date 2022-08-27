package me.lighty.minerobbers.guis;

import lombok.Getter;
import me.lighty.minerobbers.MinerobbersPlugin;
import me.lighty.minerobbers.handlers.ChatHandler;
import me.lighty.minerobbers.objects.Store;
import me.lighty.minerobbers.utils.Methods;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class EditStoreGUI implements Listener {

    @Getter public Inventory inv;
    @Getter public Store store;

    public EditStoreGUI(Store store) {
        this.inv = Bukkit.createInventory(null, 9, Methods.chatColor("Edit Store: " + store.storeNAME));
        this.store = store;

        initalizeItems();
    }

    public void initalizeItems() {
        inv.setItem(0, guiItem(Material.SPRUCE_SIGN, "&6Store Name", "&7Current: &a" + store.storeNAME, " ", "&7Click to edit"));
        inv.setItem(1, guiItem(Material.SPRUCE_DOOR, "&6Store Location", "&7Current: &a" + Methods.locationToString(store.storeLOCATION), " ", "&7Click to edit"));
        inv.setItem(2, guiItem(Material.NAME_TAG, "&6Store Price", "&7Current: &a" + store.storePRICE, " ", "&7Click to edit"));
        inv.setItem(3, guiItem(Material.SKELETON_SKULL, "&6Store Owner", "&7Current: &a" + Bukkit.getOfflinePlayer(store.storeOWNER).getName(), " ", "&7Click to edit"));
        inv.setItem(4, guiItem(Material.GOLD_NUGGET, "&6Minimum Rob Price", "&7Current: &a" + store.storeMINIMUMROBPRICE, " ", "&7Click to edit"));
        inv.setItem(5, guiItem(Material.GOLD_NUGGET, "&6Maximum Rob Price", "&7Current: &a" + store.storeMAXIMUMROBPRICE, " ", "&7Click to edit"));
    }

    protected ItemStack guiItem(final Material material, final String name, final String... lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Methods.chatColor(name));
        meta.setLore(Methods.chatColorList(List.of(lore)));
        item.setItemMeta(meta);
        return item;
    }

    public void open(Player player) {
        player.openInventory(inv);
    }

    @EventHandler
    public void inventoryClick(final InventoryClickEvent e) {
        if(!e.getInventory().equals(inv)) return;
        e.setCancelled(true);

        final ItemStack clickedItem = e.getCurrentItem();
        if (clickedItem == null || clickedItem.getType().isAir()) return;

        final Player player = (Player) e.getWhoClicked();
        final int slot = e.getSlot();

        if(slot == 0) {
            player.closeInventory();
            player.sendMessage(Methods.chatColor("&aPlease enter the new store name:"));
            ChatHandler handler = new ChatHandler(store, player, ChatHandler.ChatType.NAME);
            MinerobbersPlugin.getChatHandlers().put(player, handler);
            return;
        }

        if(slot == 1) {
            player.closeInventory();
            player.sendMessage(Methods.chatColor("&aLocation of the store changed to your location."));
            store.setStoreLOCATION(player.getLocation());
            store.saveStore();
            return;
        }

        if(slot == 2) {
            player.closeInventory();
            player.sendMessage(Methods.chatColor("&aPlease enter the new store price:"));
            ChatHandler handler = new ChatHandler(store, player, ChatHandler.ChatType.PRICE);
            MinerobbersPlugin.getChatHandlers().put(player, handler);
            return;
        }

        if(slot == 3) {
            player.closeInventory();
            player.sendMessage(Methods.chatColor("&aPlease enter the new store owner:"));
            ChatHandler handler = new ChatHandler(store, player, ChatHandler.ChatType.OWNER);
            MinerobbersPlugin.getChatHandlers().put(player, handler);
            return;
        }

        if(slot == 4) {
            player.closeInventory();
            player.sendMessage(Methods.chatColor("&aPlease enter the new minimum rob reward:"));
            ChatHandler handler = new ChatHandler(store, player, ChatHandler.ChatType.MINIMUMROBPRICE);
            MinerobbersPlugin.getChatHandlers().put(player, handler);
            return;
        }

        if(slot == 5) {
            player.closeInventory();
            player.sendMessage(Methods.chatColor("&aPlease enter the new maximum rob reward:"));
            ChatHandler handler = new ChatHandler(store, player, ChatHandler.ChatType.MAXIMUMROBPRICE);
            MinerobbersPlugin.getChatHandlers().put(player, handler);
            return;
        }

        player.sendMessage(Methods.chatColor("&8&oYou can type &c&lcancel &8&oto cancel editing."));
    }

    @EventHandler
    public void onInventoryClick(final InventoryDragEvent e) {
        if (e.getInventory().equals(inv)) {
            e.setCancelled(true);
        }
    }

}
