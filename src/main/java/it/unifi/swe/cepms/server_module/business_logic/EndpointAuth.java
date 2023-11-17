package it.unifi.swe.cepms.server_module.business_logic;

import it.unifi.swe.cepms.server_module.dao.Dao;
import it.unifi.swe.cepms.server_module.dao.EndpointDao;
import it.unifi.swe.cepms.server_module.domain_model.Endpoint;

import java.util.Optional;

public class EndpointAuth {
    private Endpoint endpoint;
    private EndpointAuth(Integer id, String OTK) throws Exception {
        Dao<Integer, Endpoint> endpointDao = new EndpointDao();
        Optional<Endpoint> endpoint1 = endpointDao.get(String.valueOf(id));
        if(endpoint1.isPresent() && endpoint1.get().getOTK().equals(OTK)){
            endpoint = endpoint1.get();
        }
        else
            throw new Exception("Not authenticated");
    }

    public static EndpointAuth login(Integer id, String OTK) throws Exception{
        return new EndpointAuth(id, OTK);
    }
    public void logout(){endpoint = null;}
    boolean isAuthenticated() {
        return endpoint != null;
    }
}
