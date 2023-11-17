package it.unifi.swe.cepms.server_module.business_logic;

import it.unifi.swe.cepms.ScanType;
import it.unifi.swe.cepms.client_module.business_logic.Facade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class ScanSchedulePageControllerIntegretionTest {
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
//        authProxy.addScanSchedule(userAuth, "Schedule every 30 seconds", "0/30 * * ? * * *", ScanType.FullScan);
//
//        ArrayList<Integer> endpointsId = new ArrayList<>();
//        endpointsId.add(1);
//        authProxy.associateEndpointsToScanSchedule(userAuth, endpointsId, 1);

    }

    @Test
    @DisplayName("Add scan schedule")
    void TestAddScanSchedule() throws Exception {
        authProxy.addScanSchedule(userAuth,"Schedule every 30 seconds", "0/30 * * ? * * *", ScanType.FullScan);

        assertNotNull(authProxy.scanSchedulePageController.scanSchedules.get(1));
        assertEquals(1, authProxy.scanSchedulePageController.scanSchedules.get(1).getId());
        assertEquals("Schedule every 30 seconds", authProxy.scanSchedulePageController.scanSchedules.get(1).getName());
        assertEquals("0/30 * * ? * * *", authProxy.scanSchedulePageController.scanSchedules.get(1).getCron());
        assertEquals(ScanType.FullScan, authProxy.scanSchedulePageController.scanSchedules.get(1).getScanType());
    }

    @Test
    @DisplayName("Associate endpoint to scan schedule")
    void TestAssociateEndpointsToScanSchedule() throws Exception {
//        Facade facade = new Facade(1, "endpoint_1", "jdbc:postgresql://localhost:5432/db_endpoint_one", "niccolo", "123Stella");
//        String OTK = authProxy.generateOTK(userAuth);
//        facade.setOTK(OTK);
//        authProxy.associateEndpoint(userAuth, OTK, 1, "office_1");
//        EndpointAuth endpointAuth = EndpointAuth.login(1, facade.getOTK());
//        authProxy.setEndpoint(endpointAuth, 1, facade);

        authProxy.addScanSchedule(userAuth, "Schedule every 30 seconds", "0/30 * * ? * * *", ScanType.FullScan);
        ArrayList<Integer> endpointsId = new ArrayList<>();
        endpointsId.add(1);
        authProxy.associateEndpointsToScanSchedule(userAuth, endpointsId, 1);

        assertNotNull(authProxy.scanSchedulePageController.scanSchedules.get(1).getAssociatedEndpoint(1));
        assertEquals(1, authProxy.scanSchedulePageController.scanSchedules.get(1).getAssociatedEndpoint(1).getId());
        assertEquals("office_1", authProxy.scanSchedulePageController.scanSchedules.get(1).getAssociatedEndpoint(1).getName());
    }

    @Test
    @DisplayName("Deassociate endpoint to scan schedule")
    void TestDeassociateEndpointsToScanSchedule() throws Exception {
        authProxy.addScanSchedule(userAuth, "Schedule every 30 seconds", "0/30 * * ? * * *", ScanType.FullScan);
        ArrayList<Integer> endpointsId = new ArrayList<>();
        endpointsId.add(1);
        authProxy.associateEndpointsToScanSchedule(userAuth, endpointsId, 1);
        authProxy.deassociateEndpointsToScanSchedule(userAuth, endpointsId, 1);

        assertNull(authProxy.scanSchedulePageController.scanSchedules.get(1).getAssociatedEndpoint(1));
    }

    @Test
    @DisplayName("Remove scan schedule")
    void TestRemoveScanSchedule() throws Exception {
        authProxy.addScanSchedule(userAuth,"Schedule every 30 seconds", "0/30 * * ? * * *", ScanType.FullScan);
        authProxy.removeScanSchedule(userAuth,1);

        assertNull(authProxy.scanSchedulePageController.scanSchedules.get(1));
        assertNull(authProxy.reportPageController.getReport(1).getScanScheduleId());
    }

    @Test
    @DisplayName("Edit scan schedule")
    void TestEditScanSchedule() throws Exception {
        authProxy.addScanSchedule(userAuth,"Schedule every 30 seconds", "0/30 * * ? * * *", ScanType.FullScan);
        authProxy.editScanSchedule(userAuth,1,"0/20 0 0 ? * * *","Schedule every 20 seconds", ScanType.FastScan,true);

        assertEquals(1, authProxy.scanSchedulePageController.scanSchedules.get(1).getId());
        assertEquals("Schedule every 20 seconds", authProxy.scanSchedulePageController.scanSchedules.get(1).getName());
        assertEquals("0/20 0 0 ? * * *", authProxy.scanSchedulePageController.scanSchedules.get(1).getCron());
        assertEquals(ScanType.FastScan, authProxy.scanSchedulePageController.scanSchedules.get(1).getScanType());
        assertTrue(authProxy.scanSchedulePageController.scanSchedules.get(1).isEnabled());
    }

    private Connection getConnection(String URL, String USER, String PASS) {
        try {
            return DriverManager.getConnection(URL, USER, PASS);
        } catch (SQLException e) {
            throw new RuntimeException("Error connecting to DB");
        }
    }
}
