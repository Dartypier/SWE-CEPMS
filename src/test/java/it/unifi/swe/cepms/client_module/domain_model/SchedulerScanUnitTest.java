package it.unifi.swe.cepms.client_module.domain_model;

import it.unifi.swe.cepms.ScanType;
import it.unifi.swe.cepms.client_module.domain_model.SchedulerScan;
import it.unifi.swe.cepms.client_module.domain_model.ScanSchedule;
import org.junit.jupiter.api.*;
import org.quartz.*;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SchedulerScanUnitTest {
    //note because Quartz here has inmemory DB, scanSchedule gets cleared afterEach
    static SchedulerScan schedulerScan;
    ScanSchedule scanScheduleMock;

    @BeforeAll
    static void setUpSchedulerScan() throws SchedulerException {
        //enabling debug for testing
        SchedulerScan.DEBUG = true;
        schedulerScan = new SchedulerScan();
    }

    @AfterEach
    void teardownScanSchedule() throws SchedulerException {
        schedulerScan.getSchedulerQuartz().clear();
    }

    @BeforeEach
    void setUpScanSchedule(){
        scanScheduleMock = mock(ScanSchedule.class);
        when(scanScheduleMock.getId()).thenReturn(1);
        when(scanScheduleMock.getCron()).thenReturn("10 0 0 ? * * *");
    }

    @Test
    @DisplayName("Constructor without args")
    void testSchedulerScanConstructorWithoutArgs() throws SchedulerException {
        SchedulerScan schedulerScan = new SchedulerScan();
        assertNotNull(schedulerScan);
    }

    @Test
    @DisplayName("Constructor with args")
    void testSchedulerScanConstructorWithArgs() throws SchedulerException {
        HashMap<Integer, ScanSchedule> arr = new HashMap<>();
        arr.put(scanScheduleMock.getId(), scanScheduleMock);

        SchedulerScan schedulerScan = new SchedulerScan(arr);
        assertNotNull(schedulerScan);
        assertEquals(scanScheduleMock, schedulerScan.getScanSchedule(1));
    }

    @Test
    @DisplayName("add scan schedule")
    void testAddScanSchedule() throws Exception {
        schedulerScan.addScanSchedule(scanScheduleMock);
        assertEquals(scanScheduleMock, schedulerScan.getScanSchedule(1));
        //scheduleJob is tested by Quartz
    }

    @Test
    @DisplayName("remove scan schedule")
    void testRemoveScanSchedule() throws Exception {
        //we need a spy because addSchedule sets job and trigger keys
        ScanSchedule scanScheduleSpy = spy(new ScanSchedule());
        when(scanScheduleSpy.getId()).thenReturn(1);
        when(scanScheduleSpy.getCron()).thenReturn("10 0 0 ? * * *");

        schedulerScan.addScanSchedule(scanScheduleSpy);
        schedulerScan.removeScanSchedule(scanScheduleSpy.getId());

        assertNull(schedulerScan.getScanSchedule(scanScheduleSpy.getId()));
    }
    
    @Test
    @DisplayName("edit scan schedule")
    void testEditScanSchedule() throws Exception {
        ScanSchedule scanScheduleSpy = spy(new ScanSchedule(2, "10 0 0 ? * * *", ScanType.FullScan));
        schedulerScan.addScanSchedule(scanScheduleSpy);

        //Trigger key and JobKey must not change
        TriggerKey triggerKey = scanScheduleSpy.getTriggerKey();
        JobKey jobKey = scanScheduleSpy.getJobKey();

        //scan schedule id doesn't change, is an identifier
        schedulerScan.editScanSchedule(2, "12 0 0 ? * * *", ScanType.FastScan, true);

        assertEquals(2, scanScheduleSpy.getId());
        assertEquals("12 0 0 ? * * *", scanScheduleSpy.getCron());
        assertEquals(ScanType.FastScan, scanScheduleSpy.getScanType());

        //assert keys
        assertEquals(jobKey, scanScheduleSpy.getJobKey());
        assertEquals(triggerKey, scanScheduleSpy.getTriggerKey());
    }

}
