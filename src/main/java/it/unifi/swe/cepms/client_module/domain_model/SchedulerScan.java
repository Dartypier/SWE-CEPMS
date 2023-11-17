package it.unifi.swe.cepms.client_module.domain_model;

import it.unifi.swe.cepms.ScanType;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.io.Serializable;
import java.util.HashMap;
import java.util.TreeMap;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.TriggerBuilder.newTrigger;
//TODO: ADDED config DEBUG and constructor for testing
public class SchedulerScan {
    private Scheduler schedulerQuartz;
    private HashMap<Integer, ScanSchedule> schedules = new HashMap<>();

    //used for testing
    public static boolean DEBUG = false;

    public SchedulerScan() throws SchedulerException {
        SchedulerFactory schedulerFactory;
        if(DEBUG==false){
            //DEBUG disabled for production
            //using default properties file
            schedulerFactory = new StdSchedulerFactory();
        }
        else{
            //DEBUG enabled for testing
            //using test properties file
            schedulerFactory = new StdSchedulerFactory("quartz_test.properties");
        }
        schedulerQuartz = schedulerFactory.getScheduler();
        schedulerQuartz.start();
    }

    public SchedulerScan(HashMap<Integer, ScanSchedule> schedules) throws SchedulerException {
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        schedulerQuartz = schedulerFactory.getScheduler();
        schedulerQuartz.start();
        this.schedules = schedules;
    }

//    public static SchedulerScan getInstance() throws Exception {
//        if (instance == null)
//            throw new Exception("Shceduler not set (Check if DAO has instantiated it");
//        else
//            return instance;
//    }
//
//    public static SchedulerScan setScheduler() throws SchedulerException {
//        instance = new SchedulerScan();
//        return instance;
//    }
//
//    public static SchedulerScan setScheduler(HashMap<Integer, ScanSchedule> schedules) throws SchedulerException {
//        instance = new SchedulerScan(schedules);
//        return instance;
//    }

    //ScanSchedule scanSchedule, JobDetail job, Trigger trigger
    public void addScanSchedule(ScanSchedule scan) throws Exception {

        JobDetail job = JobBuilder.newJob(ScanSchedule.class)
                .withIdentity("job" + "_" + scan.getId()) //default group
                .build();

        Trigger trigger = newTrigger()
                .withIdentity("trigger" + "_" + scan.getId())
                .withSchedule(cronSchedule(scan.getCron()))
//                        .withMisfireHandlingInstructionIgnoreMisfires())
                .forJob(job.getKey())
                .build();

        //define jobKey and triggerKey attributes
        //to identify job and trigger from DB
        scan.setJobKey(job.getKey());
        scan.setTriggerKey(trigger.getKey());

        schedules.put(scan.getId(), scan);
        schedulerQuartz.scheduleJob(job, trigger);
    }

    public void removeScanSchedule(int id) throws SchedulerException {
        schedulerQuartz.unscheduleJob(schedules.get(id).getTriggerKey());
        schedulerQuartz.deleteJob(schedules.get(id).getJobKey());

        schedules.remove(id);
    }

    public void editScanSchedule(int id, String cron, ScanType scanType, boolean enabled) throws SchedulerException {

        ScanSchedule scan = schedules.get(id);
        TriggerKey oldTriggerKey = scan.getTriggerKey();

        // Pause the job first to prevent any concurrent execution
        schedulerQuartz.pauseJob(scan.getJobKey());

        // Update the schedule details
        scan.setCron(cron);
        scan.setScanType(scanType);

        Trigger newTrigger = newTrigger()
                .withIdentity(scan.getTriggerKey())
                .withSchedule(cronSchedule(cron)
                        .withMisfireHandlingInstructionIgnoreMisfires())
                .forJob(scan.getJobKey())
                .build();

        // Reschedule the job with the new trigger
        schedulerQuartz.rescheduleJob(oldTriggerKey, newTrigger);

        // Resume or pause the job based on the 'enabled' flag
        if (enabled) {
            schedulerQuartz.resumeJob(scan.getJobKey());
        } else {
            schedulerQuartz.pauseJob(scan.getJobKey());
        }


    }

    public ScanSchedule getScanSchedule(int id){
        return schedules.get(id);
    }

    //only for tests: to remove all jobs and triggers from memory
    public Scheduler getSchedulerQuartz(){
        return schedulerQuartz;
    }
    public boolean isSchedulesEmpty(){
        return schedules.isEmpty();
    }


}
