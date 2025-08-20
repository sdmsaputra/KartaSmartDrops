package com.minekartastudio.kartasmartdrops;

import com.minekartastudio.kartasmartdrops.config.Config;
import com.minekartastudio.kartasmartdrops.config.StaticData;
import com.minekartastudio.kartasmartdrops.drop.ItemHandler;
import com.minekartastudio.kartasmartdrops.lang.LangManager;
import com.minekartastudio.kartasmartdrops.metric.MetricService;
import com.minekartastudio.kartasmartdrops.util.UpdateChecker;
import com.minekartastudio.kartasmartdrops.version.SemVer;
import com.minekartastudio.kartasmartdrops.version.Version;
import com.minekartastudio.kartasmartdrops.version.VersionManager;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import space.arim.morepaperlib.MorePaperLib;

import java.util.Objects;
import java.util.Optional;
import java.util.logging.Logger;

@Getter
public final class KartaSmartDrops extends JavaPlugin {
    @Getter
    private static KartaSmartDrops instance;
    @Getter
    private static MorePaperLib morePaperLib;

    private final MetricService metricService = new MetricService(this);
    private final UpdateChecker webSpigot = new UpdateChecker(this, new SemVer(getDescription().getVersion()),
            Objects.requireNonNull(VersionManager.detectMinecraftVersion()), "spigot", StaticData.RESOURCE_ID);

    private final VersionManager versionManager = new VersionManager(this);
    private final Config configuration = new Config("config", this, versionManager, 3);
    private final LangManager langManager = new LangManager(this, configuration.getConfigData());
    private final ItemHandler itemHandler = new ItemHandler(this, versionManager, configuration.getConfigData());

    @Override
    public void onEnable() {
        KartaSmartDrops.instance = this;
        KartaSmartDrops.morePaperLib = new MorePaperLib(this);

        try {
            metricService.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            versionManager.setup();
        } catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage("[" + getDescription().getName() + "] " + ChatColor.RED + e.getMessage());
            Bukkit.getPluginManager().disablePlugin(this);
            e.printStackTrace();
            return;
        }

        final Version version = versionManager.getVersion();
        final PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(version.createListener(itemHandler), this);
        pluginManager.registerEvents(new CommonListener(this), this);

        Optional.ofNullable(getCommand("kartasmartdrops"))
                .ifPresent(cmd -> cmd.setExecutor(
                        new KartaSmartDropsCommandHandler(this, versionManager, configuration.getConfigData())
                ));

        reloadPlugin(version);

        if (configuration.getConfigData().isCheckUpdates()) {
            webSpigot.checkIfOutdated((latestVersion) -> {
                final Logger logger = Bukkit.getLogger();
                logger.info("New version '" + latestVersion.getVersion_number() + "' detected.");
                logger.info("Please update it on: " + webSpigot.getResourceLink());
            }, false);
        }
    }

    @Override
    public void onDisable() {
        morePaperLib.scheduling().cancelGlobalTasks();
        itemHandler.disable();
    }

    public boolean reloadPlugin(@NotNull final Version version) {
        final boolean loadedConfig = loadConfig(version);

        if (loadedConfig)
            itemHandler.reload();

        return loadedConfig;
    }

    public boolean loadConfig(@NotNull final Version version) {
        if (configuration.load()) {
            return langManager.manageLang(configuration.getConfigData().getLang(), version.getClient());
        }

        return false;
    }

}
