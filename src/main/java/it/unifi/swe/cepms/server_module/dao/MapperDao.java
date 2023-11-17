package it.unifi.swe.cepms.server_module.dao;

import java.util.ArrayList;

public interface MapperDao<T> {

    //methods specific for Mappers
    public ArrayList<T> getAll(T id);
    public void save(T id, T id1);
    public void delete(T t, T t1);

}
