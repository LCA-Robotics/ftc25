package org.lexingtonchristian.ftc.util;

import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.lexingtonchristian.ftc.snapshot.DeviceSnapshot;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class AutoWriter {

    protected File file;
    protected BufferedWriter writer;

    public AutoWriter(String filename) throws IOException {

        this.file = new File(filename);
        if (!file.isAbsolute()) this.file = new File(AppUtil.ROBOT_DATA_DIR, filename);

        File directory = file.getParentFile();
        AppUtil.getInstance().ensureDirectoryExists(directory);

        this.writer = new BufferedWriter(new FileWriter(file));

    }

    public void writeSnapshot(DeviceSnapshot... snapshots) throws IOException {

        for (DeviceSnapshot snapshot : snapshots) {

            writer.append(snapshot.name);
            writer.append(' ');
            writer.append(String.valueOf(snapshot.value));

            writer.append('\t');

        }

        writer.append("\r\n");

    }

    public void close() {
        try {
            writer.close();
        } catch (IOException ignored) {}
    }

}
