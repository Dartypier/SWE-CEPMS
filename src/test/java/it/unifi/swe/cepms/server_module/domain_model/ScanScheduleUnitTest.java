package it.unifi.swe.cepms.server_module.domain_model;

import it.unifi.swe.cepms.ScanType;
import it.unifi.swe.cepms.server_module.domain_model.Endpoint;
import it.unifi.swe.cepms.server_module.domain_model.ScanSchedule;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class ScanScheduleUnitTest {
    private ScanSchedule scanSchedule;

    @BeforeEach
    void SetUp(){
        scanSchedule = new ScanSchedule(1, "scan_1", "cron_1", ScanType.FullScan, true);
    }

    @Test
    @DisplayName("Constructor ScanSchedule")
    void testConstructor(){
        ScanSchedule scanSchedule = new ScanSchedule(1, "scan_1", "cron_1", ScanType.FullScan, true);
        assertEquals(1, scanSchedule.getId());
        assertEquals("scan_1", scanSchedule.getName());
        assertEquals("cron_1", scanSchedule.getCron());
        assertEquals(ScanType.FullScan, scanSchedule.getScanType());
        assertTrue(scanSchedule.isEnabled());
        assertNotNull(scanSchedule.getAllAssociatedEndpoints());
    }

    @Test
    @DisplayName("Get all associated endpoints of scan schedule object")
    void testGetAllAssociatedEndpoints(){
        Endpoint endpoint1 = mock(Endpoint.class);
        when(endpoint1.getId()).thenReturn(1);
        when(endpoint1.getName()).thenReturn("endpoint_1");
        when(endpoint1.getName()).thenReturn("OTK1");

        Endpoint endpoint2 = mock(Endpoint.class);
        when(endpoint2.getId()).thenReturn(2);
        when(endpoint2.getName()).thenReturn("endpoint_2");
        when(endpoint2.getName()).thenReturn("OTK2");

        scanSchedule.addAssociatedEndpoint(endpoint1);
        scanSchedule.addAssociatedEndpoint(endpoint2);

        ArrayList<Integer> arr = scanSchedule.getAllAssociatedEndpoints();
        assertEquals(1, arr.get(0));
        assertEquals(2, arr.get(1));

        //test non existing ID
        assertEquals(2, arr.size());
    }

    @Test
    @DisplayName("Get associated endpoint to scan schedule")
    void testGetAssociatedEndpoint(){
        Endpoint endpoint1 = mock(Endpoint.class);
        when(endpoint1.getId()).thenReturn(1);
        when(endpoint1.getName()).thenReturn("endpoint_1");
        when(endpoint1.getName()).thenReturn("OTK1");

        scanSchedule.addAssociatedEndpoint(endpoint1);
        Endpoint testEndpoint = scanSchedule.getAssociatedEndpoint(1);

        assertInstanceOf(Endpoint.class, testEndpoint);
    }

    @Test
    @DisplayName("Associating endpoint to scan schedule")
    void testAddAssociatedEndpoint(){
        Endpoint endpoint1 = mock(Endpoint.class);
        when(endpoint1.getId()).thenReturn(1);
        when(endpoint1.getName()).thenReturn("endpoint_1");
        when(endpoint1.getName()).thenReturn("OTK1");

        scanSchedule.addAssociatedEndpoint(endpoint1);
        Endpoint testEndpoint = scanSchedule.getAssociatedEndpoint(1);

        assertInstanceOf(Endpoint.class, testEndpoint);
    }

    @Test
    @DisplayName("Deassociating endpoint from scan schedule")
    void testRemoveAssociatedEndpoint(){
        Endpoint endpoint1 = mock(Endpoint.class);
        when(endpoint1.getId()).thenReturn(1);
        when(endpoint1.getName()).thenReturn("endpoint_1");
        when(endpoint1.getName()).thenReturn("OTK1");

        scanSchedule.addAssociatedEndpoint(endpoint1);
        scanSchedule.removeAssociatedEndpoint(endpoint1);

        assertNull(scanSchedule.getAssociatedEndpoint(1));
    }

    @Test
    @DisplayName("Edit cron of scan schedule")
    void testEditCron() {
        scanSchedule.editCron("edited_cron");
        assertEquals("edited_cron", scanSchedule.getCron());

    }
}
