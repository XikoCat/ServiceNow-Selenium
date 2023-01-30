package self;

import main.TestTemplate;
import serviceNow.SNField;
import serviceNow.SNRecord;
import serviceNow.SNTable;
import serviceNow.ServiceNow;

public class Fields extends TestTemplate {

	public Fields(ServiceNow sn) {
		super(sn);
	}

	@Override
	public boolean preTest() throws Exception {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean test() throws Exception {
		SNTable table = new SNTable(sn, "x_uotm_utmor_lrg_late_reg");
		table.open();
		table.load();
		SNRecord record = table.getRecords().get(0);
		record.open();
		record.load();

		for (SNField field : record.getFields()) {
			if (field.toString().contains("comments"))
				field.setValue("Did it work???");
		}
		
		utils.DebuggingTool.pause();
		return true;
	}

	@Override
	public boolean postTest() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public String name() {
		return "Fields";
	}

}
