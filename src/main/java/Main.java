import it.unifi.swe.cepms.ScanType;
import it.unifi.swe.cepms.client_module.business_logic.AVController;
import it.unifi.swe.cepms.client_module.business_logic.Facade;
import it.unifi.swe.cepms.client_module.domain_model.Endpoint;
import it.unifi.swe.cepms.client_module.domain_model.QuarantineFile;
import it.unifi.swe.cepms.server_module.business_logic.AuthProxy;
import it.unifi.swe.cepms.server_module.business_logic.EndpointAuth;
import it.unifi.swe.cepms.server_module.business_logic.UserAuth;

import java.util.ArrayList;
import java.util.HashMap;

public class Main {
    public static void main(String[] args) throws Exception {
        //creating endpoint/FACADE
        Facade f1 = new Facade(1, "office_1", "jdbc:postgresql://localhost:5432/db_endpoint_one", "jacopo", "rentus");

        //loggin in to Server Module
        AuthProxy authProxy = AuthProxy.getInstance();
        UserAuth userAuth = UserAuth.login("admin@example.com", "password");

        //add user
//        authProxy.newUser(userAuth, "jacopo.zecchi@gmail.com", "Jacopo", "Zecchi", "pass");

        //edit user
//        authProxy.editUser(userAuth, "jacopo.zecchi@gmail.com", null, "Hello", "World", "ciao");

        //remove user
//        authProxy.removeUser(userAuth, "jacopo.zecchi@gmail.com");

        //associating endpoint to Server Module
        String OTK = authProxy.generateOTK(userAuth);
        f1.setOTK(OTK);
        authProxy.associateEndpoint(userAuth, OTK, 1, "office_1");

        //setting Facade auth (The Endpoint must be already associated with Server Module)
        //mandatory to execute actions on client modules, because with it
        //the server modules has their references
        EndpointAuth endpointAuth = EndpointAuth.login(1, f1.getOTK());
        authProxy.setEndpoint(endpointAuth, 1, f1);

        //creating schedule
        authProxy.addScanSchedule(userAuth, "Schedule every 30 seconds", "0/30 * * ? * * *", ScanType.FullScan);

        //associating endpoint/FACADE to scan
        ArrayList<Integer> endpointsId = new ArrayList<>();
        endpointsId.add(1);
        authProxy.associateEndpointsToScanSchedule(userAuth, endpointsId, 1);

//        authProxy.printAllScanSchedules(userAuth);

        //edit scanSchedule
//        authProxy.editScanSchedule(userAuth, 1,"0/10 * * ? * * *", "Schedule every 10 seconds", ScanType.FastScan, true);

        //remove scan schedule
//        authProxy.removeScanSchedule(userAuth, 1);

        //add quarantine files
//        HashMap<Integer, QuarantineFile> hs = new HashMap<>();
//        hs.put(1, new QuarantineFile(1, 1, 1, "test", "test", "test"));
//        hs.put(2, new QuarantineFile(2, 1, 1, "test", "test", "test"));
//        f1.avController.generateQuarantineFile(hs);

        //ignoring/removing quarantine files
//        authProxy.ignoreQuarantineFile(userAuth, 1);
//        authProxy.removeQuarantineFile(userAuth, 2);

    }
}
