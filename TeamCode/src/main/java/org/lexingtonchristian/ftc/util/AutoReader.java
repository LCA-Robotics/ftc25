package org.lexingtonchristian.ftc.util;

import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.lexingtonchristian.ftc.components.drive.Mecanum;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AutoReader {

    protected File file;
    protected BufferedReader reader;

    public AutoReader(String filename) throws IOException {

        this.file = new File(filename);
        if (!file.isAbsolute()) this.file = new File(AppUtil.ROBOT_DATA_DIR, filename);

        File directory = file.getParentFile();
        AppUtil.getInstance().ensureDirectoryExists(directory);

        this.reader = new BufferedReader(new FileReader(file));

    }

    public Map<String, Double> nextSnapshot() throws IOException {

        Map<String, Double> snapshot = new HashMap<>();

        String line = reader.readLine();
        if (line == null) return null;
        List<String> data = new ArrayList<>(Arrays.asList(line.split("\t")));

        for (String motor : data) {
            String[] info = motor.split(" ");
            if (info.length < 2) break;
            snapshot.put(info[0], Double.parseDouble(info[1]));
        }

        return snapshot;

    }

    public void close() {
        try {
            reader.close();
        } catch (IOException ignored) {}
    }

}
