package com.minekartastudio.kartasmartdrops;

import com.minekartastudio.kartasmartdrops.config.ConfigData;
import com.minekartastudio.kartasmartdrops.util.UpdateChecker;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;

public class CommonListener implements Listener {
    private final KartaSmartDrops plugin;

    public CommonListener(@NotNull final KartaSmartDrops plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(@NotNull final PlayerJoinEvent event) {
        final ConfigData configData = plugin.getConfiguration().getConfigData();
        if (!configData.isCheckUpdates() || !event.getPlayer().isOp())
            return;

        final Player player = event.getPlayer();
        final UpdateChecker webSpigot = plugin.getWebSpigot();
        webSpigot.checkIfOutdated(latest -> {
            player.sendMessage(ChatColor.DARK_PURPLE + "New version of KartaSmartDrops found. Latest: " + latest.getVersion_number());
            player.sendMessage(ChatColor.LIGHT_PURPLE + "You can update it here: " + ChatColor.GRAY + webSpigot.getResourceLink());
        }, true);
    }

}
