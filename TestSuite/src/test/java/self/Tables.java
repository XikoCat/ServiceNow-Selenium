package self;

import static org.junit.Assert.assertEquals;

import main.TestTemplate;
import serviceNow.SNRecord;
import serviceNow.SNTable;
import serviceNow.ServiceNow;

public class Tables extends TestTemplate {

	public Tables(ServiceNow sn) {
		super(sn);
	}

	@Override
	public boolean test() throws Exception {

		SNTable globalSettings = new SNTable(sn, "x_uotm_utmor_inter_student_global_setting");

		globalSettings.open();
		globalSettings.load();
		for (SNRecord r : globalSettings.getRecords())
			System.out.println(r);
		assertEquals(20, globalSettings.getRecords().size());

		globalSettings = new SNTable(sn, "x_uotm_utmor_inter_student_global_setting");

		globalSettings.open("u_application=General^u_name=utm_logo");
		globalSettings.load();
		assertEquals(1, globalSettings.getRecords().size());
		return true;
	}

	@Override
	public String name() {
		return "self_Tables";
	}

	@Override
	public boolean preTest() throws Exception {
		return true;
	}

	@Override
	public boolean postTest() {
		return true;
	}

}
