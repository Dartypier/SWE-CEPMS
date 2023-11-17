package it.unifi.swe.cepms.server_module.dao;

import it.unifi.swe.cepms.ScanType;
import it.unifi.swe.cepms.server_module.dao.ReportDao;
import it.unifi.swe.cepms.server_module.dao.ScanScheduleDao;
import it.unifi.swe.cepms.server_module.domain_model.Report;
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
import java.util.HashMap;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ReportDaoUnitTest {
    private static final String URL = "jdbc:postgresql://localhost:5432/server_module";
    private static final String USER = "jacopo";
    private static final String PASS = "rentus";
    private static ReportDao reportDao;
    private Report report1;
    private Report report2;

    @BeforeAll
    static void setUpDB() {
        reportDao = new ReportDao();
    }

    @BeforeEach
    void setUpTable() {
        //clearing tables
        String deleteAllReports = "delete from report;";
        String deleteAllScanSchedule = "delete from scan_schedule;";
        Connection con = getConnection();
        Statement stmt = null;

        try {
            stmt = con.createStatement();
            stmt.execute(deleteAllReports);
            stmt.execute(deleteAllScanSchedule);
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

        //creating and saving scanSchedule and reports
        ScanScheduleDao scanScheduleDao = new ScanScheduleDao();

        ScanSchedule scanSchedule1 = mock(ScanSchedule.class);
        ScanSchedule scanSchedule2 = mock(ScanSchedule.class);

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

        report1 = mock(Report.class);
        when(report1.getId()).thenReturn(1);
        when(report1.getName()).thenReturn("report_1");
        when(report1.getScanScheduleId()).thenReturn(1);

        report2 = mock(Report.class);
        when(report2.getId()).thenReturn(2);
        when(report2.getName()).thenReturn("report_2");
        when(report2.getScanScheduleId()).thenReturn(2);

        reportDao.save(report1, null);
        reportDao.save(report2, null);
    }

    @Test
    @DisplayName("Get report from DB")
    void testGet() {
        Optional<Report> report = reportDao.get("1");

        assertTrue(report.isPresent());
        assertEquals(1, report.get().getId());
        assertEquals("report_1", report.get().getName());
        assertEquals(1, report.get().getScanScheduleId());
    }

    @Test
    @DisplayName("Get all reports from DB")
    void testGetAll() {
        HashMap<Integer, Report> hm = reportDao.getAll();

        assertTrue(hm.containsKey(1));
        assertEquals(1, hm.get(1).getId());
        assertEquals("report_1", hm.get(1).getName());
        assertEquals(1, hm.get(1).getScanScheduleId());

        assertTrue(hm.containsKey(2));
        assertEquals(2, hm.get(2).getId());
        assertEquals("report_2", hm.get(2).getName());
        assertEquals(2, hm.get(2).getScanScheduleId());

        //check non existing scanSchedule
        assertFalse(hm.containsKey(3));
    }

    @Test
    @DisplayName("Save report to DB")
    void testSave() {
        Report report = mock(Report.class);
        when(report.getId()).thenReturn(3);
        when(report.getName()).thenReturn("report_3");
        when(report.getScanScheduleId()).thenReturn(3);

        ScanSchedule scanSchedule3 = mock(ScanSchedule.class);
        when(scanSchedule3.getId()).thenReturn(3);
        when(scanSchedule3.getName()).thenReturn("scan_3");
        when(scanSchedule3.getCron()).thenReturn("cron_3");
        when(scanSchedule3.getScanType()).thenReturn(ScanType.FullScan);
        when(scanSchedule3.isEnabled()).thenReturn(true);

        ScanScheduleDao scanScheduleDao = new ScanScheduleDao();
        scanScheduleDao.save(scanSchedule3, null);
        reportDao.save(report, null);

        Optional<Report> optRep = reportDao.get("3");
        assertTrue(optRep.isPresent());
        assertEquals(3, optRep.get().getId());
        assertEquals("report_3", optRep.get().getName());
        assertEquals(3, optRep.get().getScanScheduleId());
    }


    private Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASS);
        } catch (SQLException e) {
            throw new RuntimeException("Error connecting to DB");
        }
    }
}
