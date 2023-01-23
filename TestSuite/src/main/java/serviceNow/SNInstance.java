package serviceNow;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

import utils.Secrets;

public class SNInstance {
	private static String URL_PROTOCOL = "https://";
	private static String URL_SN = ".service-now.com";
	private static String URL_TARGET = "/now/nav/ui/classic/params/target/";

	private String instanceId;

	// Selenium Variables
	private ServiceNow sn;

	public SNInstance(ServiceNow serviceNow, String instanceId) {
		this.sn = serviceNow;
		this.instanceId = instanceId;
		// TODO Auto-generated constructor stub
	}

	/**
	 * Opens the Service Now now portal. May require authentication.
	 * 
	 * @throws InterruptedException
	 */
	public void open() throws InterruptedException {
		// Open Service Now's Now Portal
		sn.driver.get(URL_PROTOCOL + instanceId + URL_SN);
		System.out.println(sn.driver.getWindowHandle());

		// Login
		try {
			Secrets s = new Secrets();
			String utorid = s.getUTORID();
			if (utorid != null)
				if (utorid.length() > 0)
					sn.driver.findElement(By.id("username")).sendKeys(utorid);

			String utorpass = s.getUTORPASS();
			if (utorpass != null)
				if (utorpass.length() > 0)
					sn.driver.findElement(By.id("password")).sendKeys(utorpass);

			sn.driver.findElement(By.xpath("/html/body/div/div/div[1]/div[2]/form/button")).click();
		} finally {
			// Wait for manual Login
			Boolean loginCompleted = sn.wait.until(ExpectedConditions.urlContains(".service-now.com/"));
			if (loginCompleted)
				System.out.println("Login Success");
			else
				System.out.println("Login Timeout");
		}

		// Wait for the page title to be "ServiceNow" meaning the page is loaded
		sn.wait.until(ExpectedConditions.titleContains("ServiceNow"));

		Thread.sleep(3000);
		System.out.println("Page Loaded");
	}

	public void goTo(String arg) {
		sn.driver.get(URL_PROTOCOL + instanceId + URL_SN + arg);
	}
	
	public String getURL() {
		return URL_PROTOCOL + instanceId + URL_SN;
	}

	public String getURLTarget() {
		return this.getURL() + URL_TARGET;
	}

}
