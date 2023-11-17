package it.unifi.swe.cepms.client_module.domain_model;

import it.unifi.swe.cepms.client_module.dao.EndpointDao;
import org.quartz.SchedulerException;

import java.io.Serializable;
import java.util.HashMap;

//ADDED: no more singleton

//Endpoint should be a singleton, but it isn't in this implementation due to abstraction of
//the implementation
public class Endpoint {

    private int id;
    private String name;
    private String OTK;
    private SchedulerScan scheduler;
    private HashMap<Integer, QuarantineFile> quarantineFiles;

    public Endpoint(int id, String name, String OTK) throws Exception {
        //called if there is no instance in DB
        this.id = id;
        this.name = name;
        this.OTK = OTK;
        //FIXME: there shouldn't be the scheduler (NULL)
        this.scheduler = new SchedulerScan();
        this.quarantineFiles = new HashMap<>();
    }

    public Endpoint(Endpoint e, HashMap<Integer, QuarantineFile> quarantineFiles) throws Exception {
        //called if there is an instance in DB
        this.id = e.getId();
        this.name = e.getName();
        this.OTK = e.getOTK();
        //FIXME: there shouldn't be the scheduler (NULL)
        this.scheduler = new SchedulerScan();
        this.quarantineFiles = quarantineFiles;
    }

//    public static void setInstance(int id, String name) throws Exception {
//        instance = new Endpoint(id, name, null);
//    }
//
//    public static void setInstance(Endpoint e, HashMap<Integer, QuarantineFile> quarantineFiles) throws Exception {
//        instance = new Endpoint(e, quarantineFiles);
//
//    }
//
//    public static Endpoint getInstance() throws Exception {
//        if (instance == null) {
//            throw new Exception("Endpoint has not been instantiated");
//        } else
//            return instance;
//    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    public String getOTK() {
        return OTK;
    }
    public SchedulerScan getScheduler() {
        return scheduler;
    }
    public HashMap<Integer, QuarantineFile> getQuarantineFiles() {
        return quarantineFiles;
    }

    public void setOTK(String OTK) {
        this.OTK = OTK;
    }
    public void addQuarantineFile(QuarantineFile quarantineFile) {
        quarantineFiles.put(quarantineFile.getId(), quarantineFile);
    }
    public void setScheduler(SchedulerScan scheduler){
        this.scheduler = scheduler;
    }
}
