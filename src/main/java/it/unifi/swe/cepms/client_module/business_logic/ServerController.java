package it.unifi.swe.cepms.client_module.business_logic;

import it.unifi.swe.cepms.ScanType;
import it.unifi.swe.cepms.client_module.dao.QuarantineFileDao;
import it.unifi.swe.cepms.client_module.dao.ScanScheduleDao;
import it.unifi.swe.cepms.client_module.domain_model.Endpoint;
import it.unifi.swe.cepms.client_module.domain_model.QuarantineFile;
import it.unifi.swe.cepms.client_module.domain_model.ScanSchedule;

//actions that the Server Module does to the Endpoint
public class ServerController {

    Endpoint e;
    ScanScheduleDao ssd;
    QuarantineFileDao qfd;

    public ServerController(Endpoint e, ScanScheduleDao ssd, QuarantineFileDao qfd){
        this.e = e;
        this.ssd = ssd;
        this.qfd = qfd;
    }

    //The Object actually passed is an id and the cron of a ScanSchedule in ServerModule
    public void addScanSchedule(int id, String cron, ScanType scanType) throws Exception {
        ScanSchedule scan = new ScanSchedule(id, cron, scanType);
        //schedules the scan
        e.getScheduler().addScanSchedule(scan);
        //save the scan to DB
        ssd.save(scan, null);
    }

    public void removeScanSchedule(int id) throws Exception {
        //delete ScanSchedule from DB
        ssd.delete(e.getScheduler().getScanSchedule(id));
        //remove ScanSchedule from Scheduler
        e.getScheduler().removeScanSchedule(id);
    }

    public void editScanSchedule(int id, String cron, ScanType scanType, Boolean enabled) throws Exception {
        //update ScanSchedule to DB
        //update ScanSchedule to Scheduler and object
        e.getScheduler().editScanSchedule(id, cron, scanType, enabled);
        ssd.update(e.getScheduler().getScanSchedule(id), null);
    }

    public void removeQuarantineFile(int quarantineFileId) throws Exception {
        QuarantineFile quarantineFile = e.getQuarantineFiles().get(quarantineFileId);
        qfd.delete(quarantineFile);
        e.getQuarantineFiles().remove(quarantineFileId);
        System.out.printf("\nQuarantine file removed: %d", quarantineFile.getId());
    }

    public void ignoreQuarantineFile(int quarantineFileId) throws Exception {
        QuarantineFile quarantineFile = e.getQuarantineFiles().get(quarantineFileId);
        qfd.delete(quarantineFile);
        e.getQuarantineFiles().remove(quarantineFileId);
        System.out.printf("Quarantine file removed: %d", quarantineFile.getId());
    }
}
