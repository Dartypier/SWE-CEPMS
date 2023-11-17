package it.unifi.swe.cepms.server_module.business_logic;

//authentication proxy
//singleton

import it.unifi.swe.cepms.ScanType;
import it.unifi.swe.cepms.client_module.business_logic.Facade;
import it.unifi.swe.cepms.client_module.domain_model.Endpoint;
import it.unifi.swe.cepms.server_module.dao.MapReportToQuarantineFilesDao;
import it.unifi.swe.cepms.server_module.dao.MapScanScheduleToEndpointDao;
import it.unifi.swe.cepms.server_module.domain_model.Report;
import it.unifi.swe.cepms.server_module.domain_model.ScanSchedule;
import it.unifi.swe.cepms.server_module.domain_model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

public class AuthProxy {

    private static AuthProxy instance = null;

    UsersPageController usersPageController;
    QuarantineFilePageController quarantineFilePageController;
    ReportPageController reportPageController;
    EndpointPageController endpointPageController;
    ScanSchedulePageController scanSchedulePageController;

    //Mappers
    MapScanScheduleToEndpointDao mapScanScheduleToEndpointDao;
    MapReportToQuarantineFilesDao mapReportToQuarantineFilesDao;

    //ADDED: set Endpoint
    public void setEndpoint(EndpointAuth endpointAuth, int endpointId, Facade endpoint) throws Exception {
        if(checkAccess(endpointAuth)){
            endpointPageController.setEndpoint(endpointId, endpoint);
        }
        else throw new Exception("not permitted");
    }

    public static AuthProxy getInstance(){
        if(instance == null){
            instance = new AuthProxy();
        }
        return instance;
    }

    //used ONLY for tests to enable their independende
    //thus package-private
    static AuthProxy testConstructor(){
        AuthProxy authProxy = new AuthProxy();
        instance = authProxy;
        return authProxy;
    }

    private AuthProxy() {
        //initializing all controllers with data
        this.usersPageController = new UsersPageController();
        this.endpointPageController = new EndpointPageController();
        this.mapScanScheduleToEndpointDao = new MapScanScheduleToEndpointDao();
        this.mapReportToQuarantineFilesDao = new MapReportToQuarantineFilesDao();
        this.reportPageController = new ReportPageController();
        this.scanSchedulePageController = new ScanSchedulePageController(endpointPageController, reportPageController);
        this.quarantineFilePageController = new QuarantineFilePageController(endpointPageController, reportPageController);

        associateEndpointstoScanSchedule();
        associateQuarantineFilesToReport();


        //create default user for testing purpose
        if(!usersPageController.getAllUsers().containsKey("admin@example.com"))
            usersPageController.newUser("admin@example.com", "admin", "admin", "password");
    }

    //method for checking access for interface methods
    private boolean checkAccess(UserAuth auth) {
        return auth.isAuthenticated();
    }
    private boolean checkAccess(EndpointAuth auth) {return auth.isAuthenticated();}

    //helper method to set associated endpoints to respective scan schedules
    private void associateEndpointstoScanSchedule() {
        TreeMap<Integer, ScanSchedule> scanSchedules = scanSchedulePageController.getAllScanSchedules();

        for (Integer scanId : scanSchedules.keySet()) {
            ArrayList<Integer> endpoints = mapScanScheduleToEndpointDao.getAll(scanId);
            for (Integer endpointId : endpoints) {
                scanSchedules.get(scanId).addAssociatedEndpoint(endpointPageController.getEndpoint(endpointId));
            }
        }
    }

    //helper method to set associated QuarantineFile to respective reports
    private void associateQuarantineFilesToReport(){
        HashMap<Integer, Report> reports = reportPageController.getAllReports();

        for(Integer reportId : reports.keySet()){
            ArrayList<Integer> quarantineFiles = mapReportToQuarantineFilesDao.getAll(reportId);
            for(Integer quarantineFileId : quarantineFiles){
                reports.get(reportId).addQuarantineFile(quarantineFilePageController.getQuarantineFile(quarantineFileId));
            }
        }
    }

    //Interface Proxy Methods for User

    //EndpointPageController
    public String generateOTK(UserAuth userAuth) throws Exception {
        if(checkAccess(userAuth))
            return endpointPageController.generateOTK();
        else
            throw new Exception("not permitted");
    }
    public boolean associateEndpoint(UserAuth userAuth,String OTK, int id, String name) throws Exception {
        if(checkAccess(userAuth)){
            return endpointPageController.associateEndpoint(OTK, id, name);
        }
        else throw new Exception("not permitted");

    }
    public void deassociateEndpoint(UserAuth userAuth, int id) throws Exception {
        if(checkAccess(userAuth)){
            endpointPageController.deassociateEndpoint(id);
        }
        else throw new Exception("not permitted");
    }

    //UserPageController
    public void newUser(UserAuth userAuth, String email, String name, String surname, String password) throws Exception {
        if(checkAccess(userAuth)){
            usersPageController.newUser(email, name, surname, password);
        }
        else throw new Exception("not permitted");
    }

    public void removeUser(UserAuth userAuth, String email) throws Exception {
        if(checkAccess(userAuth)){
            usersPageController.removeUser(email);
        }
        else throw new Exception("not permitted");
    }

    public void editUser(UserAuth userAuth, String emailId, String newEmail, String name, String surname, String password) throws Exception {
        if(checkAccess(userAuth)){
            usersPageController.editUser(emailId, newEmail, name, surname, password);
        }
        else throw new Exception("not permitted");
    }

    public HashMap<String, User> getAllUsers(UserAuth userAuth) throws Exception {
        if(checkAccess(userAuth)){
            return usersPageController.getAllUsers();
        }
        else throw new Exception("not permitted");
    }

    //ScanSchedulePageController
    public TreeMap<Integer, ScanSchedule> getAllScanSchedules(UserAuth userAuth) throws Exception {
        if(checkAccess(userAuth)){
            return scanSchedulePageController.getAllScanSchedules();
        }
        else throw new Exception("not permitted");
    }

    public void printAllScanSchedules(UserAuth userAuth){
        scanSchedulePageController.printAllScanSchedules();
    }

    public void addScanSchedule(UserAuth userAuth, String name, String cron, ScanType scanType) throws Exception {
        if(checkAccess(userAuth)){
            scanSchedulePageController.addScanSchedule(name, cron, scanType);
        }
        else throw new Exception("not permitted");
    }

    public void associateEndpointsToScanSchedule(UserAuth userAuth, ArrayList<Integer> endpointsId, int scanScheduleId) throws Exception {
        if(checkAccess(userAuth)){
            scanSchedulePageController.associateEndpointsToScanSchedule(endpointsId, scanScheduleId);
        }
        else throw new Exception("not permitted");
    }

    public void deassociateEndpointsToScanSchedule(UserAuth userAuth, ArrayList<Integer> endpointsId, int scanScheduleId) throws Exception{
        if(checkAccess(userAuth)){
            scanSchedulePageController.deassociateEndpoints(endpointsId, scanScheduleId);
        }
        else throw new Exception("not permitted");
    }

    public void removeScanSchedule(UserAuth userAuth, int scanScheduleId) throws Exception {
        if(checkAccess(userAuth)){
            scanSchedulePageController.removeScanSchedule(scanScheduleId);
        }
        else throw new Exception("not permitted");
    }

    public void editScanSchedule(UserAuth userAuth, int scanScheduleId,String cron, String name, ScanType scanType, Boolean enabled) throws Exception {
        if(checkAccess(userAuth)){
            scanSchedulePageController.editScanSchedule(scanScheduleId, cron, name, scanType, enabled);
        }
        else throw new Exception("not permitted");
    }

    //Interface Proxy Methods for Endpoint

    //QuarantineFilePageController
    public void addQuarantineFile(EndpointAuth endpointAuth, int quarantineFileId, int scanScheduleId, int endpointId, String quarantineFileName, String details, String discoveredDate) throws Exception {
        if(checkAccess(endpointAuth)){
            quarantineFilePageController.addQuarantineFile(quarantineFileId, scanScheduleId, endpointId, quarantineFileName, details, discoveredDate);
        }
        else throw new Exception("not permitted");
    }

    public void removeQuarantineFile(UserAuth userAuth, int quarantineFileId) throws Exception {
        if(checkAccess(userAuth)){
            quarantineFilePageController.removeQuarantineFile(quarantineFileId);
        }
        else throw new Exception("not permitted");
    }

    public void ignoreQuarantineFile(UserAuth userAuth, int quarantineFileId) throws Exception {
        if(checkAccess(userAuth)){
            quarantineFilePageController.ignoreQuarantineFile(quarantineFileId);
        }
        else throw new Exception("not permitted");
    }

}
