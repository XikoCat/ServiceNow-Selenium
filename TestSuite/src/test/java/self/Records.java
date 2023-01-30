package self;

import java.util.ArrayList;

import main.TestTemplate;
import serviceNow.SNField;
import serviceNow.SNRecord;
import serviceNow.SNTable;
import serviceNow.ServiceNow;
import serviceNow.SNField.InvalidFieldException;

public class Records extends TestTemplate {

	public Records(ServiceNow sn) {
		super(sn);
	}

	@Override
	public boolean test() throws Exception {

		// Open table
		SNTable globalSettings = new SNTable(sn, "x_uotm_utmor_inter_student_global_setting");
		globalSettings.open();

		// Create a new record
		SNRecord temp = globalSettings.newRecord();
		temp.load();

		ArrayList<SNField> utm_logo_fields = temp.getFields();
		try {
			utm_logo_fields.get(0).setValue("false");
			utm_logo_fields.get(1).setValue("latereg");
			utm_logo_fields.get(2).setValue("test");
			utm_logo_fields.get(3).setValue("hello");
			utm_logo_fields.get(4).setValue("world");
		} catch (InvalidFieldException e) {
		}
		temp.save();

		globalSettings.open();
		temp.open();

		globalSettings.addRecord(temp);
		temp.delete();
		
		return true;
	}

	@Override
	public String name() {
		return "Records";
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
