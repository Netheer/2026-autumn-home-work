package company.vk.edu.distrib.compute.iizhukov.urlshortener.db.dao;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;

import company.vk.edu.distrib.compute.Dao;
import company.vk.edu.distrib.compute.iizhukov.urlshortener.db.FileStorage;
import company.vk.edu.distrib.compute.iizhukov.urlshortener.db.StorageException;

public final class UserDao implements Dao<String> {
    private static final UserDao INSTANCE = new UserDao();
    private final Map<String, String> users;
    private final FileStorage storage;

    private UserDao() {
        try {
            storage = new FileStorage(new File("/tmp/iizhukov-urlshortener/users.db"));
            users = new ConcurrentHashMap<>(storage.read());
        } catch (IOException e) {
            throw new StorageException("cant open file", e);
        }
    }

    public static UserDao create() {
        return INSTANCE;
    }

    @Override
    public String get(String key) throws IllegalArgumentException {
        var value = users.get(key);

        if (value == null) {
            throw new NoSuchElementException();
        }

        return value;
    }

    @Override
    public void upsert(String key, String value) throws IllegalArgumentException {
        try {
            users.put(key, value);
            storage.write(users);
        } catch (IOException e) {
            throw new StorageException("cant write file =(", e);
        }
    }

    @Override
    public void delete(String key) throws IllegalArgumentException {
        try {
            users.remove(key);
            storage.write(users);
        } catch (IOException e) {
            throw new StorageException("cant write file =(", e);
        }
    }

    @Override
    public void close() {
        try {
            storage.write(users);
            storage.close();
        } catch (IOException e) {
            throw new StorageException("cant close file", e);
        }
    }
}
