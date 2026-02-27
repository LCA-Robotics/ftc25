package org.lexingtonchristian.ftc.snapshot.io;

import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.lexingtonchristian.ftc.snapshot.Device;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class SnapshotWriter {

    protected File file;
    protected BufferedWriter writer;

    public SnapshotWriter(String filename) throws IOException {

        this.file = new File(filename);
        if (!file.isAbsolute()) this.file = new File(AppUtil.ROBOT_DATA_DIR, filename);

        File directory = file.getParentFile();
        AppUtil.getInstance().ensureDirectoryExists(directory);

        this.writer = new BufferedWriter(new FileWriter(file));

    }

    public <T> SnapshotWriter writeSnapshot(Device.Snapshot<T> snapshot) throws IOException {
        writer.append(String.format("%s\t%s\t", snapshot.name, snapshot.value));
        return this;
    }

    public void endLine() throws IOException {
        writer.append("\r\n");
    }

    public void close() {
        try {
            writer.close();
        } catch (IOException ignored) {}
    }

}
