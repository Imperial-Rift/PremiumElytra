package dev.jdog.premiumelytra.listeners;

import dev.jdog.premiumelytra.CheckFlightTask;
import dev.jdog.premiumelytra.PlayerGlideStatus;
import dev.jdog.premiumelytra.PremiumElytra;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityToggleGlideEvent;

import java.util.Timer;
import java.util.TimerTask;

public class GlideToggleListener implements Listener {
    @EventHandler
    public void onGlideToggle(EntityToggleGlideEvent e) {
        Economy economy = PremiumElytra.getEconomy();
        Timer timer = new Timer();
        TimerTask tt = null;


        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();

            if (e.isGliding()) {
                if (economy.getBalance(p) <= 10) {
                    e.setCancelled(true);
                } else {
                    PremiumElytra.setPlayerGlideStatus(p, economy.getBalance(p));
                    PremiumElytra.getBossBar().addPlayer(p);
                    PremiumElytra.getBossBar().setTitle(ChatColor.YELLOW + economy.format(economy.getBalance(p)));
                }
            } else {
                PremiumElytra.getBossBar().removePlayer(p);
                PlayerGlideStatus playerGlideStatus = PremiumElytra.getPlayerGlideStatus(p);
                PremiumElytra.deletePlayerGlidingStatus(p);

                Double cost = playerGlideStatus.getLocation().distance(p.getLocation());


                if (economy.getBalance(p) - cost <= 0) {
                    economy.withdrawPlayer(p, economy.getBalance(p));
                } else {
                    economy.withdrawPlayer(p, cost);
                }
            }
        }
    }
}
