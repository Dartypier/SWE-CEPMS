package it.unifi.swe.cepms.server_module.domain_model;

public class QuarantineFile {
    private int id;
    private int scanScheduleId;
    private int endpointId;
    private String name;
    private String details;
    private String discoveredDate;

    public QuarantineFile(int id, int scanScheduleId, int endpointId, String name, String details, String discoveredDate) {
        this.id = id;
        this.scanScheduleId = scanScheduleId;
        this.endpointId = endpointId;
        this.name = name;
        this.details = details;
        this.discoveredDate = discoveredDate;
    }

    public int getId() {
        return id;
    }
    public int getScanScheduleId(){return scanScheduleId;}

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
