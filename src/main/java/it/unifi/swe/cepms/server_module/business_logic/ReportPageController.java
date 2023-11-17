package it.unifi.swe.cepms.server_module.business_logic;

import it.unifi.swe.cepms.server_module.dao.ReportDao;
import it.unifi.swe.cepms.server_module.domain_model.Report;

import java.util.HashMap;

public class ReportPageController {
    ReportDao reportDao;
    HashMap<Integer, Report> reports;

    public ReportPageController(){
        this.reportDao = new ReportDao();
        this.reports = reportDao.getAll();
    }

    public Report getReport(int id){
        return reports.get(id);
    }

    public HashMap<Integer, Report> getAllReports(){
        return reports;
    }

    public void printAllReports(){
        for(Integer reportId : reports.keySet()){
            Report report = reports.get(reportId);
            System.out.println(report.getId());
        }
    }

    public void addReport(int reportId, String reportName, int scanScheduleId){
        Report report = new Report(reportId, reportName, scanScheduleId);
        reports.put(report.getId(), report);
        reportDao.save(report, null);
    }

    public void setScancheduleIdNULL(int reportId){
        getReport(reportId).setScanScheduleId(null);
    }

    public Integer generateReportId() {
        int smallestFreeID = 1; // Start with the smallest possible ID

        for (Integer id : reports.keySet()) {
            if (id == smallestFreeID) {
                smallestFreeID++; // Increment if the ID exists in the TreeMap
            } else if (id > smallestFreeID) {
                break; // Exit the loop if we find a gap in the sequence
            }
        }

        return smallestFreeID;
    }


}
