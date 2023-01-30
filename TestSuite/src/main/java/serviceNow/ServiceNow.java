package serviceNow;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.sukgu.Shadow;

public class ServiceNow {

	protected WebDriver driver;
	protected WebDriverWait wait;
	protected Shadow shadow;

	protected FormHandler formHandler;
	protected SNInstance instance;
	protected SNUser user;

	public ServiceNow(WebDriver driver, String instanceId) {
		this.driver = driver;
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(60));
		this.shadow = new Shadow(driver);
		try {
			// will wait for maximum 30 secs and will check after every 1 secs.
			shadow.setExplicitWait(30, 1);
		} catch (Exception e) {
			e.printStackTrace();
		}

		this.instance = new SNInstance(this, instanceId);
		this.formHandler = new FormHandler(this);
		this.user = new SNUser(this);
	}

	public void open() {
		instance.open();
	}

	public SNInstance getInstance() {
		return instance;
	}

	public WebDriver getDriver() {
		return driver;
	}

	public Shadow getShadow() {
		return shadow;
	}

	public WebDriverWait getWait() {
		return wait;
	}

	public FormHandler getFormHandler() {
		return formHandler;
	}

	public SNUser getUser() {
		return this.user;
	}

	public void sleep(long milis) {
		try {
			Thread.sleep(milis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
