package dev.jdog.premiumelytra;

import dev.jdog.premiumelytra.listeners.GlideToggleListener;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public final class PremiumElytra extends JavaPlugin implements Listener {
    final static HashMap<String, PlayerGlideStatus> playerGlidingStatuses = new HashMap<>();
    private static Economy econ = null;

    private static BossBar bossBar;

    private static final ScheduledExecutorService EXECUTOR = Executors.newSingleThreadScheduledExecutor();



    @Override
    public void onEnable() {
        // Plugin startup logic

        if (!setupEconomy() ) {
            getLogger().severe("Disabled due to no Vault dependency found!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        getLogger().info("Starting up");

        getServer().getPluginManager().registerEvents(new GlideToggleListener(), this);

        // Timer
        new BukkitRunnable() {
            @Override
            public void run() {
                new CheckFlightTask(playerGlidingStatuses).run();
            }
        }.runTaskTimerAsynchronously(this, 0, 20);


        // BossBar
        bossBar = Bukkit.createBossBar(
                ChatColor.YELLOW + "$10,000",
                BarColor.GREEN,
                BarStyle.SEGMENTED_20
        );
    }


    public static Economy getEconomy() {
        return econ;
    }

    public static BossBar getBossBar() {
        return bossBar;
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    public static void setPlayerGlideStatus(Player p, Double startingMoney) {
        Location location = p.getLocation();
        PlayerGlideStatus playerGlideStatus = new PlayerGlideStatus(location, startingMoney);
        if (playerGlidingStatuses.containsKey(p.getName())) {

            playerGlidingStatuses.replace(p.getName(), playerGlideStatus);
        } else {
            playerGlidingStatuses.put(p.getName(), playerGlideStatus);
        }
    }

    public static PlayerGlideStatus getPlayerGlideStatus(Player p) {
        return playerGlidingStatuses.get(p.getName());
    }

    public static void setPlayerGlidingStatusLocation(Player p, Location location) {
        Double startingMoney = playerGlidingStatuses.get(p.getName()).getStartingMoney();
        PlayerGlideStatus playerGlideStatus = new PlayerGlideStatus(location, startingMoney);
        playerGlidingStatuses.replace(p.getName(), playerGlideStatus);
    }

    public static void deletePlayerGlidingStatus(Player p) {
        playerGlidingStatuses.remove(p.getName());

    }
}
