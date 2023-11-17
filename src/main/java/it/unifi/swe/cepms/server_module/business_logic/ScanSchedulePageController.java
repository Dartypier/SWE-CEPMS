package it.unifi.swe.cepms.server_module.business_logic;

import it.unifi.swe.cepms.client_module.business_logic.ServerController;
import it.unifi.swe.cepms.server_module.dao.MapScanScheduleToEndpointDao;
import it.unifi.swe.cepms.server_module.dao.ScanScheduleDao;
import it.unifi.swe.cepms.server_module.domain_model.Report;
import it.unifi.swe.cepms.server_module.domain_model.ScanSchedule;
import it.unifi.swe.cepms.ScanType;

import java.util.ArrayList;
import java.util.TreeMap;

public class ScanSchedulePageController {
    ScanScheduleDao scanScheduleDao;
    MapScanScheduleToEndpointDao mapScanScheduleToEndpointDao;
    TreeMap<Integer, ScanSchedule> scanSchedules;
    EndpointPageController endpointPageController;
    ReportPageController reportPageController;

    public ScanSchedulePageController(EndpointPageController endpointPageController, ReportPageController reportPageController) {
        this.scanScheduleDao = new ScanScheduleDao();
        this.mapScanScheduleToEndpointDao = new MapScanScheduleToEndpointDao();
        this.scanSchedules = scanScheduleDao.getAll();
        this.endpointPageController = endpointPageController;
        this.reportPageController = reportPageController;
    }

    public TreeMap<Integer, ScanSchedule> getAllScanSchedules() {
        return scanSchedules;
    }

    public void printAllScanSchedules() {
        for (Integer scanId : scanSchedules.keySet()) {
            ScanSchedule scanSchedule = scanSchedules.get(scanId);
            System.out.println(scanSchedule.getId() + ": " + scanSchedule.getName());
        }
    }

    public void addScanSchedule(String name, String cron, ScanType scanType) throws Exception {
        //Create associated report to ScanSchedule
        //NOTE: the scanScheduleId is the same as the Report id, because Reports are never deleted from DB

        //endpointId here would serve as indeitifier to send to the specificied endpoint the scan
        ScanSchedule scanSchedule = new ScanSchedule(reportPageController.generateReportId(), name, cron, scanType, true);
        scanSchedules.put(reportPageController.generateReportId(), scanSchedule);
        scanScheduleDao.save(scanSchedule, null);
        //add scan to new associated Report
        reportPageController.addReport(reportPageController.generateReportId(), "report" + "_scan_" + scanSchedule.getId(), scanSchedule.getId());
    }

    public void associateEndpointsToScanSchedule(ArrayList<Integer> endpointsId, int scanScheduleId) throws Exception {
        ScanSchedule scanSchedule = scanSchedules.get(scanScheduleId);
        for (int endpointId : endpointsId) {
            if (scanSchedule.getAssociatedEndpoint(endpointId) == null) {
                scanSchedule.addAssociatedEndpoint(endpointPageController.getEndpoint(endpointId));
                mapScanScheduleToEndpointDao.save(scanSchedule.getId(), endpointId);

                //call method on client_module endpoint
                if(endpointPageController.getEndpoint(endpointId).getFacade()!=null){
                    endpointPageController.getEndpoint(endpointId).getFacade().serverController.addScanSchedule(scanSchedule.getId(), scanSchedule.getCron(), scanSchedule.getScanType());
                }
            }
        }
        //send scan schedule to endpoint
//        ServerController.addScanSchedule(scanSchedule.getId(), scanSchedule.getCron(), scanSchedule.getScanType());
    }

    public void deassociateEndpoints(ArrayList<Integer> endpointsId, int scanScheduleId) throws Exception {
        ScanSchedule scanSchedule = scanSchedules.get(scanScheduleId);
        for (int endpointId : endpointsId) {
            if (scanSchedule.getAssociatedEndpoint(endpointId) != null) {
                scanSchedule.removeAssociatedEndpoint(endpointPageController.getEndpoint(endpointId));
                mapScanScheduleToEndpointDao.delete(endpointId, scanScheduleId);

                //call method on client_module endpoint
                if(endpointPageController.getEndpoint(endpointId).getFacade()!=null){
                    endpointPageController.getEndpoint(endpointId).getFacade().serverController.removeScanSchedule(scanSchedule.getId());
                }
            }
        }
        //remove scan schedule from endpoint
        //TODO: modifica perche' qui lo invia all'unico endpoint
//        ServerController.removeScanSchedule(scanSchedule.getId());
    }

    public void removeScanSchedule(int scanScheduleId) throws Exception {
        //remove from associated endpoint
        ScanSchedule scanSchedule = scanSchedules.get(scanScheduleId);
        deassociateEndpoints(scanSchedule.getAllAssociatedEndpoints(), scanScheduleId);

        //remove ScanSchedule and from associated endpoints
        scanScheduleDao.delete(scanSchedules.get(scanScheduleId));
        scanSchedules.remove(scanScheduleId);

        //because the associated scanSchedule to Report is deleted, the scanSchedule reference is set to null
        reportPageController.setScancheduleIdNULL(scanScheduleId);
    }

    public void editScanSchedule(int scanScheduleId, String cron, String name, ScanType scanType, Boolean enabled) throws Exception {
        ScanSchedule scanSchedule = scanSchedules.get(scanScheduleId);
        scanSchedule.editCron(cron);
        scanSchedule.setName(name);
        scanSchedule.setScanType(scanType);
        scanSchedule.setEnabled(enabled);
        scanScheduleDao.update(scanSchedule, new String[]{name, cron, scanType.toString(), enabled.toString()});

        ArrayList<Integer> associatedEndpoints = scanSchedules.get(scanScheduleId).getAllAssociatedEndpoints();
        for(int endpointId : associatedEndpoints){
            endpointPageController.getEndpoint(endpointId).getFacade().serverController.editScanSchedule(scanScheduleId, cron, scanType, enabled);
        }

        //TODO: modifica perhce' dovrebbe inviare update a tutti gli endpoint associati
//        ServerController.editScanSchedule(scanScheduleId, cron, scanType, enabled);

    }
}
