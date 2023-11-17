package it.unifi.swe.cepms.client_module.domain_model;

public class QuarantineFile {
    private  int quarantineFileId;
    private int scanScheduleId;
    private int endpointId;
    private  String name;
    private  String details;
    private  String discoveredDate;

    public QuarantineFile(int quarantineFileId, int scanScheduleId, int endpointId, String name, String details, String discoveredDate){
        this.quarantineFileId = quarantineFileId;
        this.scanScheduleId = scanScheduleId;
        this.endpointId = endpointId;
        this.name = name;
        this.details = details;
        this.discoveredDate = discoveredDate;
    }

    public int getId(){
        return quarantineFileId;
    }

    public int getScanScheduleId(){
        return scanScheduleId;
    }

    public String getName() {
        return name;
    }

    public String getDetails() {
        return details;
    }

    public String getDiscoveredDate() {
        return discoveredDate;
    }

    public int getEndpointId() {
        return endpointId;
    }
}
