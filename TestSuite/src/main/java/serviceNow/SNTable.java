package serviceNow;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebElement;

import utils.Encoding;

public class SNTable {

	private String name;
	private String display_name;
	private ArrayList<SNRecord> records = new ArrayList<SNRecord>();

	// Selenium Variables
	private ServiceNow sn;

	public SNTable(ServiceNow serviceNow, String table_name) {
		this.sn = serviceNow;
		this.name = table_name;
	}

	public void open() {
		System.out.println("Opening table '" + this.name + "'");
		sn.driver.get(sn.instance.getURLTarget() + name + "_list.do");
	}

	public void open(String encodedQuery) {
		System.out.println("Opening table '" + this.name + "' with query '" + encodedQuery + "'");
		sn.driver.get(sn.instance.getURLTarget() + name + "_list.do%3Fsysparm_query%3D"
				+ Encoding.encode(encodedQuery, Encoding.ENCODED_QUERY_TO_URL));
	}

	/**
	 * Loads the current open list of records to memory.
	 */
	public void load() {
		String RecordElementsCSS = "table#" + name + "_table > tbody > tr";

		sn.driver.switchTo().frame(sn.shadow.findElement("iframe#gsft_main"));
		try {
			sn.shadow.findElement(RecordElementsCSS + ":first-of-type");
		} catch (Exception e) {
			sn.driver.switchTo().parentFrame();
			return;
		}

		List<WebElement> recordElements = sn.shadow.findElements(RecordElementsCSS);

		for (WebElement rec : recordElements) {
			String sys_id = rec.getAttribute("sys_id");
			SNRecord record = new SNRecord(sn, sys_id, this);
			records.add(record);
		}

		sn.driver.switchTo().parentFrame();
		System.out.println("Loaded " + recordElements.size() + " records from table '" + this.name + "'");
	}

	public String getName() {
		return this.name;
	}

	public String getDisplayName() {
		return this.display_name;
	}

	public ArrayList<SNRecord> getRecords() {
		return this.records;
	}

	public void addRecord(SNRecord record) {
		this.records.add(record);
	}

	public SNRecord newRecord() {
		sn.formHandler.newRecord();
		SNRecord r = new SNRecord(sn, "-1", this);
		return r;
	}

}
