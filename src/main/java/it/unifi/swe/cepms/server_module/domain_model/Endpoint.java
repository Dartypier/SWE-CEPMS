package it.unifi.swe.cepms.server_module.domain_model;

import it.unifi.swe.cepms.client_module.business_logic.Facade;

public class Endpoint {
    //id cannot be modified because is a primary key
    private int id;
    private String name;
    private String OTK;

    //ADDED: new code to pass directly endpoint reference
    private Facade facade = null; //this is actually the facade

    public void setFacade(Facade facade){
        this.facade = facade;
    }

    public Facade getFacade(){
        return facade;
    }




    public Endpoint(int id, String name, String OTK) {
        this.id = id;
        this.name = name;
        this.OTK = OTK;
    }

    public int getId() {
        return id;
    }
    public String getName(){return name;}
    public String getOTK(){return OTK;}

    public void setName(String name) {
        this.name = name;
    }
}
