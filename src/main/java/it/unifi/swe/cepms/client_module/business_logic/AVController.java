package it.unifi.swe.cepms.client_module.business_logic;

import it.unifi.swe.cepms.client_module.dao.EndpointDao;
import it.unifi.swe.cepms.client_module.dao.QuarantineFileDao;
import it.unifi.swe.cepms.client_module.domain_model.Endpoint;
import it.unifi.swe.cepms.client_module.domain_model.QuarantineFile;
import it.unifi.swe.cepms.server_module.business_logic.AuthProxy;
import it.unifi.swe.cepms.server_module.business_logic.EndpointAuth;

import java.util.HashMap;

//actions that the Local AV does to the Endpoint
public class AVController {

    Endpoint e;
    QuarantineFileDao qfd;


//    public static void generateReport(int id, String name, String details, int scanScheduleId) throws Exception {
//        //Local AV create a Report object and
//        //add it to the endpoint Reports field
//        Report r = new Report(id, name, details);
//        //add report to Endpoint
//        Endpoint e = Endpoint.getInstance();
//        e.addReport(r);
//        //save report to DB
//        ReportDao rd = new ReportDao();
//        rd.save(r, null);
//        EndpointAuth endpointAuth = EndpointAuth.login(e.getId(), e.getOTK());
//        AuthProxy.getInstance().addReport(endpointAuth, id, name, scanScheduleId);
//        endpointAuth.logout();
//    }

    public AVController(Endpoint e, QuarantineFileDao qfd) {
        this.e = e;
        this.qfd = qfd;
    }

    public void generateQuarantineFile(HashMap<Integer, QuarantineFile> quarantineFiles) throws Exception {
        //Local AV generate a QuarantineFile object
        //and add it to the endpoint QuarantineFiles field
        EndpointAuth endpointAuth = EndpointAuth.login(e.getId(), e.getOTK());

        for (Integer id : quarantineFiles.keySet()) {
            QuarantineFile current = quarantineFiles.get(id);
            e.addQuarantineFile(current);
            qfd.save(current, null);
            AuthProxy.getInstance().addQuarantineFile(endpointAuth, current.getId(), current.getScanScheduleId(), current.getEndpointId(), current.getName(), current.getDetails(), current.getDiscoveredDate());
        }

        endpointAuth.logout();
    }
}
