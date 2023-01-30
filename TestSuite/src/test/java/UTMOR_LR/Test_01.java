package UTMOR_LR;

import java.io.File;
import java.time.Clock;
import java.time.Instant;
import java.time.Year;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;

import main.TestTemplate;
import serviceNow.SNField;
import serviceNow.SNField.InvalidFieldException;
import serviceNow.SNRecord;
import serviceNow.SNTable;
import serviceNow.ServiceNow;

public class Test_01 extends TestTemplate {

	public Test_01(ServiceNow sn) {
		super(sn);
	}

	@Override
	public boolean test() throws Exception {
		
		sn.getUser().impersonate("t studen");
		fillOutForm();
		checkoutPayment();

		sn.getUser().unimpersonate();
		sn.getUser().impersonate("Olha Fihol");
		approveRequest();

		return true;
	}

	@Override
	public String name() {
		return "LR_Test_01";
	}

	@Override
	public boolean preTest() throws Exception {
		SNTable stuGlobalSettings = new SNTable(sn, "x_uotm_utmor_inter_student_global_setting");
		vars.put("stuGlobalSettings", stuGlobalSettings);

		// open_from
		stuGlobalSettings.open("u_application=latereg^u_name=open_from");
		stuGlobalSettings.load();
		SNRecord open_from = stuGlobalSettings.getRecords().get(stuGlobalSettings.getRecords().size() - 1);
		vars.put("open_from", open_from);
		open_from.open();
		open_from.load();

		Instant yesterday = Instant.now().minus(1, ChronoUnit.DAYS);
		String s_yesterday = DateTimeFormatter.ofPattern("yyy-MM-dd hh:mm:ss").withZone(ZoneId.systemDefault())
				.format(yesterday);
		try {
			open_from.getFields().get(3).setValue(s_yesterday);
		} catch (serviceNow.SNField.InvalidFieldException e) {
		}
		open_from.update();

		// close_after
		stuGlobalSettings.open("u_application=latereg^u_name=close_after");
		stuGlobalSettings.load();
		SNRecord close_after = stuGlobalSettings.getRecords().get(stuGlobalSettings.getRecords().size() - 1);
		vars.put("close_after", close_after);
		close_after.open();
		close_after.load();

		Instant tomorow = Instant.now().plus(1, ChronoUnit.DAYS);
		String s_tomorow = DateTimeFormatter.ofPattern("yyy-MM-dd hh:mm:ss").withZone(ZoneId.systemDefault())
				.format(tomorow);
		try {
			close_after.getFields().get(3).setValue(s_tomorow);
		} catch (serviceNow.SNField.InvalidFieldException e) {
		}
		close_after.update();

		// open_flag
		stuGlobalSettings.open("u_application=latereg^u_name=open_flag");
		stuGlobalSettings.load();
		SNRecord open_flag = stuGlobalSettings.getRecords().get(stuGlobalSettings.getRecords().size() - 1);
		vars.put("open_flag", open_flag);
		open_flag.open();
		open_flag.load();

		try {
			open_flag.getFields().get(3).setValue("1");
		} catch (InvalidFieldException e2) {
			e2.printStackTrace();
		}
		open_flag.update();

		// Cancel any Late Reg for "t studen"
		SNTable lateRegTable = new SNTable(sn, "x_uotm_utmor_lrg_late_reg");
		lateRegTable.open("x_fru_lift_commerc_requested_by.name=t studen^active=true");
		lateRegTable.load();
		if (lateRegTable.getRecords().size() > 0) {
			SNRecord rec = lateRegTable.getRecords().get(0);
			rec.open();
			rec.delete();
		}

		return true;
	}

	@Override
	public boolean postTest() {
		SNRecord open_from = (SNRecord) vars.get("open_from");
		SNRecord close_after = (SNRecord) vars.get("close_after");
		SNRecord open_flag = (SNRecord) vars.get("open_flag");

		sn.getUser().unimpersonate();
		// Restore

		// open_from
		open_from.open();
		open_from.getFields().get(3).revertValue();
		open_from.update();

		// close_after
		close_after.open();
		close_after.getFields().get(3).revertValue();
		open_from.update();

		// open_flag
		open_flag.open();
		open_flag.getFields().get(3).revertValue();
		open_flag.update();
		return true;
	}

	private void fillOutForm() throws Exception {
		sn.getInstance().goTo("/utm_askregistrar?id=sc_cat_item&sys_id=f5a3e07187bbc15009eaed3e8bbb35ec");

		WebElement check2 = sn.getShadow().findElement("#check2");
		sn.getWait().until(ExpectedConditions.elementToBeClickable(check2)).click();
		// I acknowledge that I have read ...
		// Select 'Yes'
		new Actions(sn.getDriver()).sendKeys(Keys.ARROW_DOWN).sendKeys(Keys.ENTER).perform();

		// go to next input
		new Actions(sn.getDriver()).sendKeys(Keys.TAB).sendKeys(Keys.ENTER).perform();
		// I am eligible to register ...
		// Select 'Yes'
		new Actions(sn.getDriver()).sendKeys(Keys.ARROW_DOWN).sendKeys(Keys.ENTER).perform();

		// go to next input
		new Actions(sn.getDriver()).sendKeys(Keys.TAB).sendKeys(Keys.ENTER).perform();

		// I have been away from my studies ...
		// Select 'No'
		new Actions(sn.getDriver()).sendKeys(Keys.ARROW_DOWN).sendKeys(Keys.ARROW_DOWN).sendKeys(Keys.ENTER).perform();

		// go to next input
		new Actions(sn.getDriver()).sendKeys(Keys.TAB).sendKeys(Keys.ENTER).perform();

		// I understand that I am financially responsible...
		// Select 'Yes'
		new Actions(sn.getDriver()).sendKeys(Keys.ARROW_DOWN).sendKeys(Keys.ENTER).perform();

		// Add attachment
		File img = new File("src/main/resources/images/TEST_IMAGE.png");
		String path = img.getAbsolutePath();
		WebElement attInput = sn.getShadow().findElement("input.sp-attachments-input");
		attInput.sendKeys(path);

		// Wait for attachment to be uploaded
		sn.sleep(3000);
		// sn.getWait().until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".attachment-box")));

		WebElement submitButton = sn.getDriver().findElement(By.cssSelector("button#submit-btn"));
		sn.getWait().until(ExpectedConditions.elementToBeClickable(submitButton)).click();
	}

	private void checkoutPayment() {
		sn.getWait().until(ExpectedConditions.urlContains("id=ticket"));

		// Click on Payment Tab
		String paymentTabMenuCSS = "a#Payment";
		WebElement paymentTabMenu = sn.getShadow().findElement(paymentTabMenuCSS);
		sn.getWait().until(ExpectedConditions.elementToBeClickable(paymentTabMenu)).click();

		// Press the checkout button
		String checkoutButtonCSS = "a[ng-if*='checkout']";
		WebElement checkoutButton = sn.getShadow().findElement(checkoutButtonCSS);
		sn.getWait().until(ExpectedConditions.elementToBeClickable(checkoutButton)).click();

		// Moneris Checkout Widget
		sn.getWait().until(ExpectedConditions.urlContains("id=moneris_checkout"));

		// Enter Iframe
		sn.getDriver().switchTo().frame(sn.getShadow().findElement("iframe#monerisCheckout-Frame"));

		// Enter Cardholder Name
		String nameCSS = "input#cardholder";
		WebElement nameInput = sn.getWait().until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(nameCSS)));
		nameInput.sendKeys("Selenium Bot");

		// Enter Card Number
		String numberCSS = "input#pan";
		WebElement numberInput = sn.getDriver().findElement(By.cssSelector(numberCSS));
		numberInput.sendKeys("5454545454545454");

		// Enter Card expiry date
		String expiryCSS = "input#expiry_date";
		WebElement expiryInput = sn.getDriver().findElement(By.cssSelector(expiryCSS));
		String date = "12" + Year.now(Clock.systemUTC()).toString().substring(2);
		expiryInput.sendKeys(date);

		// Enter Card CVV
		String cvvCSS = "input#cvv";
		WebElement cvvInput = sn.getDriver().findElement(By.cssSelector(cvvCSS));
		cvvInput.sendKeys("100");

		sn.sleep(1000);

		// Submit Checkout
		String submitCheckoutCSS = "input#process";
		WebElement submitCheckout = sn.getDriver().findElement(By.cssSelector(submitCheckoutCSS));
		sn.getWait().until(ExpectedConditions.elementToBeClickable(submitCheckout)).click();
		sn.sleep(1000);
		if(ExpectedConditions.presenceOfElementLocated(By.cssSelector(submitCheckoutCSS)) != null)
			sn.getWait().until(ExpectedConditions.elementToBeClickable(submitCheckout)).click();		
		
		// Submit Checkout
		String backButtonCSS = "#back_button";
		WebElement backButton = sn.getDriver().findElement(By.cssSelector(backButtonCSS));
		sn.getWait().until(ExpectedConditions.elementToBeClickable(backButton)).click();

		// Exit Iframe
		sn.getDriver().switchTo().parentFrame();
	}

	private void approveRequest() throws InvalidFieldException {
		SNTable lateRegTable = new SNTable(sn, "x_uotm_utmor_lrg_late_reg");
		lateRegTable.open("state=1^active=true^x_fru_lift_commerc_requested_by.name=t studen");
		lateRegTable.load();

		SNRecord record = lateRegTable.getRecords().get(0);
		record.open();
		record.load();

		List<SNField> fields = record.getFields();
		for (SNField field : fields) {
			System.out.println(field);
			if (field.toString().contains("assigned_to"))
				field.setValue("Olha Fihol");
			if (field.toString().contains("comments"))
				field.setValue("Test");
		}

		sn.getFormHandler().ClickUIAction("Complete Request");
	}
}
