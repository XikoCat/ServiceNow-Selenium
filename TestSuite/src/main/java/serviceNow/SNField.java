package serviceNow;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class SNField {

	// Init Variables
	private String name;
	private SNRecord record;

	// Load Variables
	private String type;
	private String initial_value;
	private String value;
	private String display_value;
	private String display_name;
	private boolean readOnly;

	// Selenium Variables
	private ServiceNow sn;

	public SNField(ServiceNow serviceNow, String name, SNRecord record) {
		this.sn = serviceNow;
		this.name = name;
		this.record = record;
	}

	/**
	 * Loads all variables to memory
	 * 
	 * @throws InvalidFieldException
	 */
	public void load() throws InvalidFieldException {
		String tableName = record.getTable().getName();
		String fieldCSS = "#element\\." + tableName + "\\." + name;
		String fieldTypeCSS = fieldCSS + " > div:first-child";

		// If driver is not on the correct IFRAME, switch to it.
		try {
			sn.driver.findElement(By.cssSelector(fieldCSS));
		} catch (Exception e) {
			sn.driver.switchTo().frame(sn.shadow.findElement("iframe#gsft_main"));
		}

		this.type = sn.driver.findElement(By.cssSelector(fieldTypeCSS)).getAttribute("type");

		switch (type) {
		case "boolean":
			this.value = new Field_Boolean().read(fieldCSS);
			break;
		case "choice":
			this.value = new Field_Choice().read(fieldCSS);
			break;
		case "string":
			this.value = new Field_String().read(fieldCSS);
			break;
		case "reference":
			this.value = new Field_Reference().read(fieldCSS);
			break;
		default:
			throw new InvalidFieldException();
		}

		this.initial_value = this.value;

		System.out.println("Loaded value '" + this.value + "' for field '" + name + "'");
	}

	public void setValue(String new_value) throws InvalidFieldException {

		String tableName = record.getTable().getName();
		String fieldCSS = "#element\\." + tableName + "\\." + name;
		String fieldTypeCSS = fieldCSS + " > div:first-child";

		// If driver is not on the correct IFRAME, switch to it.
		try {
			sn.driver.findElement(By.cssSelector(fieldCSS));
		} catch (Exception e) {
			sn.driver.switchTo().frame(sn.shadow.findElement("iframe#gsft_main"));
		}

		this.type = sn.driver.findElement(By.cssSelector(fieldTypeCSS)).getAttribute("type");

		switch (type) {
		case "boolean":
			new Field_Boolean().write(fieldCSS, new_value);
			break;
		case "choice":
			new Field_Choice().write(fieldCSS, new_value);
			break;
		case "string":
			new Field_String().write(fieldCSS, new_value);
			break;
		case "reference":
			new Field_Reference().write(fieldCSS, new_value);
			break;
		default:
			throw new InvalidFieldException();
		}

		// Exit to parent frame
		sn.driver.switchTo().parentFrame();

		this.value = new_value;
		System.out.println("Updated value to '" + new_value + "' on field '" + name + "'");
	}

	public void revertValue() throws InvalidFieldException {
		this.setValue(initial_value);
	}
	
	public String getType() {
		return this.type;
	}

	public String getValue() {
		return this.value;
	}

	public String getDisplayValue() {
		return this.display_value;
	}

	public String toString() {
		return this.name;
	}

	private interface FieldBase {
		public String read(String fieldLocationCSS);

		public void write(String fieldLocationCSS, String new_value);
	}

	class Field_Boolean implements FieldBase {

		@Override
		public String read(String fieldLocationCSS) {
			return sn.driver.findElement(By.cssSelector(fieldLocationCSS + " > .input_controls input"))
					.getAttribute("value");
		}

		@Override
		public void write(String fieldLocationCSS, String new_value) {
			WebElement box = sn.driver
					.findElement(By.cssSelector(fieldLocationCSS + " > .input_controls > span > label"));
			WebElement display = sn.driver.findElement(By.cssSelector(fieldLocationCSS + " > .input_controls > input"));
			if (!new_value.contains(display.getAttribute("value")))
				sn.wait.until(ExpectedConditions.elementToBeClickable(box)).click();
		}

	}

	class Field_Choice implements FieldBase {

		@Override
		public String read(String fieldLocationCSS) {
			return sn.driver
					.findElement(By
							.cssSelector(fieldLocationCSS + " > .input_controls select option[selected=\"SELECTED\"]"))
					.getAttribute("value");
		}

		@Override
		public void write(String fieldLocationCSS, String new_value) {
			String selectBoxCSS = fieldLocationCSS + " > .input_controls > select";
			String optionsCSS = selectBoxCSS + " > option";
			int selected = -1;
			int to_select = -1;

			List<WebElement> options = sn.driver.findElements(By.cssSelector(optionsCSS));
			for (int i = 0; i < options.size(); i++) {
				WebElement option = options.get(i);

				if (option.getAttribute("selected") != null)
					selected = i;

				if (option.getAttribute("value").contains(new_value))
					to_select = i;
			}

			if (to_select == -1)
				return;
			if (selected == to_select)
				return;

			int offset = Math.abs(to_select - selected);
			Keys key = to_select > selected ? Keys.ARROW_DOWN : Keys.ARROW_UP;

			WebElement selectBox = sn.driver.findElement(By.cssSelector(selectBoxCSS));
			sn.wait.until(ExpectedConditions.elementToBeClickable(selectBox));
			selectBox.click();

			while (offset > 0) {
				selectBox.sendKeys(key);
				offset--;
			}

			selectBox.sendKeys(Keys.ENTER);

		}

	}

	class Field_String implements FieldBase {

		@Override
		public String read(String fieldLocationCSS) {
			return sn.driver.findElement(By.cssSelector(fieldLocationCSS + " > .input_controls > input"))
					.getAttribute("value");
		}

		@Override
		public void write(String fieldLocationCSS, String new_value) {
			String textBoxCSS = fieldLocationCSS + " > .input_controls > *:nth-child(2)";
			WebElement textBox = sn.driver.findElement(By.cssSelector(textBoxCSS));
			textBox.clear();
			textBox.sendKeys(new_value);
		}

	}

	class Field_Reference implements FieldBase {

		@Override
		public String read(String fieldLocationCSS) {
			return sn.driver.findElement(By.cssSelector(fieldLocationCSS + " > .input_controls > input")).getAttribute("value");
					
		}

		@Override
		public void write(String fieldLocationCSS, String new_value) {
			// TODO Auto-generated method stub
			
		}
	}
	
	// Exceptions
	public class InvalidFieldException extends Exception {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

	}
}
