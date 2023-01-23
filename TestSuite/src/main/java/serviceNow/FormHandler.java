package serviceNow;

import java.util.List;

import org.openqa.selenium.WebElement;

public class FormHandler {

	private ServiceNow sn;

	public FormHandler(ServiceNow SN) {
		this.sn = SN;
	}

	public void updateRecord() {
		ClickUIAction("Update");
	}

	public void submitRecord() {
		ClickUIAction("Submit");
	}

	public void newRecord() {
		ClickUIAction("New");
	}

	public void ClickUIAction(String IUAction) {
		// Enter IFrame
		sn.driver.switchTo().frame(sn.shadow.findElement("iframe#gsft_main"));

		List<WebElement> UIActions = sn.shadow.findElements("button.action_context[type='submit']");

		for (WebElement e : UIActions)
			if (e.getText().contains(IUAction)) {
				e.click();
				break;
			}

		// Exit IFrame
		sn.driver.switchTo().parentFrame();

	}

}
