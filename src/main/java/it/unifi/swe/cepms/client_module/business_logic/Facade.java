package it.unifi.swe.cepms.client_module.business_logic;

import it.unifi.swe.cepms.client_module.dao.EndpointDao;
import it.unifi.swe.cepms.client_module.dao.QuarantineFileDao;
import it.unifi.swe.cepms.client_module.dao.ScanScheduleDao;
import it.unifi.swe.cepms.client_module.domain_model.Endpoint;
import it.unifi.swe.cepms.client_module.domain_model.QuarantineFile;
import it.unifi.swe.cepms.client_module.domain_model.ScanSchedule;
import it.unifi.swe.cepms.client_module.domain_model.SchedulerScan;

import java.util.HashMap;
import java.util.Optional;

//ADDED: this is all the client module object. In fact this is a facade pattern
public class Facade {
    //business logic (PUBLIC CALLABLE)
    public ServerController serverController;
    public AVController avController;
    //domain model
    //is protected to get access for test
    Endpoint endpoint;
    private SchedulerScan schedulerScan;
    //DAO
    private EndpointDao endpointDao;
    private QuarantineFileDao quarantineFileDao;
    private ScanScheduleDao scanScheduleDao;

    //init all client_module objects
    public Facade(int endpointId, String endpointName, String DBName, String DBUser, String DBPass) throws Exception {
        endpointDao = new EndpointDao(DBName, DBUser, DBPass);
        scanScheduleDao = new ScanScheduleDao(DBName, DBUser, DBPass);
        quarantineFileDao = new QuarantineFileDao(DBName, DBUser, DBPass);

        HashMap<Integer, ScanSchedule> schedules = scanScheduleDao.getAll();
        schedulerScan = new SchedulerScan(schedules);

        Optional<Endpoint> e = endpointDao.get(String.valueOf(endpointId));
        if(e.isPresent()){
            HashMap<Integer, QuarantineFile> quarantineFiles = quarantineFileDao.getAll();
            endpoint = new Endpoint(e.get(), quarantineFiles);
            endpoint.setScheduler(schedulerScan);
        }
        else{
            endpoint = new Endpoint(endpointId, endpointName, null);
            endpointDao.save(endpoint, new String[]{String.valueOf(endpointId), endpointName});
            endpoint.setScheduler(new SchedulerScan());
        }

        //init controllers
        serverController = new ServerController(endpoint, scanScheduleDao, quarantineFileDao);
        avController = new AVController(endpoint, quarantineFileDao);
    }

    public void setOTK(String OTK) throws Exception{
        endpoint.setOTK(OTK);
        endpointDao.update(endpoint, null);
    }

    public String getOTK(){
        return endpoint.getOTK();
    }

}
