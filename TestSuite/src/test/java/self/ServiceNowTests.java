package self;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import org.junit.Test;
import org.openqa.selenium.WebDriver;

import serviceNow.SNField;
import serviceNow.SNField.InvalidFieldException;
import serviceNow.FormHandler;
import serviceNow.SNRecord;
import serviceNow.ServiceNow;
import serviceNow.SNTable;
import serviceNow.User;
import utils.Config;
import utils.Config.EmptyEnvFileException;

public class ServiceNowTests {

	@Test
	public void tests() {

		Config c;
		try {
			c = new Config();
		} catch (EmptyEnvFileException e1) {
			return;
		}

		// Selenium Settings
		boolean REMOTE = c.getRUN_REMOTELY();
		String REMOTE_IP = c.getREMOTE_SERVER_ADDRESS();
		boolean EXIT_ON_FINISHED = c.getEXIT_ON_FINISHED();

		// Service Now Settings
		String instanceId = c.getINSTANCE_ID();

		WebDriver driver = REMOTE ? seleniumConfig.Browsers.chrome(REMOTE_IP) : seleniumConfig.Browsers.chrome();
		ServiceNow SN = new ServiceNow(driver, instanceId);

		try {
			SN.open();

			test_impersonation(SN);
			// test_tables(SN);
			// test_records(SN);
			// test_records_1(SN);

			// test_UTMOR_LR_01(SN);
			// test_UTMOR_GL_01(SN);

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		} finally {
			if (EXIT_ON_FINISHED)
				driver.quit();
		}

		assertTrue(true);
	}

	private void test_impersonation(ServiceNow SN) {
		System.out.println("[TEST] test_impersonation STARTED");

		User client = new User(SN);

		try {
			client.impersonate("dosreish-student");
			client.unimpersonate();
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}

		assertTrue(true);
		System.out.println("[TEST] test_impersonation OK");
	}

	private void test_tables(ServiceNow SN) {
		System.out.println("[TEST] test_tables STARTED");

		SNTable globalSettings = new SNTable(SN, "x_uotm_utmor_inter_student_global_setting");

		globalSettings.open();
		globalSettings.load();
		for (SNRecord r : globalSettings.getRecords())
			System.out.println(r);
		assertEquals(20, globalSettings.getRecords().size());

		globalSettings = new SNTable(SN, "x_uotm_utmor_inter_student_global_setting");

		globalSettings.open("u_application=General^u_name=utm_logo");
		globalSettings.load();
		assertEquals(1, globalSettings.getRecords().size());

		System.out.println("[TEST] test_tables OK");
	}

	private void test_records(ServiceNow SN) {
		System.out.println("[TEST] test_records STARTED");

		SNTable globalSettings = new SNTable(SN, "x_uotm_utmor_inter_student_global_setting");
		globalSettings.open("u_application=General^u_name=utm_logo");
		globalSettings.load();

		SNRecord utm_logo = globalSettings.getRecords().get(0);
		utm_logo.open();
		utm_logo.load();

		ArrayList<SNField> utm_logo_fields = utm_logo.getFields();
		try {
			utm_logo_fields.get(0).setValue("false");
			utm_logo_fields.get(1).setValue("latereg");
			utm_logo_fields.get(2).setValue("test");
			utm_logo_fields.get(3).setValue("hello");
			utm_logo_fields.get(4).setValue("world");
		} catch (InvalidFieldException e) {
		}

		System.out.println("[TEST] test_records OK");
	}

	private void test_records_1(ServiceNow SN) {
		System.out.println("[TEST] test_records STARTED");

		SNTable lateRegTable = new SNTable(SN, "x_uotm_utmor_lrg_late_reg");
		lateRegTable.open();

		FormHandler fh = SN.getFormHandler();
		fh.newRecord();

		System.out.println("[TEST] test_records OK");
	}

	private void test_UTMOR_LR_01(ServiceNow SN) {
		System.out.println("[TEST] test_UTMOR_LR_01 STARTED");

		FormHandler fh = SN.getFormHandler();
		fh.newRecord();

		// Pre
		SNTable stuGlobalSettings = new SNTable(SN, "x_uotm_utmor_inter_student_global_setting");

		// open_from
		stuGlobalSettings.open("u_application=latereg^u_name=open_from");
		stuGlobalSettings.load();
		SNRecord open_from = stuGlobalSettings.getRecords().get(stuGlobalSettings.getRecords().size() - 1);
		open_from.open();
		open_from.load();

		Instant yesterday = Instant.now().minus(1, ChronoUnit.DAYS);
		String s_yesterday = DateTimeFormatter.ofPattern("yyy-MM-dd hh:mm:ss").withZone(ZoneId.systemDefault())
				.format(yesterday);
		try {
			open_from.getFields().get(3).setValue(s_yesterday);
		} catch (serviceNow.SNField.InvalidFieldException e) {
		}
		fh.updateRecord();

		// close_after
		stuGlobalSettings.open("u_application=latereg^u_name=close_after");
		stuGlobalSettings.load();
		SNRecord close_after = stuGlobalSettings.getRecords().get(stuGlobalSettings.getRecords().size() - 1);
		close_after.open();
		close_after.load();

		Instant tomorow = Instant.now().plus(1, ChronoUnit.DAYS);
		String s_tomorow = DateTimeFormatter.ofPattern("yyy-MM-dd hh:mm:ss").withZone(ZoneId.systemDefault())
				.format(tomorow);
		try {
			close_after.getFields().get(3).setValue(s_tomorow);
		} catch (serviceNow.SNField.InvalidFieldException e) {
		}
		fh.updateRecord();

		// Test

		SNTable late_reg = new SNTable(SN, "x_uotm_utmor_lrg_late_reg");
		late_reg.open();
		fh.newRecord();
		
		SNRecord lr_record = new SNRecord(SN, "-1", late_reg);
		lr_record.load();
		
		// Restore
		
		// open_from
		open_from.open();
		try {
			open_from.getFields().get(3).revertValue();
		} catch (InvalidFieldException e) {
		}
		fh.updateRecord();

		// close_after
		close_after.open();
		try {
			close_after.getFields().get(3).revertValue();
		} catch (InvalidFieldException e) {
		}
		fh.updateRecord();

		System.out.println("[TEST] test_UTMOR_LR_01 OK");
	}

	private void test_UTMOR_GL_01(ServiceNow SN) {
		System.out.println("[TEST] test_UTMOR_GL_01 STARTED");

		User client = new User(SN);

		try {
			client.impersonate("t stude");
		} catch (Exception e) {
			e.printStackTrace();
			fail();
			return;
		}

		SN.getClient().goTo("/utm_askregistrar?id=sc_cat_item&sys_id=70190b7c4f23995009ea07a8f3ad4867");

		/*
		 * ArrayList<Field> first_fields = first.getFields(); try {
		 * first_fields.get(0).setValue("false"); } catch (InvalidFieldException e) { }
		 */

		System.out.println("[TEST] test_UTMOR_GL_01 OK");

	}
}
