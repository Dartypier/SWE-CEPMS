package it.unifi.swe.cepms.client_module.main;

import it.unifi.swe.cepms.client_module.business_logic.AVController;
import it.unifi.swe.cepms.client_module.business_logic.ServerController;
import it.unifi.swe.cepms.client_module.domain_model.ScanSchedule;
import it.unifi.swe.cepms.client_module.domain_model.SchedulerScan;
import it.unifi.swe.cepms.server_module.business_logic.EndpointAuth;
import it.unifi.swe.cepms.server_module.domain_model.Endpoint;

public class Main {
    public static void main(String[] args) throws Exception {

        //Test with InitController
//        InitController.createEndpoint(1, "e1");
//        ServerController.addScanSchedule(1, "0/2 * * ? * * *");
//        ServerController.addScanSchedule(2, "0/5 * * ? * * *");
//        Thread.sleep(4000);
//        ServerController.editScanSchedule(1, "0/10 * * ? * * *");
//        Thread.sleep(15000);
//        ServerController.removeScanSchedule(1);
//        AVController.generateReport(1, "report_1", "report details");
//        AVController.generateQuarantineFile(1, "report_1", "report details", "10/01/1998");


        //Test with ServerController
//        SchedulerScan.setScheduler();
//        Endpoint.setInstance(1, "e1");
//        ServerController.addScanSchedule(1, "0/2 * * ? * * *");
//        Thread.sleep(4000);
//        ServerController.editScanSchedule(1, "0/10 * * ? * * *");
//        Thread.sleep(15000);
//        ServerController.removeScanSchedule(1);

//        Test without ServerController
//        Endpoint.setInstance(1, "e1");
//        Endpoint e1 = Endpoint.getInstance();
//        SchedulerScan.setScheduler();
//        ScanSchedule sc1 = new ScanSchedule(1, "0/10 * * ? * * *");
//        e1.getScheduler().addScanSchedule(sc1);
//        Thread.sleep(15000);
//        e1.getScheduler().editScanSchedule(1, "0/2 * * ? * * *");
//        Thread.sleep(15000);
//        e1.getScheduler().removeScanSchedule(1);

        //crea istanza DAO
        //retrieve endpoint info
        //crea endpoint
        //retrieve reports and quarantine files and SCANs (Querttz)
        //and put them in Endpoint object

        EndpointAuth e = EndpointAuth.login(1, "grjUizQAeI");

    }
}
