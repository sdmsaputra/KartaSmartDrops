package com.minekartastudio.kartasmartdrops.config.updaters;

import com.minekartastudio.kartasmartdrops.config.Config;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.function.Consumer;

public class Updater1 implements Consumer<Config> {
    @Override
    public void accept(Config config) {
        final FileConfiguration cfg = config.getConfig();

        cfg.set("config-version", 2);
        cfg.getConfigurationSection("messages")
                .set("prefix", "&5&lKartaSmartDrops &7> &f");

        config.save();
    }
}
