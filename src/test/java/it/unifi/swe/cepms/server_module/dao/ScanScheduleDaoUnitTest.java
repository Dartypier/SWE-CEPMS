package it.unifi.swe.cepms.server_module.dao;

import it.unifi.swe.cepms.ScanType;
import it.unifi.swe.cepms.server_module.dao.ScanScheduleDao;
import it.unifi.swe.cepms.server_module.domain_model.ScanSchedule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.TreeMap;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ScanScheduleDaoUnitTest {

    private static final String URL = "jdbc:postgresql://localhost:5432/server_module";
    private static final String USER = "jacopo";
    private static final String PASS = "rentus";
    private static ScanScheduleDao scanScheduleDao;
    private ScanSchedule scanSchedule1;
    private ScanSchedule scanSchedule2;

    @BeforeAll
    static void setUpDB() {
        scanScheduleDao = new ScanScheduleDao();
    }

    @BeforeEach
    void setUpTable() {
        //clearing table
        String deleteAllScanSchedules = "delete from scan_schedule;";
        Connection con = getConnection();
        Statement stmt = null;

        try {
            stmt = con.createStatement();
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

        //creating rows
        scanSchedule1 = mock(ScanSchedule.class);
        scanSchedule2 = mock(ScanSchedule.class);

        when(scanSchedule1.getId()).thenReturn(1);
        when(scanSchedule1.getName()).thenReturn("scan_1");
        when(scanSchedule1.getCron()).thenReturn("cron_1");
        when(scanSchedule1.getScanType()).thenReturn(ScanType.FullScan);
        when(scanSchedule1.isEnabled()).thenReturn(true);

        when(scanSchedule2.getId()).thenReturn(2);
        when(scanSchedule2.getName()).thenReturn("scan_2");
        when(scanSchedule2.getCron()).thenReturn("cron_2");
        when(scanSchedule2.getScanType()).thenReturn(ScanType.FullScan);
        when(scanSchedule2.isEnabled()).thenReturn(true);

        scanScheduleDao.save(scanSchedule1, null);
        scanScheduleDao.save(scanSchedule2, null);
    }

    @Test
    @DisplayName("Get scan schedule from DB")
    void testGet(){
        Optional<ScanSchedule> scanSchedule = scanScheduleDao.get("1");

        assertTrue(scanSchedule.isPresent());
        assertEquals(1, scanSchedule.get().getId());
        assertEquals("scan_1", scanSchedule.get().getName());
        assertEquals("cron_1", scanSchedule.get().getCron());
        assertEquals(ScanType.FullScan, scanSchedule.get().getScanType());
        assertTrue(scanSchedule.get().isEnabled());
    }

    @Test
    @DisplayName("Get All scan schedules from DB")
    void testGetAll(){
        TreeMap<Integer, ScanSchedule> tm = scanScheduleDao.getAll();

        assertTrue(tm.containsKey(1));
        assertEquals(1, tm.get(1).getId());
        assertEquals("scan_1", tm.get(1).getName());
        assertEquals("cron_1", tm.get(1).getCron());
        assertEquals(ScanType.FullScan, tm.get(1).getScanType());
        assertTrue(tm.get(1).isEnabled());

        assertTrue(tm.containsKey(2));
        assertEquals(2, tm.get(2).getId());
        assertEquals("scan_2", tm.get(2).getName());
        assertEquals("cron_2", tm.get(2).getCron());
        assertEquals(ScanType.FullScan, tm.get(2).getScanType());
        assertTrue(tm.get(2).isEnabled());

        //check non existing scanSchedule
        assertFalse(tm.containsKey(3));
    }

    @Test
    @DisplayName("Save scan schedule to DB")
    void testSave(){
        ScanSchedule scanSchedule = mock(ScanSchedule.class);
        when(scanSchedule.getId()).thenReturn(3);
        when(scanSchedule.getName()).thenReturn("scan_3");
        when(scanSchedule.getCron()).thenReturn("cron_3");
        when(scanSchedule.getScanType()).thenReturn(ScanType.FullScan);
        when(scanSchedule.isEnabled()).thenReturn(true);
        scanScheduleDao.save(scanSchedule, null);

        Optional<ScanSchedule> scanOpt = scanScheduleDao.get("3");
        assertTrue(scanOpt.isPresent());
        assertEquals(3, scanOpt.get().getId());
        assertEquals("scan_3", scanOpt.get().getName());
        assertEquals("cron_3", scanOpt.get().getCron());
        assertEquals(ScanType.FullScan, scanOpt.get().getScanType());
        assertTrue(scanOpt.get().isEnabled());
    }

    @Test
    @DisplayName("Update scan schedule DB")
    void testUpdate(){

        scanScheduleDao.update(scanSchedule1, new String[]{"scan_up", "cron_up", ScanType.FastScan.toString(), String.valueOf(false)});

        Optional<ScanSchedule> scanOpt = scanScheduleDao.get("1");

        assertTrue(scanOpt.isPresent());
        assertEquals(1, scanOpt.get().getId());
        assertEquals("scan_up", scanOpt.get().getName());
        assertEquals("cron_up", scanOpt.get().getCron());
        assertEquals(ScanType.FastScan, scanOpt.get().getScanType());
        assertFalse(scanOpt.get().isEnabled());
    }

    @Test
    @DisplayName("Delete scan schedule from DB")
    void testDelete(){
        scanScheduleDao.delete(scanSchedule1);

        Optional<ScanSchedule> scanSchedule = scanScheduleDao.get("1");
        assertFalse(scanSchedule.isPresent());
    }

    private Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASS);
        } catch (SQLException e) {
            throw new RuntimeException("Error connecting to DB");
        }
    }
}
