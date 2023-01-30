package main;

import java.util.HashMap;

import org.openqa.selenium.WebDriver;

import serviceNow.ServiceNow;
import utils.FileHandler;

public abstract class TestTemplate {

	protected ServiceNow sn;
	
	public TestTemplate(ServiceNow sn) {
		this.sn = sn;
	}
	
	public HashMap<String, Object> vars = new HashMap<String, Object>();

	public abstract boolean preTest() throws Exception;

	public abstract boolean test() throws Exception;

	public abstract boolean postTest();

	public abstract String name();

	public static void saveSnapshot(Exception e, WebDriver driver, String name) {
		// Save error to a file
		FileHandler.createFile(name + ".txt", e.getMessage() + "\n" + e.getStackTrace());

		// Save screenshot to a file
		seleniumConfig.Screenshot.takeScreenshot(driver, name + ".png");

		// Save page source to a file
		FileHandler.createFile(name + ".html", driver.getPageSource());

	}
}
