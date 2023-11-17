package it.unifi.swe.cepms.server_module.business_logic;

import it.unifi.swe.cepms.client_module.business_logic.ServerController;
import it.unifi.swe.cepms.server_module.dao.MapReportToQuarantineFilesDao;
import it.unifi.swe.cepms.server_module.dao.QuarantineFileDao;
import it.unifi.swe.cepms.server_module.domain_model.QuarantineFile;

import java.util.HashMap;

public class QuarantineFilePageController {
    private HashMap<Integer, QuarantineFile> quarantineFiles;
    private QuarantineFileDao quarantineFileDao;
    private MapReportToQuarantineFilesDao mapReportToQuarantineFilesDao;
    private EndpointPageController endpointPageController;
    private ReportPageController reportPageController;

    public QuarantineFilePageController(EndpointPageController endpointPageController, ReportPageController reportPageController) {
        quarantineFileDao = new QuarantineFileDao();
        quarantineFiles = quarantineFileDao.getAll();
        mapReportToQuarantineFilesDao = new MapReportToQuarantineFilesDao();
        this.endpointPageController = endpointPageController;
        this.reportPageController = reportPageController;
    }

    public QuarantineFile getQuarantineFile(int id) {
        return quarantineFiles.get(id);
    }

    public void addQuarantineFile(int quarantineFileId, int scanScheduleId, int endpointId, String quarantineFileName, String details, String discoveredDate) {
        QuarantineFile quarantineFile = new QuarantineFile(quarantineFileId, scanScheduleId, endpointId, quarantineFileName, details, discoveredDate);
        quarantineFiles.put(quarantineFileId, quarantineFile);
        quarantineFileDao.save(quarantineFile, null);

        reportPageController.getAllReports().get(scanScheduleId).addQuarantineFile(quarantineFile);

        mapReportToQuarantineFilesDao.save(scanScheduleId, quarantineFileId);
    }

    public void removeQuarantineFile(int quarantineFileId) throws Exception {

        int endpointId = quarantineFiles.get(quarantineFileId).getEndpointId();
        endpointPageController.getEndpoint(endpointId).getFacade().serverController.removeQuarantineFile(quarantineFileId);

        int reportId = quarantineFiles.get(quarantineFileId).getScanScheduleId();
        reportPageController.getAllReports().get(reportId).removeQuarantineFile(quarantineFileId);

        quarantineFileDao.delete(quarantineFiles.get(quarantineFileId));
        quarantineFiles.remove(quarantineFileId);

        //        ServerController.removeQuarantineFile(quarantineFileId);
    }

    public void ignoreQuarantineFile(int quarantineFileId) throws Exception {
        int endpointId = quarantineFiles.get(quarantineFileId).getEndpointId();
        endpointPageController.getEndpoint(endpointId).getFacade().serverController.ignoreQuarantineFile(quarantineFileId);

        int reportId = quarantineFiles.get(quarantineFileId).getScanScheduleId();
        reportPageController.getAllReports().get(reportId).removeQuarantineFile(quarantineFileId);

        quarantineFileDao.delete(quarantineFiles.get(quarantineFileId));
        quarantineFiles.remove(quarantineFileId);

//        ServerController.ignoreQuarantineFile(quarantineFileId);
    }
}
