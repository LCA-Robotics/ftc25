package org.lexingtonchristian.ftc.snapshot;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class DeviceSnapshot {

    public final String name;
    public final double value;

    public DeviceSnapshot(String name, double value) {
        this.name = name;
        this.value = value;
    }

}
