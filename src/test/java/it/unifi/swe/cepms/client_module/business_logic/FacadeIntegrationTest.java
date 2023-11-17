package it.unifi.swe.cepms.client_module.business_logic;

import it.unifi.swe.cepms.ScanType;
import it.unifi.swe.cepms.client_module.dao.EndpointDao;
import it.unifi.swe.cepms.client_module.dao.QuarantineFileDao;
import it.unifi.swe.cepms.client_module.dao.ScanScheduleDao;
import it.unifi.swe.cepms.client_module.domain_model.Endpoint;
import it.unifi.swe.cepms.client_module.domain_model.QuarantineFile;
import it.unifi.swe.cepms.client_module.domain_model.ScanSchedule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

public class FacadeIntegrationTest {

    private String URL = "jdbc:postgresql://localhost:5432/db_endpoint_one";
    private String USER = "jacopo";
    private String PASS = "rentus";

    @BeforeEach
    void SetUpTables() throws Exception {

        //clearing table
        String deleteAllEndpoints = "delete from endpoint;";
        String deleteAllScanSchedules = "delete from scan_schedule;";
        String deleteAllQuarantineFiles = "delete from quarantine;";
        Connection con = getConnection();
        Statement stmt = null;

        try {
            stmt = con.createStatement();
            stmt.execute(deleteAllEndpoints);
            stmt.execute(deleteAllScanSchedules);
            stmt.execute(deleteAllQuarantineFiles);
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

        //adding rows

        EndpointDao endpointDao = new EndpointDao(URL, USER, PASS);
        ScanScheduleDao scanScheduleDao = new ScanScheduleDao(URL, USER, PASS);
        QuarantineFileDao quarantineFileDao = new QuarantineFileDao(URL, USER, PASS);

        Endpoint endpoint = new Endpoint(1, "endpoint_1", "OTK1");
        ScanSchedule scanSchedule1 = new ScanSchedule(1, "10 0 0 ? * * *", ScanType.FullScan);
        ScanSchedule scanSchedule2 = new ScanSchedule(2, "15 0 0 ? * * *", ScanType.FullScan);
        QuarantineFile quarantineFile1 = new QuarantineFile(1, 1, 1, "qf_1", "details", "date");
        QuarantineFile quarantineFile2 = new QuarantineFile(2, 2, 1, "qf_2", "details", "date");

        endpointDao.save(endpoint, null);
        scanScheduleDao.save(scanSchedule1, null);
        scanScheduleDao.save(scanSchedule2, null);
        quarantineFileDao.save(quarantineFile1, null);
        quarantineFileDao.save(quarantineFile2, null);
    }

    @Test
    @DisplayName("Testing constructor with endpoint")
    void testFacadeWithEndpoint() throws Exception {
        Facade facade = new Facade(1, "endpoint_1", URL, USER, PASS);
        Endpoint endpoint = facade.endpoint;

        assertNotNull(endpoint.getScheduler());
        assertNotNull(endpoint.getScheduler().getScanSchedule(1));
        assertNotNull(endpoint.getScheduler().getScanSchedule(2));

        assertNotNull(endpoint.getQuarantineFiles().get(1));
        assertNotNull(endpoint.getQuarantineFiles().get(2));

    }

    @Test
    @DisplayName("Testing constructor without endpoint")
    void testFacadeWithoutEndpoint() throws Exception {
        //Endpoint with id=2 is not in DB
        Facade facade = new Facade(2, "endpoint_2", URL, USER, PASS);
        Endpoint endpoint = facade.endpoint;

        assertNotNull(endpoint.getScheduler());
        //verify it doesn't have schedules and quarantine files
        //actually the scheduler mantains scheduleScan from DB
        //but in a rela scenario shouldn't be a problem
        assertTrue(endpoint.getQuarantineFiles().isEmpty());
        assertTrue(endpoint.getScheduler().isSchedulesEmpty());
    }

    private Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASS);
        } catch (SQLException e) {
            throw new RuntimeException("Error connecting to DB");
        }
    }

}


