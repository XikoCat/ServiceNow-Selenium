package serviceNow;

import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

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

	public void save() {
		// Enter IFrame
		sn.driver.switchTo().frame(sn.shadow.findElement("iframe#gsft_main"));

		String additionalActionsMontextMenuButtonCSS = "button.additional-actions-context-menu-button";
		WebElement button = sn.shadow.findElement(additionalActionsMontextMenuButtonCSS);
		sn.wait.until(ExpectedConditions.elementToBeClickable(button)).click();

		String contextMenuSaveCSS = "div#context_1 > div:nth-child(2)";
		WebElement menuItem = sn.shadow.findElement(contextMenuSaveCSS);
		sn.wait.until(ExpectedConditions.elementToBeClickable(menuItem)).click();

		// Exit IFrame
		sn.driver.switchTo().parentFrame();
	}

}
