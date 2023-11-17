package it.unifi.swe.cepms.server_module.business_logic;

import it.unifi.swe.cepms.client_module.business_logic.Facade;
import it.unifi.swe.cepms.server_module.dao.EndpointDao;
import it.unifi.swe.cepms.server_module.domain_model.Endpoint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class EndpointPageController {

//    public static EndpointPageController instance = null;
    private HashMap<Integer, Endpoint> endpoints;
    private EndpointDao endpointDao;
    private ArrayList<String> generatedOTK;

    //ADDED: setEndpoint
    //this is used to set and endpoint so the Server Module knows the reference to pass actions
    //to the actual endpoint reference
    public void setEndpoint(int endpointId, Facade endpoint){
        endpoints.get(endpointId).setFacade(endpoint);
    }


    public EndpointPageController() {
        endpointDao = new EndpointDao();
        endpoints = endpointDao.getAll();
        generatedOTK = new ArrayList<>();
    }

//    public static EndpointPageController getInstance(){
//        if(instance == null){
//            instance = new EndpointPageController();
//        }
//        return instance;
//    }

    public void editEndpointName(Integer endpointId, String name){
        endpoints.get(endpointId).setName(name);
        endpointDao.update(endpoints.get(endpointId), new String[]{name});
    }

    public Endpoint getEndpoint(int endpointId) {
        return endpoints.get(endpointId);
    }

    //TODO: implement should mantain a list of OTK that enables authenticatio for otk
    //maybe not here, but for interfaces that interact with Endpoints
    public boolean associateEndpoint(String OTK, int id, String name) {
        //TODO: the OTK shouldn't be accepted and be removed if it is not used over 5m

        if (generatedOTK.contains(OTK)) {
            Endpoint endpoint = new Endpoint(id, name, OTK);
            endpoints.put(endpoint.getId(), endpoint);
            endpointDao.save(endpoint, null);
            generatedOTK.remove(OTK);
            return true;
        } else {
            return false;
        }
    }

    public void deassociateEndpoint(int id) {
        endpointDao.delete(endpoints.get(id));
        endpoints.remove(id);
    }

    public String generateOTK() {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();

//        String timeStamp = new SimpleDateFormat("mm.HH.dd.MM.yyyy").format(new java.util.Date());
        String generatedString = random.ints(leftLimit, rightLimit + 1).filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97)).limit(targetStringLength).collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();
//        generatedString += "#"+timeStamp;

        generatedOTK.add(generatedString);


        return generatedString;
    }

}
