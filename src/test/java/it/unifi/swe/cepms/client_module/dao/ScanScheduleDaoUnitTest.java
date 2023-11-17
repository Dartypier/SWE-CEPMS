package it.unifi.swe.cepms.client_module.dao;

import it.unifi.swe.cepms.ScanType;
import it.unifi.swe.cepms.client_module.dao.ScanScheduleDao;
import it.unifi.swe.cepms.client_module.domain_model.ScanSchedule;
import org.junit.jupiter.api.*;

import java.util.HashMap;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ScanScheduleDaoUnitTest {

    static ScanScheduleDao scanScheduleDao;
    ScanSchedule scanScheduleMock1;
    ScanSchedule scanScheduleMock2;

    @BeforeAll
     static void setUpDB(){
        scanScheduleDao = new ScanScheduleDao("jdbc:postgresql://localhost:5432/db_endpoint_one", "jacopo", "rentus");
        HashMap<Integer, ScanSchedule> hs = scanScheduleDao.getAll();
        for (Integer scanId: hs.keySet()) {
            scanScheduleDao.delete(hs.get(scanId));
        }
    }

    @BeforeEach
    void setUpTable(){
        scanScheduleMock1 = mock(ScanSchedule.class);
        scanScheduleMock2 = mock(ScanSchedule.class);
        when(scanScheduleMock1.getId()).thenReturn(1);
        when(scanScheduleMock1.getCron()).thenReturn("10 0 0 ? * * *");
        when(scanScheduleMock1.getScanType()).thenReturn(ScanType.FullScan);
        when(scanScheduleMock2.getId()).thenReturn(2);
        when(scanScheduleMock2.getCron()).thenReturn("12 0 0 ? * * *");
        when(scanScheduleMock2.getScanType()).thenReturn(ScanType.FastScan);
        scanScheduleDao.save(scanScheduleMock1,null);
        scanScheduleDao.save(scanScheduleMock2, null);
    }

    @AfterEach
    void tearDown(){
        HashMap<Integer, ScanSchedule> hs = scanScheduleDao.getAll();
        for (Integer scanId: hs.keySet()) {
            scanScheduleDao.delete(hs.get(scanId));
        }
    }

    @Test
    @DisplayName("Get scan schedule from DB")
    void testGet(){
        Optional<ScanSchedule> scanSchedule = scanScheduleDao.get("1");
        assertTrue(scanSchedule.isPresent());
        assertEquals(1, scanSchedule.get().getId());
        assertEquals("10 0 0 ? * * *", scanSchedule.get().getCron());
        assertEquals(ScanType.FullScan, scanSchedule.get().getScanType());
        assertEquals("DEFAULT.job_1",scanSchedule.get().getJobKey().toString());
        assertEquals("DEFAULT.trigger_1", scanSchedule.get().getTriggerKey().toString());
    }

    @Test
    @DisplayName("Get all scan schedule from DB")
    void testGetAll(){
        HashMap<Integer, ScanSchedule> hs = scanScheduleDao.getAll();

        assertEquals(1, hs.get(1).getId());
        assertEquals("10 0 0 ? * * *", hs.get(1).getCron());
        assertEquals(ScanType.FullScan, hs.get(1).getScanType());
        assertEquals("DEFAULT.job_1",hs.get(1).getJobKey().toString());
        assertEquals("DEFAULT.trigger_1", hs.get(1).getTriggerKey().toString());

        assertEquals(2, hs.get(2).getId());
        assertEquals("12 0 0 ? * * *", hs.get(2).getCron());
        assertEquals(ScanType.FastScan, hs.get(2).getScanType());
        assertEquals("DEFAULT.job_2",hs.get(2).getJobKey().toString());
        assertEquals("DEFAULT.trigger_2", hs.get(2).getTriggerKey().toString());
    }

    @Test
    @DisplayName("Save scan schedule to DB")
    void testSave(){
        ScanSchedule scanScheduleMock = mock(ScanSchedule.class);
        when(scanScheduleMock.getId()).thenReturn(3);
        when(scanScheduleMock.getCron()).thenReturn("10 0 0 ? * * *");
        when(scanScheduleMock.getScanType()).thenReturn(ScanType.FullScan);
        scanScheduleDao.save(scanScheduleMock,null);

        Optional<ScanSchedule> scanSchedule = scanScheduleDao.get("3");
        assertTrue(scanSchedule.isPresent());
        assertEquals(3, scanSchedule.get().getId());
        assertEquals("10 0 0 ? * * *", scanSchedule.get().getCron());
        assertEquals(ScanType.FullScan, scanSchedule.get().getScanType());
        assertEquals("DEFAULT.job_3",scanSchedule.get().getJobKey().toString());
        assertEquals("DEFAULT.trigger_3", scanSchedule.get().getTriggerKey().toString());
    }

    @Test
    @DisplayName("Update scan schedule")
    void testUpdate(){
        when(scanScheduleMock1.getId()).thenReturn(1);
        when(scanScheduleMock1.getCron()).thenReturn("15 0 0 ? * * *");
        when(scanScheduleMock1.getScanType()).thenReturn(ScanType.FastScan);
        scanScheduleDao.update(scanScheduleMock1,null);

        Optional<ScanSchedule> scanSchedule = scanScheduleDao.get("1");
        assertTrue(scanSchedule.isPresent());
        assertEquals(1, scanSchedule.get().getId());
        assertEquals("15 0 0 ? * * *", scanSchedule.get().getCron());
        assertEquals(ScanType.FastScan, scanSchedule.get().getScanType());
        assertEquals("DEFAULT.job_1",scanSchedule.get().getJobKey().toString());
        assertEquals("DEFAULT.trigger_1", scanSchedule.get().getTriggerKey().toString());
    }

    @Test
    @DisplayName("Delete scan schedule from DB")
    void testDelete(){
        scanScheduleDao.delete(scanScheduleMock1);
        Optional<ScanSchedule> scanSchedule = scanScheduleDao.get("1");
        assertFalse(scanSchedule.isPresent());
    }


}
