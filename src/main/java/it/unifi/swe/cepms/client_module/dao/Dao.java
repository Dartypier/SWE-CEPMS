package it.unifi.swe.cepms.client_module.dao;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;

public interface Dao<T> {
    Optional<T> get(String id);
    HashMap<Integer, T> getAll();
    void save(T t, String[] params);
    void update(T t, String[] params);
    void delete(T t);
}