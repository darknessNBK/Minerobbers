package me.lighty.minerobbers.objects;

import com.github.unldenis.hologram.Hologram;
import com.github.unldenis.hologram.TextLine;
import com.github.unldenis.hologram.animation.Animation;
import com.github.unldenis.hologram.line.ItemLine;
import de.leonhard.storage.Json;
import lombok.Getter;
import me.lighty.minerobbers.MinerobbersPlugin;
import me.lighty.minerobbers.utils.Methods;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class ATM {

    @Getter public int ID;
    @Getter public int lastRobbery;
    @Getter public UUID lastRobber;
    @Getter public Location location;
    @Getter public Hologram hologram;

    public ATM(int id, Location location, UUID robber, int robberyTime) {
        this.ID = id;
        this.location = location;
        this.lastRobber = robber;
        this.lastRobbery = robberyTime;

        new BukkitRunnable() {
            @Override
            public void run() {
                if(!MinerobbersPlugin.getAtms().contains(ATM.this)) {
                    cancel();
                    return;
                }
                updateHologram();
            }
        }.runTaskTimer(MinerobbersPlugin.getInstance(), 0, 20);

        Bukkit.getLogger().info("Creating ATM with ID " + id);
        MinerobbersPlugin.getAtms().add(this);
        createHologram();
    }

    public void saveATM() {
        Json atmsJson = MinerobbersPlugin.getAtmsJson();
        atmsJson.set("atms." + this.ID + ".location", Methods.locationToString(location));
        atmsJson.set("atms." + this.ID + ".lastRobbery", lastRobbery);
        atmsJson.set("atms." + this.ID + ".lastRobber", lastRobber.toString());
        createHologram();
    }

    public void robATM(Player player) {
        if(Methods.getUnixTime() - lastRobbery < 180) {
            player.sendMessage("§cYou can't rob this ATM for another " + (180 - (Methods.getUnixTime() - lastRobbery)) + " seconds!");
        } else {
            this.lastRobbery = Methods.getUnixTime();
            this.lastRobber = player.getUniqueId();
            updateHologram();
            saveATM();

            int money = Methods.getRandomIntFromRange(750, 1500);
            Methods.giveMoneyOverTime(player, money, 15, this.location);
            player.sendMessage(Methods.chatColor("&aYou started to rob this ATM!"));

        }
    }

    public void updateHologram() {
        if(Methods.getUnixTime() - lastRobbery < 180) {
            String text = "§c§lLast robbed: §e" + Methods.secondsOrMinutes(((Methods.getUnixTime() - lastRobbery))) + " ago";
            TextLine line = (TextLine) this.hologram.getLines().get(1);
            ItemLine itemLine = (ItemLine) this.hologram.getLines().get(2);
            itemLine.set(new ItemStack(Material.COAL_BLOCK));
            line.set(text);
        } else {
            String text = Methods.chatColor("&7&oType /robbery to rob this ATM.");
            ItemLine itemLine = (ItemLine) this.hologram.getLines().get(2);
            itemLine.set(new ItemStack(Material.EMERALD_BLOCK));
            TextLine line = (TextLine) this.hologram.getLines().get(1);
            line.set(text);
        }

    }

    public void createHologram() {
        deleteHologram();
        String robLine;
        if(Methods.getUnixTime() - lastRobbery < 180) {
            robLine = "§c§lLast robbed: §e" + Methods.secondsOrMinutes(((Methods.getUnixTime() - lastRobbery))) + " ago";
        } else {
            robLine = Methods.chatColor("&7&oType /robbery to rob this ATM.");
        }

        this.hologram = Hologram.builder()
                .location(this.location.clone().add(0, -1.2, 0))
                .addLine("§a§lATM", true)
                .addLine(robLine, true)
                .addLine(new ItemStack(Material.EMERALD_BLOCK))
                .build(MinerobbersPlugin.getHologramPool());
        this.hologram.getLines().get(2).setAnimation(Animation.AnimationType.CIRCLE);
    }

    public void deleteHologram() {
        if(this.hologram != null) {
            MinerobbersPlugin.getHologramPool().remove(this.hologram);
        }
    }

    public void delete() {
        deleteHologram();
        MinerobbersPlugin.getAtms().remove(this);
        MinerobbersPlugin.getAtmsJson().remove("atms." + ID);
    }

    public boolean canRob() {
        return Methods.getUnixTime() - lastRobbery > 180;
    }
}
