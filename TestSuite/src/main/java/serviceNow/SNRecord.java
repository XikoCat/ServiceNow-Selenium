package serviceNow;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import serviceNow.SNField.InvalidFieldException;

public class SNRecord {

	private String sys_id;
	private SNTable table;
	private ArrayList<SNField> fields = new ArrayList<SNField>();

	// Selenium Variables
	private ServiceNow sn;

	public SNRecord(ServiceNow serviceNow, String sys_id, SNTable table) {
		this.sn = serviceNow;
		this.sys_id = sys_id;
		this.table = table;
	}

	/**
	 * Opens the record on the browser
	 */
	public void open() {
		System.out.println("Opening record " + sys_id + " from table '" + table.getName() + "'");

		sn.driver.get(sn.instance.getURLTarget() + table.getName() + ".do%3Fsys_id%3D" + sys_id);
	}

	/**
	 * Loads all values into memory, populates the fields array
	 */
	public void load() {
		String FieldElementsCSS = "form div[id^='element\\." + table.getName() + "\\.']";

		sn.driver.switchTo().frame(sn.shadow.findElement("iframe#gsft_main"));

		List<WebElement> fieldElements = sn.driver.findElements(By.cssSelector(FieldElementsCSS));

		for (WebElement element : fieldElements) {
			String name = element.getAttribute("id").split("\\.")[2];
			SNField field = new SNField(sn, name, this);
			try {
				field.load();
			} catch (InvalidFieldException e) {
				System.out.println("Field '" + name + "' could not be loaded");
			}
			fields.add(field);
		}

		sn.driver.switchTo().parentFrame();

		System.out.println("Loaded " + fieldElements.size() + " fields from record '" + this + "'");
	}

	public ArrayList<SNField> getFields() {
		return this.fields;
	}

	public SNTable getTable() {
		return this.table;
	}

	public String toString() {
		return this.sys_id;
	}

	public void submit() {
		sn.formHandler.submitRecord();
	}

	public void save() {
		sn.formHandler.save();

		String url = sn.driver.getCurrentUrl();

		String sys_id_locator = "sys_id%3D";
		int sys_id_start = url.indexOf(sys_id_locator) + sys_id_locator.length();
		int sys_id_end = url.indexOf("%", sys_id_start + sys_id_locator.length());

		this.sys_id = url.substring(sys_id_start, sys_id_end);
		System.out.println(this.sys_id);
	}

	public void update() {
		sn.formHandler.ClickUIAction("Update");
	}

	public void delete() {
		sn.formHandler.ClickUIAction("Delete");

		// Enter IFrame
		sn.driver.switchTo().frame(sn.shadow.findElement("iframe#gsft_main"));

		String confirmDeleteCSS = "#delete_confirm_form button#ok_button";
		WebElement button = sn.shadow.findElement(confirmDeleteCSS);
		sn.wait.until(ExpectedConditions.elementToBeClickable(button)).click();
		
		// Exit IFrame
		sn.driver.switchTo().parentFrame();
		
	}

}
