package it.unifi.swe.cepms.client_module.domain_model;

import it.unifi.swe.cepms.ScanType;
import org.quartz.*;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

//This class is responsible to execute Jobs
//setted by the server module

public class ScanSchedule implements Job {

    //key for quartz scheduler for Jobs and Triggers
    private JobKey jobKey;
    private TriggerKey triggerKey;
    private ScanType scanType;
    private String cron;
    private int id;

    //used by Quartz
    public ScanSchedule(){}
    public ScanSchedule(int id, String cron, ScanType scanType){
        this.cron = cron;
        //the id is passed by the server module
        this.id = id;
        this.scanType = scanType;
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println("Triggered Scan");
    }

    public TriggerKey getTriggerKey() {
        return triggerKey;
    }

    public void setTriggerKey(TriggerKey triggerKey){
        this.triggerKey = triggerKey;
    }

    public JobKey getJobKey() {
        return jobKey;
    }

    public void setJobKey(JobKey jobKey){
        this.jobKey = jobKey;
    }

    public Integer getId() {
        return id;
    }

    public String getCron(){
        return cron;
    }
    public ScanType getScanType(){return scanType;}

    public void setScanType(ScanType scanType) {
        this.scanType = scanType;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }
}
