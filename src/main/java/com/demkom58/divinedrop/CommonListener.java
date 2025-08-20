package com.demkom58.divinedrop;

import com.demkom58.divinedrop.config.ConfigData;
import com.demkom58.divinedrop.util.UpdateChecker;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;

public class CommonListener implements Listener {
    private final DivineDrop plugin;

    public CommonListener(@NotNull final DivineDrop plugin) {
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
            player.sendMessage(ChatColor.DARK_PURPLE + "New version of DivineDrop found. Latest: " + latest.getVersion_number());
            player.sendMessage(ChatColor.LIGHT_PURPLE + "You can update it here: " + ChatColor.GRAY + webSpigot.getResourceLink());
        }, true);
    }

}
