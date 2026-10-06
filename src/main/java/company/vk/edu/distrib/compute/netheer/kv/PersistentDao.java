package company.vk.edu.distrib.compute.netheer.kv;

import company.vk.edu.distrib.compute.Dao;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;

public final class PersistentDao implements Dao<byte[]> {
    private final Path storagePath;
    private static final byte UPSERT = 1;
    private static final byte DELETE = 2;
    private final Map<String, byte[]> data = new ConcurrentHashMap<>();
    private final DataOutputStream output;

    public PersistentDao(Path storagePath) throws IOException {
        if (storagePath == null) {
            throw new IllegalArgumentException("Storage path can't be null");
        }
        this.storagePath = storagePath.toAbsolutePath().normalize();
        Path parentDirectory = this.storagePath.getParent();
        Files.createDirectories(parentDirectory);

        if (Files.exists(this.storagePath)) {
            uploadData();
        }

        this.output = new DataOutputStream(new BufferedOutputStream(Files.newOutputStream(
                this.storagePath,
                StandardOpenOption.CREATE,
                StandardOpenOption.WRITE,
                StandardOpenOption.APPEND)));
    }

    @Override
    public byte[] get(String key) throws IOException {
        if (key.isEmpty()) {
            throw new IllegalArgumentException("Key is empty");
        }
        byte[] value = data.get(key);
        if (value == null) {
            throw new NoSuchElementException("Key not found: " + key);
        }

        return value;
    }

    @Override
    public synchronized void upsert(String key, byte[] value) throws IOException {
        if (key.isEmpty()) {
            throw new IllegalArgumentException("Key is empty");
        }

        byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);

        output.writeByte(UPSERT);
        output.writeInt(keyBytes.length);
        output.write(keyBytes);
        output.writeInt(value.length);
        output.write(value);
        output.flush();

        data.put(key, value);
    }

    @Override
    public synchronized void delete(String key) throws IOException {
        if (key.isEmpty()) {
            throw new IllegalArgumentException("Key is empty");
        }

        byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);

        output.writeByte(DELETE);
        output.writeInt(keyBytes.length);
        output.write(keyBytes);
        output.flush();

        data.remove(key);
    }

    @Override
    public synchronized void close() throws IOException {
        output.close();
    }

    private void uploadData() throws IOException {
        try (DataInputStream input = new DataInputStream(
                new BufferedInputStream(
                Files.newInputStream(this.storagePath)))) {
            int operation;
            while ((operation = input.read()) != -1) {
                int keyLength = input.readInt();
                byte[] keyBytes = new byte[keyLength];
                input.readFully(keyBytes);
                String key = new String(keyBytes, StandardCharsets.UTF_8);

                switch (operation) {
                    case UPSERT -> {
                        int valueLength = input.readInt();
                        byte[] valueBytes = new byte[valueLength];
                        input.readFully(valueBytes);
                        data.put(key, valueBytes);
                    }
                    case DELETE -> {
                        data.remove(key);
                    }
                    default -> throw new IOException("Unknown operation" + operation);
                }
            }
        }
    }
}
