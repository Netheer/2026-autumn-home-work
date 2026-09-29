package company.vk.edu.distrib.compute.iizhukov.urlshortener.db;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;

public final class FileStorage implements Closeable {
    private final RandomAccessFile file;

    public FileStorage(File file) throws IOException {
        File parent = file.getParentFile();

        if (parent != null) {
            parent.mkdirs();
        }

        this.file = new RandomAccessFile(file, "rw");
    }

    @SuppressWarnings("PMD.AvoidSynchronizedStatement")
    public void write(Map<String, String> data) throws IOException {
        synchronized (file) {
            var snapshot = new HashMap<>(data);
            file.setLength(0);
            file.writeInt(snapshot.size());

            for (var entry : snapshot.entrySet()) {
                file.writeUTF(entry.getKey());
                file.writeUTF(entry.getValue());
            }
        }
    }

    public Map<String, String> read() throws IOException {
        var result = new HashMap<String, String>();
        file.seek(0);

        if (file.length() == 0) {
            return result;
        }

        var count = file.readInt();

        for (int i = 0; i < count; ++i) {
            result.put(file.readUTF(), file.readUTF());
        }

        return result;
    }

    @Override
    public void close() throws IOException {
        file.close();
    }
}
