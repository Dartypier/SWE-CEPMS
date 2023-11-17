package it.unifi.swe.cepms;

import it.unifi.swe.cepms.client_module.business_logic.FacadeIntegrationTest;
import it.unifi.swe.cepms.client_module.business_logic.ServerControllerIntegrationTest;
import it.unifi.swe.cepms.client_module.dao.ScanScheduleDaoUnitTest;
import it.unifi.swe.cepms.client_module.domain_model.SchedulerScanUnitTest;
import it.unifi.swe.cepms.server_module.business_logic.EndpointPageControllerIntegrationTest;
import it.unifi.swe.cepms.server_module.business_logic.QuarantineFilePageControllerIntegrationTest;
import it.unifi.swe.cepms.server_module.business_logic.ScanSchedulePageControllerIntegretionTest;
import it.unifi.swe.cepms.server_module.business_logic.UserAuthUniTest;
import it.unifi.swe.cepms.server_module.dao.MapScanScheduleToEndpointDaoUnitTest;
import it.unifi.swe.cepms.server_module.dao.ReportDaoUnitTest;
import it.unifi.swe.cepms.server_module.domain_model.ScanScheduleUnitTest;
import org.junit.platform.suite.api.*;

//suite for executing all test classes
//Note: to execute the tests, the DBs and Tables
//are required

@Suite
@SuiteDisplayName("Testing all classes of test")
@SelectClasses({
        FacadeIntegrationTest.class,
        ServerControllerIntegrationTest.class,
        ScanScheduleDaoUnitTest.class,
        SchedulerScanUnitTest.class,

        MapScanScheduleToEndpointDaoUnitTest.class,
        ReportDaoUnitTest.class,
        ScanScheduleDaoUnitTest.class,
        ScanScheduleUnitTest.class,
        UserAuthUniTest.class,
        QuarantineFilePageControllerIntegrationTest.class,
        EndpointPageControllerIntegrationTest.class,
        ScanSchedulePageControllerIntegretionTest.class,
})
public class ExecuteSuite {
}
