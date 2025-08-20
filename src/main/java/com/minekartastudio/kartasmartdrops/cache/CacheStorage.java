package com.minekartastudio.kartasmartdrops.cache;

import com.minekartastudio.kartasmartdrops.lang.Downloader;
import com.minekartastudio.kartasmartdrops.version.Version;
import com.google.gson.stream.JsonReader;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class CacheStorage {
    private Map<String, Map<String, String>> versionLangsCache = new HashMap<>();

    private CacheStorage() { }

    protected CacheStorage(Map<String, Map<String, String>> versionLangsCache) {
        this.versionLangsCache = versionLangsCache;
    }

    public static CacheStorage load() {
        try (JsonReader reader = new JsonReader(new InputStreamReader(CacheStorage.class.getResourceAsStream("/cache.json")))) {
            return Downloader.GSON.fromJson(reader, CacheStorage.class);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public @Nullable String getLink(@NotNull final Version.ResourceClient client,
                                    @NotNull final String language) {
        final Map<String, String> languages = versionLangsCache.get(client.id());

        if (languages == null)
            return null;

        return languages.get(client.reformatLangCode(language));
    }

}
