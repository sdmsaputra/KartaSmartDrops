package com.minekartastudio.kartasmartdrops.version.V11R1;

import com.minekartastudio.kartasmartdrops.version.V8R3.V8ResourceClient;
import org.jetbrains.annotations.NotNull;

public class V11ResourceClient extends V8ResourceClient {
    public V11ResourceClient(@NotNull String id) {
        super(id);
    }

    @Override
    public @NotNull String getLangPath(@NotNull String locale) {
        return String.format(V8ResourceClient.PATH, locale.toLowerCase());
    }

    @Override
    public @NotNull String reformatLangCode(@NotNull String localeCode) {
        return localeCode.toLowerCase();
    }
}
