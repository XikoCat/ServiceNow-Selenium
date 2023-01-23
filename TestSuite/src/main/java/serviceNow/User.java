package serviceNow;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class User {

	private ServiceNow sn;

	public User(ServiceNow serviceNow) {
		this.sn = serviceNow;
	}

	public void impersonate(String userId) throws InterruptedException {
		System.out.println("Impersonating user: " + userId);

		openUserMenu();

		// Click the "Impersonate" menu item
		WebElement userMenuButton = sn.shadow.findElement("button.user-menu-button.impersonateUser");
		sn.wait.until(ExpectedConditions.elementToBeClickable(userMenuButton));
		userMenuButton.click();

		// Type user on Impersonate a User Input box
		WebElement usernameField = sn.shadow.findElement("now-popover div.now-typeahead-field.now-form-field input");
		usernameField.sendKeys(userId);

		Thread.sleep(3000);

		// Select the first result
		usernameField.sendKeys(Keys.ARROW_DOWN);
		usernameField.sendKeys(Keys.ENTER);

		// Click the "Impersonate" button
		WebElement impersonateButton = sn.shadow.findElement("now-button.now-modal-footer-button:nth-child(2)");
		impersonateButton.click();

		// Wait for the portal page to load
		sn.wait.until(ExpectedConditions.titleIs("ServiceNow"));
		Thread.sleep(3000);
	}

	public void unimpersonate() throws InterruptedException {
		System.out.println("Unimpersonating user");

		openUserMenu();
		
		// Click the "Unimpersonate" menu item
		WebElement userMenuButton = sn.shadow.findElement("button.user-menu-button.unimpersonate");
		sn.wait.until(ExpectedConditions.elementToBeClickable(userMenuButton));
		userMenuButton.click();

		// Wait for the portal page to load
		sn.wait.until(ExpectedConditions.titleIs("ServiceNow"));
		Thread.sleep(3000);
	}
	
	private void openUserMenu() {
		String userButtonCSS = ".header-avatar-button.contextual-zone-button.user-menu";
		WebElement userButton = sn.shadow.findElement(userButtonCSS);
		sn.wait.until(ExpectedConditions.elementToBeClickable(userButton)).click();

		// Check if the menu has opened
		if (userButton.getAttribute("aria-expanded") == "false") {
			// Try clicking again if needed
			userButton = sn.shadow.findElement(userButtonCSS);
			sn.wait.until(ExpectedConditions.elementToBeClickable(userButton)).click();
		}
	}

}
