package main;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.openqa.selenium.WebDriver;

import serviceNow.ServiceNow;
import utils.Config;
import utils.Config.EmptyEnvFileException;

public class TestRunner {

	ServiceNow sn;
	boolean allGood = true;

	@Test
	public void main() {

		Config c;
		try {
			c = new Config();
		} catch (EmptyEnvFileException e1) {
			System.out.println("Config file is not correctly filled out");
			return;
		}

		// Selenium Settings
		boolean REMOTE = c.getRUN_REMOTELY();
		String REMOTE_IP = c.getREMOTE_SERVER_ADDRESS();
		boolean EXIT_ON_FINISHED = c.getEXIT_ON_FINISHED();

		// Service Now Settings
		String instanceId = c.getINSTANCE_ID();

		WebDriver driver = REMOTE ? seleniumConfig.Browsers.chrome(REMOTE_IP) : seleniumConfig.Browsers.chrome();
		this.sn = new ServiceNow(driver, instanceId);

		sn.open();

		// Test this software
		// runTest(new self.Impersonation());
		// runTest(new self.Tables());
		// runTest(new self.Records());
		//runTest(new self.Fields(sn));

		// Test cases
		runTest(new UTMOR_LR.Test_01(sn));

		if (EXIT_ON_FINISHED)
			driver.quit();

		if (allGood)
			assertTrue(true);
		else
			assertTrue(false);
	}

	private void runTest(TestTemplate test) {
		System.out.println("[TEST] started - " + test.name());
		boolean testSuccess = false;

		try {
			test.preTest();
			testSuccess = test.test();
		} catch (Exception e) {
			e.printStackTrace();
			TestTemplate.saveSnapshot(e, sn.getDriver(), "snapshots\\" + test.name() + "\\failiure");
			utils.DebuggingTool.pause();
		} finally {
			test.postTest();
		}

		if (testSuccess)
			System.out.println("[TEST] OK : " + test.name());
		else {
			System.out.println("[TEST] FAIL : " + test.name());
			allGood = false;
		}
	}

}
