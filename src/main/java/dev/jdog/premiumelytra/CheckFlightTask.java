package dev.jdog.premiumelytra;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.sql.Time;
import java.util.HashMap;
import java.util.TimerTask;

public class CheckFlightTask extends TimerTask {
    private HashMap<String, PlayerGlideStatus> playerGlideStatuses;
    public CheckFlightTask(HashMap<String, PlayerGlideStatus> playerGlideStatuses) {
        this.playerGlideStatuses = playerGlideStatuses;
    }
    public void run() {
        Economy economy = PremiumElytra.getEconomy();

        for (String key: playerGlideStatuses.keySet()) {

            Player p = Bukkit.getPlayer(key);

            PlayerGlideStatus playerGlideStatus = PremiumElytra.getPlayerGlideStatus(p);

            Double cost = playerGlideStatus.getLocation().distance(p.getLocation());





            if (economy.getBalance(p) - cost <= 0) {
                if (p.getInventory().getChestplate() != null && p.getInventory().getChestplate().getType() == Material. ELYTRA) {
                    p.setGliding(false);
                    PremiumElytra.getBossBar().removePlayer(p);
                    economy.withdrawPlayer(p, economy.getBalance(p));

                    PremiumElytra.deletePlayerGlidingStatus(p);
                }
            } else {
                economy.withdrawPlayer(p, cost);
                PremiumElytra.setPlayerGlidingStatusLocation(p, p.getLocation());
                PremiumElytra.getBossBar().setProgress(economy.getBalance(p)/playerGlideStatus.getStartingMoney());
                PremiumElytra.getBossBar().setTitle(ChatColor.YELLOW + economy.format(economy.getBalance(p)));
            }


        }
    }
}
