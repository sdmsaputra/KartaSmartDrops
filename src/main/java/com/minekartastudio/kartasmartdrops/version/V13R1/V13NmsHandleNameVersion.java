package com.minekartastudio.kartasmartdrops.version.V13R1;

import com.minekartastudio.kartasmartdrops.drop.ItemHandler;
import com.minekartastudio.kartasmartdrops.lang.Language;
import com.minekartastudio.kartasmartdrops.version.V12R1.V12Listener;
import com.minekartastudio.kartasmartdrops.version.V8R3.NmsHandleNameVersion;
import lombok.SneakyThrows;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;

public class V13NmsHandleNameVersion extends NmsHandleNameVersion {
    public V13NmsHandleNameVersion(@NotNull final ResourceClient client,
                                   @Nullable final String nmsVersion,
                                   @NotNull final MethodHandle asNMSCopyHandle,
                                   @NotNull final MethodHandle getItemHandle,
                                   @NotNull final MethodHandle getNameHandle) {
        super(
                client,
                nmsVersion,
                asNMSCopyHandle.asType(MethodType.methodType(Object.class, ItemStack.class)),
                getItemHandle.asType(MethodType.methodType(Object.class, Object.class)),
                getNameHandle.asType(MethodType.methodType(String.class, Object.class))
        );
    }

    @NotNull
    @Override
    public Listener createListener(ItemHandler manager) {
        return new V12Listener(manager);
    }

    @NotNull
    @SneakyThrows
    @Override
    protected String getLangNameNMS(Object itemStack) {
        final Object item = getItemHandle.bindTo(itemStack).invokeExact();
        final String name = (String) getNameHandle.bindTo(item).invokeExact();
        return Language.getInstance().getLocName(name).trim();
    }
}
