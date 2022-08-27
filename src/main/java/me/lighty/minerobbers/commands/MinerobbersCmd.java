package me.lighty.minerobbers.commands;

import me.lighty.minerobbers.MinerobbersPlugin;
import me.lighty.minerobbers.guis.EditStoreGUI;
import me.lighty.minerobbers.objects.Store;
import me.lighty.minerobbers.utils.Methods;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class MinerobbersCmd implements CommandExecutor {
    @Override
    public boolean onCommand(org.bukkit.command.CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (args.length < 1) {
            sender.sendMessage("/minerobbers reload");
            return true;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            if (sender.hasPermission("minerobbers.reload")) {
                MinerobbersPlugin.getInstance().reload();
                sender.sendMessage("§aSuccessfully reloaded the plugin!");
                return true;
            } else {
                sender.sendMessage("§cYou do not have permission to do this!");
                return true;
            }
        }

        if (args[0].equalsIgnoreCase("help")) {
            sender.sendMessage("§a/minerobbers reload");
            return true;
        }

        if (args[0].equalsIgnoreCase("store")) {
            if (args.length < 2) {
                sender.sendMessage("§c/minerobbers store <create/delete/edit/list/tp/info>");
                return true;
            }

            if (args[1].equalsIgnoreCase("create")) {
                if (!(sender instanceof Player)) {
                    sender.sendMessage("§cYou must be a player to do this!");
                    return true;
                }

                Player player = (Player) sender;
                if (player.hasPermission("minerobbers.store.create")) {
                    int perfectID = Methods.findPerfectID();

                    Store newStore = new Store(perfectID, Methods.nullUUID(), player.getLocation(), "Store #" + perfectID, 0, 0, 0, 0, Methods.nullUUID());
                    newStore.saveStore();

                    player.sendMessage("§aSuccessfully created a store with ID " + perfectID + "!");
                    return true;

                } else {
                    sender.sendMessage("§cYou do not have permission to do this!");
                    return true;
                }
            }

            if (args[1].equalsIgnoreCase("delete")) {
                if (!(sender instanceof Player)) {
                    sender.sendMessage("§cYou must be a player to do this!");
                    return true;
                }

                Player player = (Player) sender;
                if (player.hasPermission("minerobbers.store.delete")) {
                    if (args.length < 3) {
                        sender.sendMessage("§c/minerobbers store delete <storeID>");
                        return true;
                    }

                    if (!Methods.isInt(args[2])) {
                        sender.sendMessage("§cInvalid storeID!");
                        return true;
                    }

                    int storeID = Integer.parseInt(args[2]);
                    if (!Methods.doesStoreExist(storeID)) {
                        sender.sendMessage("§cStore with ID " + storeID + " does not exist!");
                        return true;
                    }

                    Store store = Methods.getStore(storeID);
                    store.deleteStore();
                    sender.sendMessage("§aSuccessfully deleted store with ID " + storeID + "!");
                    return true;
                } else {
                    sender.sendMessage("§cYou do not have permission to do this!");
                    return true;
                }
            }

            if (args[1].equalsIgnoreCase("tp")) {
                if (!(sender instanceof Player)) {
                    sender.sendMessage("§cYou must be a player to do this!");
                    return true;
                }

                Player player = (Player) sender;
                if (player.hasPermission("minerobbers.store.tp")) {
                    if (args.length < 3) {
                        sender.sendMessage("§c/minerobbers store tp <storeID>");
                        return true;
                    }

                    if (!Methods.isInt(args[2])) {
                        sender.sendMessage("§cInvalid store ID!");
                        return true;
                    }

                    int storeID = Integer.parseInt(args[2]);
                    if (!Methods.doesStoreExist(storeID)) {
                        sender.sendMessage("§cStore with ID " + storeID + " does not exist!");
                        return true;
                    }
                    Store store = Methods.getStore(storeID);
                    player.teleport(store.getStoreLOCATION());
                    sender.sendMessage("§aSuccessfully teleported to store with ID " + storeID + "!");
                    return true;
                } else {
                    sender.sendMessage("§cYou do not have permission to do this!");
                    return true;
                }
            }

            if (args[1].equalsIgnoreCase("info")) {
                if (args.length < 3) {
                    sender.sendMessage("§c/minerobbers store info <storeID>");
                    return true;
                }

                if (!sender.hasPermission("minerobbers.store.info")) {
                    sender.sendMessage("§cYou do not have permission to do this!");
                    return true;
                }

                if (!Methods.isInt(args[2])) {
                    sender.sendMessage("§cInvalid store ID!");
                    return true;
                }

                int storeID = Integer.parseInt(args[2]);
                if (!Methods.doesStoreExist(storeID)) {
                    sender.sendMessage("§cStore with ID " + storeID + " does not exist!");
                    return true;
                }

                Store store = Methods.getStore(storeID);
                sender.sendMessage(" ");
                sender.sendMessage(Methods.chatColor("&6&lSTORE INFO"));
                sender.sendMessage(Methods.chatColor("&aStore ID: &e" + store.getStoreID()));
                sender.sendMessage(Methods.chatColor("&aStore Name: &e" + store.getStoreNAME()));
                sender.sendMessage(Methods.chatColor("&aStore Owner: &e" + Bukkit.getOfflinePlayer(store.getStoreOWNER()).getName()));
                sender.sendMessage(Methods.chatColor("&aStore Location: &e" + store.getStoreLOCATION().getBlockX() + ", " + store.getStoreLOCATION().getBlockY() + ", " + store.getStoreLOCATION().getBlockZ() + " in world " + store.getStoreLOCATION().getWorld().getName()));
                return true;
            }

            if (args[1].equalsIgnoreCase("list")) {
                if (!sender.hasPermission("minerobbers.store.list")) {
                    sender.sendMessage("§cYou do not have permission to do this!");
                    return true;
                }

                if(!(sender instanceof Player)) {
                    sender.sendMessage("§cYou must be a player to do this!");
                    return true;
                }

                Methods.clearPlayerChat((Player) sender);
                sender.sendMessage(Methods.chatColor("&6&lSTORE LIST"));
                List<Store> paginatedStore = Methods.getPaginatedStores(args.length > 2 ? Integer.parseInt(args[2]) : 1);

                if (paginatedStore.size() == 0) {
                    sender.sendMessage(Methods.chatColor("&7&oThere are no stores to list!"));
                    return true;
                }

                for (Store store : paginatedStore) {
                    TextComponent storecomponent = new TextComponent(Methods.chatColor(store.storeNAME));
                    storecomponent.setItalic(true);
                    storecomponent.setColor(net.md_5.bungee.api.ChatColor.GRAY);
                    storecomponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(Methods.chatColor("&7&oClick to learn info about this store!")).create()));
                    storecomponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/minerobbers store info " + store.getStoreID()));
                    sender.sendMessage(storecomponent);
                }

                sender.sendMessage(" ");
                sender.sendMessage(Methods.pageNextAndBackStore(args.length > 2 ? Integer.parseInt(args[2]) : 1));
                return true;
            }

            if (args[1].equalsIgnoreCase("edit")) {
                if (!(sender instanceof Player)) {
                    sender.sendMessage("§cYou must be a player to do this!");
                    return true;
                }

                Player player = (Player) sender;
                if (player.hasPermission("minerobbers.store.edit")) {
                    if (args.length < 3) {
                        sender.sendMessage("§c/minerobbers store edit <storeID>");
                        return true;
                    }

                    if (!Methods.isInt(args[2])) {
                        sender.sendMessage("§cInvalid store ID!");
                        return true;
                    }

                    int storeID = Integer.parseInt(args[2]);
                    if (!Methods.doesStoreExist(storeID)) {
                        sender.sendMessage("§cStore with ID " + storeID + " does not exist!");
                        return true;
                    }

                    Store store = Methods.getStore(storeID);
                    EditStoreGUI gui = new EditStoreGUI(store);
                    Bukkit.getServer().getPluginManager().registerEvents(gui, MinerobbersPlugin.getInstance());
                    gui.open(player);
                    return true;
                } else {
                    sender.sendMessage("§cYou do not have permission to do this!");
                    return true;
                }
            }
        }
        return false;
    }

}
