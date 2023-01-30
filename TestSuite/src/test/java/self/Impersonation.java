package self;

import main.TestTemplate;
import serviceNow.ServiceNow;

public class Impersonation extends TestTemplate {

	public Impersonation(ServiceNow sn) {
		super(sn);
	}

	@Override
	public boolean test() throws Exception {

		sn.getUser().impersonate("dosreish-student");
		sn.getUser().unimpersonate();
		
		return true;
	}
	
	@Override
	public String name() {
		return "Impersonation";
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
