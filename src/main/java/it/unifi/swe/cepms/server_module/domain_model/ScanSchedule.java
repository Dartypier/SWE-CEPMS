package it.unifi.swe.cepms.server_module.domain_model;

import it.unifi.swe.cepms.ScanType;

import java.util.ArrayList;
import java.util.HashMap;

public class ScanSchedule {

    private int id;
    private String name;
    private String cron;
    private ScanType scanType;
    protected HashMap<Integer, Endpoint> associatedEndpoints;
    protected boolean enabled;

//    public ScanSchedule(int id, String cron, String name) {
//        this.associatedEndpoints = new HashMap<>();
//        this.id = id;
//        this.cron = cron;
//        this.name = name;
//        this.scanType = scanType;
//        this.scanRecurrence = scanRecurrence;
//    }

    //This constructor is used only by ScanScheduleDao
    public ScanSchedule(int id, String name, String cron, ScanType scanType, boolean enabled) {
//        System.out.printf("%d, %s, %s, %s, %d",id, name, cron, scanType.toString(), enabled );
        this.id = id;
        this.name = name;
        this.cron = cron;
        this.scanType = scanType;
        this.enabled = enabled;
        this.associatedEndpoints = new HashMap<>();
    }

    //return a list of ids of all associated endpoints
    public ArrayList<Integer> getAllAssociatedEndpoints() {
        ArrayList<Integer> endpointsId = new ArrayList<>();
        for(Integer endpointId : associatedEndpoints.keySet()){
            endpointsId.add(endpointId);
        }
        return endpointsId;
    }

//    public Endpoint getAssociatedEndpoint(Endpoint endpoint) {
//        return associatedEndpoints.get(endpoint.getId());
//    }

    public Endpoint getAssociatedEndpoint(Integer id) {
        return associatedEndpoints.get(id);
    }

    public void addAssociatedEndpoint(Endpoint endpoint) {
        associatedEndpoints.put(endpoint.getId(), endpoint);
    }

    public void removeAssociatedEndpoint(Endpoint endpoint) {
        associatedEndpoints.remove(endpoint.getId());
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCron(){
        return cron;
    }
    public ScanType getScanType(){
        return scanType;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setScanType(ScanType scanType){
        this.scanType = scanType;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void editCron(String cron) {
        //The validation happens in Business logic
        this.cron = cron;
    }
//    public void setScanType(ScanType scanType){this.scanType = scanType;}
}