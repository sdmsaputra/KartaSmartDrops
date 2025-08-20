package com.minekartastudio.kartasmartdrops.config;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DataContainer {
    private int timer;
    private String format;

    public void timerDecrement() {
        timer--;
    }
}
