package it.unifi.swe.cepms.server_module.dao;

import it.unifi.swe.cepms.ScanType;
import it.unifi.swe.cepms.server_module.dao.EndpointDao;
import it.unifi.swe.cepms.server_module.dao.ScanScheduleDao;
import it.unifi.swe.cepms.server_module.domain_model.Endpoint;
import it.unifi.swe.cepms.server_module.domain_model.ScanSchedule;
import it.unifi.swe.cepms.server_module.dao.MapScanScheduleToEndpointDao;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MapScanScheduleToEndpointDaoUnitTest {
    private static final String URL = "jdbc:postgresql://localhost:5432/server_module";
    private static final String USER = "jacopo";
    private static final String PASS = "rentus";
    static MapScanScheduleToEndpointDao mapScanScheduleToEndpointDao;
    static EndpointDao endpointDao;
    static ScanScheduleDao scanScheduleDao;
    Endpoint endpoint1;
    Endpoint endpoint2;
    ScanSchedule scanSchedule;

    @BeforeAll
    static void setUpDB(){
        mapScanScheduleToEndpointDao = new MapScanScheduleToEndpointDao();
        endpointDao = new EndpointDao();
        scanScheduleDao = new ScanScheduleDao();
    }

    @BeforeEach
    void setUpTable() {
        //clearing tables
        String deleteAllMap = "delete from map_scan_schedule_to_endpoint;";
        String deleteAllEndpoints = "delete from endpoint;";
        String deleteAllScanSchedule = "delete from scan_schedule;";

        Connection con = getConnection();
        Statement stmt = null;

        try {
            stmt = con.createStatement();
            stmt.execute(deleteAllMap);
            stmt.execute(deleteAllEndpoints);
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

        //creating and saving scan schedule
        scanSchedule = mock(ScanSchedule.class);
        when(scanSchedule.getId()).thenReturn(1);
        when(scanSchedule.getName()).thenReturn("");
        when(scanSchedule.getCron()).thenReturn("");
        when(scanSchedule.getScanType()).thenReturn(ScanType.FullScan);
        //other fields are mockito defaults
        scanScheduleDao.save(scanSchedule, null);

        //creating and saving endpoints
        endpoint1 = mock(Endpoint.class);
        when(endpoint1.getId()).thenReturn(10);
        when(endpoint1.getName()).thenReturn("");
        when(endpoint1.getOTK()).thenReturn("");
        endpoint2 = mock(Endpoint.class);
        when(endpoint2.getId()).thenReturn(20);
        when(endpoint2.getName()).thenReturn("");
        when(endpoint2.getOTK()).thenReturn("");
        endpointDao.save(endpoint1, null);
        endpointDao.save(endpoint2, null);
    }

    @Test
    @DisplayName("Get all rows from table")
    void testGetAll(){
        //creating scan and endpoints and saving to DB because are FK
        mapScanScheduleToEndpointDao.save(1, 10);
        mapScanScheduleToEndpointDao.save(1, 20);

        ArrayList<Integer> arr = mapScanScheduleToEndpointDao.getAll(scanSchedule.getId());
        assertEquals(10, arr.get(0));
        assertEquals(20, arr.get(1));
    }

    @Test
    @DisplayName("Save association scan_schedule and endpoint")
    void testSave(){
        mapScanScheduleToEndpointDao.save(1, 10);
        ArrayList<Integer> arr = mapScanScheduleToEndpointDao.getAll(scanSchedule.getId());
        assertEquals(10, arr.get(0));
    }

    private Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASS);
        } catch (SQLException e) {
            throw new RuntimeException("Error connecting to DB");
        }
    }
}
