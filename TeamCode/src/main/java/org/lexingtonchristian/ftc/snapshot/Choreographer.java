package org.lexingtonchristian.ftc.snapshot;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.lexingtonchristian.ftc.snapshot.io.SnapshotReader;
import org.lexingtonchristian.ftc.snapshot.io.SnapshotWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Choreographer {

    protected SnapshotReader reader;
    protected SnapshotWriter writer;

    protected List<Device<?>> devices;

    public Choreographer(String filename, List<Device<?>> devices) {
        try {
            this.reader = new SnapshotReader(filename);
            this.writer = new SnapshotWriter(filename);
        } catch (IOException error) {
            throw new RuntimeException(error);
        }
        this.devices = devices;
    }

    /**
     * Records a series of snapshots to the save file
     */
    public void writePicture() {
        try {
            for (Device<?> device : devices) {
                this.writer.writeSnapshot(device.takeSnapshot());
            }
            this.writer.endLine();
        } catch (IOException error) {
            throw new RuntimeException(error);
        }
    }

    public Map<String, ?> readPicture() {
        List<Device.Snapshot<?>> snapshots = new ArrayList<>();
        try {
            while (true) {
                Device.Snapshot<?> snapshot = reader.nextSnapshot();
                if (snapshot == null) break;
                snapshots.add(reader.nextSnapshot());
            }
        } catch (IOException error) {
            throw new RuntimeException(error);
        }
        return snapshots.stream().collect(Collectors.toMap(
                snapshot -> snapshot.name,
                snapshot -> snapshot.value
        ));
    }

    public void close() {
        this.reader.close();
        this.writer.close();
    }

    public static class Builder {

        private final String filename;
        private final List<Device<?>> devices;

        public Builder(String filename) {
            this.filename = filename;
            this.devices = new ArrayList<>();
        }

        public <T> Builder addDevice(Device<T> device) {
            this.devices.add(device);
            return this;
        }

        public <T> Builder addDevice(String name, Class<? extends Device<T>> type, HardwareMap map) {
            this.devices.add(map.get(type, name));
            return this;
        }

        public Choreographer build() {
            return new Choreographer(filename, devices);
        }

    }

}
