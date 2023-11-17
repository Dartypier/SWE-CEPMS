package it.unifi.swe.cepms.client_module.business_logic;

import it.unifi.swe.cepms.ScanType;
import it.unifi.swe.cepms.client_module.dao.QuarantineFileDao;
import it.unifi.swe.cepms.client_module.dao.ScanScheduleDao;
import it.unifi.swe.cepms.client_module.domain_model.Endpoint;
import it.unifi.swe.cepms.client_module.domain_model.QuarantineFile;
import it.unifi.swe.cepms.client_module.domain_model.ScanSchedule;
import it.unifi.swe.cepms.client_module.domain_model.SchedulerScan;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static org.junit.jupiter.api.Assertions.*;

public class ServerControllerIntegrationTest {

    private static final String URL = "jdbc:postgresql://localhost:5432/db_endpoint_one";
    private static final String USER = "jacopo";
    private static final String PASS = "rentus";
    static ScanScheduleDao scanScheduleDao;
    static QuarantineFileDao quarantineFileDao;
    Endpoint endpoint;
    ServerController serverController;

    @BeforeAll
    static void setUp(){
        scanScheduleDao = new ScanScheduleDao("jdbc:postgresql://localhost:5432/db_endpoint_one", "jacopo", "rentus");
        quarantineFileDao = new QuarantineFileDao("jdbc:postgresql://localhost:5432/db_endpoint_one", "jacopo", "rentus");
        //set in memory DB
        SchedulerScan.DEBUG = true;
    }

    @BeforeEach
    void setUpTable() throws Exception {
        //clearing table
        String deleteAllScanSchedules = "delete from scan_schedule;";
        String deleteAllQuarantineFiles = "delete from quarantine;";
        Connection con = getConnection();
        Statement stmt = null;

        try {
            stmt = con.createStatement();
            stmt.execute(deleteAllQuarantineFiles);
            stmt.execute(deleteAllScanSchedules);
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

        //creating objects
        endpoint = new Endpoint(1, "endpoint_1", "OTK");
        serverController = new ServerController(endpoint, scanScheduleDao, quarantineFileDao);

        //clearing scheduler
        endpoint.getScheduler().getSchedulerQuartz().clear();

        QuarantineFile quarantineFile = new QuarantineFile(1, 1, 1, "qf_1", "details", "date");
        endpoint.addQuarantineFile(quarantineFile);
        serverController.addScanSchedule(1, "10 0 0 ? * * *", ScanType.FullScan);

        //because is AVController that saves quarantine File, here we have to manually save it
        quarantineFileDao.save(quarantineFile, null);
    }

    @Test
    @DisplayName("Add scan schedule")
    void testAddScanSchedule() throws Exception {
        serverController.addScanSchedule(2, "10 0 0 ? * * *", ScanType.FullScan);

        ScanSchedule scanScheduleTest = endpoint.getScheduler().getScanSchedule(2);
        assertEquals(2, scanScheduleTest.getId());
        assertEquals("10 0 0 ? * * *", scanScheduleTest.getCron());
        assertEquals(ScanType.FullScan, scanScheduleTest.getScanType());

        Optional<ScanSchedule> optScanSchedule = scanScheduleDao.get("1");
        assertTrue(optScanSchedule.isPresent());
    }

    @Test
    @DisplayName("Remove scan schedule")
    void testRemoveScanSchedule() throws Exception {
        serverController.removeScanSchedule(1);

        assertNull(endpoint.getScheduler().getScanSchedule(1));
        Optional<ScanSchedule> optScanSchedule = scanScheduleDao.get("1");

        assertFalse(optScanSchedule.isPresent());
    }

    @Test
    @DisplayName("Edit scan schedule")
    void testEditScanSchedule() throws Exception {
        serverController.editScanSchedule(1, "15 0 0 ? * * *", ScanType.FastScan, false);
        assertEquals(1, endpoint.getScheduler().getScanSchedule(1).getId());
        assertEquals("15 0 0 ? * * *", endpoint.getScheduler().getScanSchedule(1).getCron());
        assertEquals(ScanType.FastScan, endpoint.getScheduler().getScanSchedule(1).getScanType());

        Optional<ScanSchedule> optScanSchedule = scanScheduleDao.get("1");
        assertTrue(optScanSchedule.isPresent());
        assertEquals(1, optScanSchedule.get().getId());
        assertEquals("15 0 0 ? * * *", optScanSchedule.get().getCron());
        assertEquals(ScanType.FastScan, optScanSchedule.get().getScanType());
    }

    @Test
    @DisplayName("Remove quarantine file")
    void testRemoveQuarantineFile() throws Exception {
        serverController.removeQuarantineFile(1);

        Optional<QuarantineFile> optionalQuarantineFileDao = quarantineFileDao.get("1");
        assertFalse(optionalQuarantineFileDao.isPresent());

        assertNull(endpoint.getQuarantineFiles().get(1));

    }

    //ignore is the same as remove quaranine file

    private Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASS);
        } catch (SQLException e) {
            throw new RuntimeException("Error connecting to DB");
        }
    }
}
