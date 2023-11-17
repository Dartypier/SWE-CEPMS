package it.unifi.swe.cepms.server_module.business_logic;

import it.unifi.swe.cepms.ScanType;
import it.unifi.swe.cepms.client_module.business_logic.Facade;
import it.unifi.swe.cepms.client_module.domain_model.QuarantineFile;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class QuarantineFilePageControllerIntegrationTest {

    Facade facade;
    AuthProxy authProxy;
    EndpointAuth endpointAuth;
    UserAuth userAuth;

    @BeforeEach
    void setUp() throws Exception {
        //clearing table
        String deleteAllUsers = "delete from users;";
        String deleteAllEndpoints = "delete from endpoint;";
        String deleteAllScanSchedules = "delete from scan_schedule;";
        String deleteAllQuarantineFiles = "delete from quarantine_file;";
        String deleteAllMapScanScheduleToEndpoint = "delete from map_scan_schedule_to_endpoint;";
        String deleteAllReport = "delete from report;";
        String deleteAllMapReportToQuarantineFiles = "delete from map_report_to_quarantine_files;";

        String deleteAllEndpointsClient = "delete from endpoint;";
        String deleteAllScanScheduleClient = "delete from scan_schedule;";
        String deleteAllQuarantineClient = "delete from quarantine;";
        String deleteAllQrtzTriggers = "delete from qrtz_triggers CASCADE;";
        String deleteAllQrtzJobDetails = "delete from qrtz_job_details CASCADE;";
        String deleteAllQrtzFiredTriggers = "delete from qrtz_fired_triggers CASCADE;";
        String deleteAllQrtzCronTriggers = "delete from qrtz_cron_triggers CASCADE;";

        Connection con = null;
        Statement stmt = null;

        try {
            con = getConnection("jdbc:postgresql://localhost:5432/server_module", "jacopo", "rentus");
            stmt = con.createStatement();
            stmt.execute(deleteAllUsers);
            stmt.execute(deleteAllEndpoints);
            stmt.execute(deleteAllScanSchedules);
            stmt.execute(deleteAllQuarantineFiles);
            stmt.execute(deleteAllMapScanScheduleToEndpoint);
            stmt.execute(deleteAllReport);
            stmt.execute(deleteAllMapReportToQuarantineFiles);

            con = getConnection("jdbc:postgresql://localhost:5432/db_endpoint_one", "jacopo", "rentus");
            stmt = con.createStatement();
            stmt.execute(deleteAllEndpointsClient);
            stmt.execute(deleteAllScanScheduleClient);
            stmt.execute(deleteAllQuarantineClient);
            stmt.execute(deleteAllQrtzCronTriggers);
            stmt.execute(deleteAllQrtzTriggers);
            stmt.execute(deleteAllQrtzJobDetails);
            stmt.execute(deleteAllQrtzFiredTriggers);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        facade = new Facade(1, "endpoint_1", "jdbc:postgresql://localhost:5432/db_endpoint_one", "jacopo", "rentus");
        authProxy = AuthProxy.testConstructor();
        userAuth = UserAuth.login("admin@example.com", "password");
        String OTK = authProxy.generateOTK(userAuth);
        facade.setOTK(OTK);
        authProxy.associateEndpoint(userAuth, OTK, 1, "office_1");
        endpointAuth = EndpointAuth.login(1, facade.getOTK());
        authProxy.setEndpoint(endpointAuth, 1, facade);
        authProxy.addScanSchedule(userAuth, "Schedule every 30 seconds", "0/30 * * ? * * *", ScanType.FullScan);

        ArrayList<Integer> endpointsId = new ArrayList<>();
        endpointsId.add(1);
        authProxy.associateEndpointsToScanSchedule(userAuth, endpointsId, 1);

    }

    @Test
    @DisplayName("Add quarantine file")
    void testAddQUarantineFile() throws Exception {
        HashMap<Integer, QuarantineFile> hs = new HashMap<>();
        hs.put(1, new QuarantineFile(1, 1, 1, "qf_1", "details", "date"));
        facade.avController.generateQuarantineFile(hs);

        assertNotNull(authProxy.reportPageController.getAllReports().get(1).getQuarantineFiles().get(1));
        assertEquals(1, authProxy.reportPageController.getAllReports().get(1).getQuarantineFiles().get(1).getScanScheduleId());
        assertEquals(1, authProxy.reportPageController.getAllReports().get(1).getQuarantineFiles().get(1).getEndpointId());
        assertEquals(1, authProxy.reportPageController.getAllReports().get(1).getQuarantineFiles().get(1).getId());
        assertEquals("qf_1", authProxy.reportPageController.getAllReports().get(1).getQuarantineFiles().get(1).getName());
        assertEquals("details", authProxy.reportPageController.getAllReports().get(1).getQuarantineFiles().get(1).getDetails());
        assertEquals("date", authProxy.reportPageController.getAllReports().get(1).getQuarantineFiles().get(1).getDiscoveredDate());
    }

    @Test
    @DisplayName("Ignore Quarantine File")
    void testIgnoreQuarantineFile() throws Exception {
        HashMap<Integer, QuarantineFile> hs = new HashMap<>();
        hs.put(1, new QuarantineFile(1, 1, 1, "qf_1", "details", "date"));
        facade.avController.generateQuarantineFile(hs);

        authProxy.ignoreQuarantineFile(userAuth, 1);

        assertNull(authProxy.reportPageController.getAllReports().get(1).getQuarantineFiles().get(1));
    }
    //remove quarantine file is not tested because do the same operations

    private Connection getConnection(String URL, String USER, String PASS) {
        try {
            return DriverManager.getConnection(URL, USER, PASS);
        } catch (SQLException e) {
            throw new RuntimeException("Error connecting to DB");
        }
    }

}
