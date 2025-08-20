package com.demkom58.divinedrop.version;

import com.demkom58.divinedrop.DivineDrop;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.regex.Pattern;

public class VersionManager {
    public static SupportedVersion detectedVersion = null;

    private final DivineDrop plugin;

    private Version version;
    private SupportedVersion supportedVersion;

    public VersionManager(@NotNull final DivineDrop plugin) {
        this.plugin = plugin;
    }

    /**
     * Prepares all for using version dependent methods.
     * <p>
     * It is recommended to be called in {@link Plugin#onEnable()} method
     * for in order to avoid exceptions and mistakes.
     *
     * @throws UnsupportedOperationException throws in situation when version is not supported or when it can't parse version.
     */
    public void setup() throws UnsupportedOperationException {
        final SemVer minecraftVersion = detectMinecraftVersion();
        this.supportedVersion = SupportedVersion.getVersion(minecraftVersion);

        if (this.supportedVersion == null) {
            throw new UnsupportedOperationException(
                    "Current version: " + Bukkit.getVersion() + ". This version is not supported!");
        }

        this.version = supportedVersion.create(minecraftVersion);

        VersionManager.detectedVersion = supportedVersion;

        final String nmsName = version.getNmsName();
        plugin.getLogger().info("Detected version: " + supportedVersion.name()
                + " (NMS: " + (nmsName == null ? "Generic" : nmsName) + ")"
                + ", while server version is: " + Bukkit.getVersion());
    }

    /**
     * Getter of {@link Version version} version field.
     * <p>
     * Shouldn't be called before {@link VersionManager#setup()}
     * because it sets version.
     *
     * @return detected version that give access to version depended on methods.
     */
    @NotNull
    public Version getVersion() {
        return version;
    }

    /**
     * Supported version enum constant.
     * <p>
     * Allows check newer or older version
     * or create new instances.
     *
     * @return version constant
     */
    public SupportedVersion getSupportedVersion() {
        return supportedVersion;
    }

    /**
     * Helps to check whether the current version
     * is newer than the specified version or not.
     *
     * @param version - version against which the check is performed.
     * @return true if newer.
     */
    public boolean isNewer(@NotNull final SupportedVersion version) {
        return supportedVersion.isNewer(version);
    }

    /**
     * Helps to check whether the current version
     * is older than the specified version or not.
     *
     * @param version - version against which the check is performed.
     * @return true if older.
     */
    public boolean isOlder(@NotNull final SupportedVersion version) {
        return supportedVersion.isOlder(version);
    }

    public static @Nullable SemVer detectMinecraftVersion() {
        final String bukkitVersion = Bukkit.getVersion(); // format: "1.21-37-dd49fba (MC: 1.21)"
        final Pattern compile = Pattern.compile("MC: ([0-9.]+)");
        final java.util.regex.Matcher matcher = compile.matcher(bukkitVersion);

        if (matcher.find()) {
            return new SemVer(matcher.group(1));
        }

        return null;
    }

}
