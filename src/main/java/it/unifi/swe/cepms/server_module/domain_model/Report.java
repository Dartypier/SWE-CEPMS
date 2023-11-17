package it.unifi.swe.cepms.server_module.domain_model;

import java.util.HashMap;

public class Report {
    private int id;
    private String name;
//    private ScanSchedule scanSchedule;
    private Integer scanScheduleId;
    private HashMap<Integer, QuarantineFile> quarantineFiles;

//    public Report(int id, String name, ScanSchedule scanSchedule){
//        this.id = id;
//        this.name = name;
//        this.scanSchedule = scanSchedule;
//        this.quarantineFiles = new HashMap<>();
//    }

    //used by Dao
    public Report(int id, String name, int scanScheduleId){
        this.id = id;
        this.name = name;
        this.scanScheduleId = scanScheduleId;
        this.quarantineFiles = new HashMap<>();
    }

    public void addQuarantineFile(QuarantineFile quarantineFile){
        this.quarantineFiles.put(quarantineFile.getId(), quarantineFile);
    }

    public void removeQuarantineFile(int quarantineFileId){
        quarantineFiles.remove(quarantineFileId);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

//    public ScanSchedule getScanSchedule() {
//        return scanSchedule;
//    }
    public Integer getScanScheduleId(){return scanScheduleId;}

    public HashMap<Integer, QuarantineFile> getQuarantineFiles() {
        return quarantineFiles;
    }

    public void setScanScheduleId(Integer scanScheduleId){
        this.scanScheduleId = scanScheduleId;
        //can be null if the scanSchedule has been removed
    }
}
