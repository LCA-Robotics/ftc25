package org.lexingtonchristian.ftc.snapshot.io;

import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.lexingtonchristian.ftc.snapshot.Device;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class SnapshotReader {

    protected File file;
    protected BufferedReader reader;

    public SnapshotReader(String filename) throws IOException {

        this.file = new File(filename);
        if (!file.isAbsolute()) this.file = new File(AppUtil.ROBOT_DATA_DIR, filename);

        File directory = file.getParentFile();
        AppUtil.getInstance().ensureDirectoryExists(directory);

        this.reader = new BufferedReader(new FileReader(file));

    }

    public <T> Device.Snapshot<T> nextSnapshot() throws IOException {

        String line = reader.readLine();
        if (line == null || line.equals("\r\n")) return null;
        String[] separated = line.split("\t");

        String name = separated[0];
        T value = (T) separated[1];

        return new Device.Snapshot<>(name, value);

    }

    public void close() {
        try {
            reader.close();
        } catch (IOException ignored) {}
    }

}
