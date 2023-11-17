package it.unifi.swe.cepms.server_module.dao;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;

public interface Dao<S, T> {

    Optional<T> get(String id);
    Map<S, T> getAll();
    void save(T t, String[] params);
    void update(T t, String[] params);
    void delete(T t);


}