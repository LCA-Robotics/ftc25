package org.lexingtonchristian.ftc.action;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.lexingtonchristian.ftc.json.ActionDeserializer;

import java.util.List;

@JsonDeserialize(using = ActionDeserializer.class)
public abstract class Action {

    private String name;
    private List<Object> args;

    abstract void run(HardwareMap map);

    protected void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException except) {
            Thread.currentThread().interrupt();
        }
    }

}
