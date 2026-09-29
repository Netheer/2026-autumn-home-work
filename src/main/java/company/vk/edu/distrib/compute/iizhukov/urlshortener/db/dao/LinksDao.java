package company.vk.edu.distrib.compute.iizhukov.urlshortener.db.dao;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;

import company.vk.edu.distrib.compute.Dao;
import company.vk.edu.distrib.compute.iizhukov.urlshortener.db.DataValidationUtils;
import company.vk.edu.distrib.compute.iizhukov.urlshortener.db.FileStorage;
import company.vk.edu.distrib.compute.iizhukov.urlshortener.db.StorageException;
import org.jspecify.annotations.Nullable;

public final class LinksDao implements Dao<String> {
    @Nullable
    private static LinksDao instance;
    private final Map<String, String> links;
    private final FileStorage storage;

    private LinksDao() throws IOException {
        storage = new FileStorage(new File("/tmp/iizhukov-urlshortener/links.db"));
        links = new ConcurrentHashMap<>(storage.read());
    }

    public static synchronized LinksDao create() {
        if (instance == null) {
            try {
                instance = new LinksDao();
            } catch (IOException e) {
                throw new StorageException("cant open file", e);
            }
        }

        return instance;
    }

    @Override
    public String get(String key) throws IllegalArgumentException {
        DataValidationUtils.validateKey(key);
        var value = links.get(key);

        if (value == null) {
            throw new NoSuchElementException();
        }

        return value;
    }

    @Override
    public void upsert(String key, String value) throws IllegalArgumentException {
        DataValidationUtils.validateKey(key);
        DataValidationUtils.validateUrl(value);
        try {
            links.put(key, value);
            storage.write(links);
        } catch (IOException e) {
            throw new StorageException("cant write file =(", e);
        }
    }

    @Override
    public void delete(String key) throws IllegalArgumentException {
        DataValidationUtils.validateKey(key);
        try {
            links.remove(key);
            storage.write(links);
        } catch (IOException e) {
            throw new StorageException("cant write file =(", e);
        }
    }

    @Override
    public void close() {
        try {
            storage.write(links);
            storage.close();
        } catch (IOException e) {
            throw new StorageException("cant close file", e);
        }
    }
}
