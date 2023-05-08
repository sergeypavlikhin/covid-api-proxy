package com.pvsrg.covidapi.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app")
public record AppProperties(

        String externalSourceURL,

        long requestDelayMs,
        long coolingTimeSec,
        double coolingTimeMultiplier,
        long coolingTimeMax
) {
}
