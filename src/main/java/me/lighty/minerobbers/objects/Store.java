package me.lighty.minerobbers.objects;

import com.github.unldenis.hologram.Hologram;
import com.github.unldenis.hologram.animation.Animation;
import com.github.unldenis.hologram.line.ItemLine;
import de.leonhard.storage.Json;
import lombok.Getter;
import lombok.Setter;
import me.lighty.minerobbers.MinerobbersPlugin;
import me.lighty.minerobbers.utils.Methods;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class Store {

    // STORE VALUES
    @Getter @Setter public String storeNAME;
    @Getter @Setter public Location storeLOCATION;

    @Getter @Setter public int storeID;
    @Getter @Setter public int storePRICE;
    @Getter @Setter public int storeMINIMUMROBPRICE;
    @Getter @Setter public int storeMAXIMUMROBPRICE;
    @Getter @Setter public int storeLASTROBBED;

    @Getter @Setter public UUID storeOWNER;
    @Getter @Setter public UUID storeLASTROBBER;

    @Getter public Hologram hologram;

    public Store(int id, String owner, Location location, String name, int price, int minimumRobPrice, int maximumRobPrice, int lastRobbed, String lastRobber) {
        storeID = id;
        storeOWNER = UUID.fromString(owner);
        storeLOCATION = location;
        storeNAME = name;
        storePRICE = price;
        storeLASTROBBER = UUID.fromString(lastRobber);
        storeLASTROBBED = lastRobbed;
        storeMINIMUMROBPRICE = minimumRobPrice;
        storeMAXIMUMROBPRICE = maximumRobPrice;

        MinerobbersPlugin.getStores().add(this);
        createHologram();
    }

    public void saveStore() {
        Json storesJson = MinerobbersPlugin.getStoresJson();
        storesJson.set("stores." + storeID + ".owner", storeOWNER.toString());
        storesJson.set("stores." + storeID + ".location", Methods.locationToString(storeLOCATION));
        storesJson.set("stores." + storeID + ".name", storeNAME);
        storesJson.set("stores." + storeID + ".price", storePRICE);
        storesJson.set("stores." + storeID + ".minRobPrice", storeMINIMUMROBPRICE);
        storesJson.set("stores." + storeID + ".maxRobPrice", storeMAXIMUMROBPRICE);
        storesJson.set("stores." + storeID + ".lastRobbed", storeLASTROBBED);
        storesJson.set("stores." + storeID + ".lastRobber", storeLASTROBBER.toString());
        createHologram();
    }

    public void deleteStore() {
        deleteHologram();
        MinerobbersPlugin.getStoresJson().remove("stores." + storeID);
        MinerobbersPlugin.getStores().remove(this);
    }

    public void createHologram() {
        deleteHologram();
        this.hologram = Hologram.builder()
                .location(storeLOCATION.clone().add(0, -1.2, 0))
                .addLine(Methods.chatColor("&6&l" + storeNAME), false)
                .addLine(new ItemStack(Material.GOLD_BLOCK))
                .build(MinerobbersPlugin.getHologramPool());

        this.hologram.getLines().get(1).setAnimation(Animation.AnimationType.CIRCLE);
    }

    public void deleteHologram() {
        if(this.hologram != null) {
            MinerobbersPlugin.getHologramPool().remove(this.hologram);
        }
    }
}
