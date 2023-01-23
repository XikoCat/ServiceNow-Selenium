package serviceNow;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

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
		String FieldElementsCSS = "div[id^='element\\." + table.getName() + "\\.']";

		try {
			sn.shadow.findElement(FieldElementsCSS + ":first-of-type");
		} catch (Exception e) {
			sn.driver.switchTo().frame(sn.shadow.findElement("iframe#gsft_main"));
			sn.shadow.findElement(FieldElementsCSS + ":first-of-type");
		}

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

}
