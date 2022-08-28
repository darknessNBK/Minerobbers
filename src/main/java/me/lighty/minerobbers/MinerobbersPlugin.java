package me.lighty.minerobbers;

import com.github.unldenis.hologram.HologramPool;
import de.leonhard.storage.Json;
import lombok.Getter;
import me.lighty.minerobbers.commands.MinerobbersCmd;
import me.lighty.minerobbers.handlers.ChatHandler;
import me.lighty.minerobbers.listeners.ChatListener;
import me.lighty.minerobbers.objects.ATM;
import me.lighty.minerobbers.objects.Store;
import me.lighty.minerobbers.utils.Methods;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public final class MinerobbersPlugin extends JavaPlugin {

    @Getter private static MinerobbersPlugin instance;
    @Getter private static HologramPool hologramPool;
    @Getter private static ArrayList<Store> stores = new ArrayList<>();
    @Getter private static ArrayList<ATM> atms = new ArrayList<>();
    @Getter private static HashMap<Player, ChatHandler> chatHandlers = new HashMap<>();
    @Getter private static Json storesJson;
    @Getter private static Json atmsJson;
    @Getter private static Json playerDataJson;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        hologramPool = new HologramPool(this, 15, 5f, 0.5f);
        storesJson = new Json(new File("plugins/Minerobbers/data/stores.json"));
        playerDataJson = new Json(new File("plugins/Minerobbers/data/playerdata.json"));
        atmsJson = new Json(new File("plugins/Minerobbers/data/atms.json"));

        Methods.loadAllStoresFromConfig();
        Methods.loadAllATMsFromConfig();

        getCommand("minerobbers").setExecutor(new MinerobbersCmd());
        getServer().getPluginManager().registerEvents(new ChatListener(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        for(Store store : stores) {
            store.deleteHologram();
        }
        for(ATM atm : atms) {
            atm.deleteHologram();
        }
    }

    public void reload() {
        storesJson = new Json(new File("plugins/Minerobbers/data/stores.json"));
        playerDataJson = new Json(new File("plugins/Minerobbers/data/playerdata.json"));
        atmsJson = new Json(new File("plugins/Minerobbers/data/atms.json"));
        for(Store store : stores) {
            store.deleteHologram();
        }
        for(ATM atm : atms) {
            atm.deleteHologram();
        }
        Methods.loadAllStoresFromConfig();
        Methods.loadAllATMsFromConfig();
    }
}
