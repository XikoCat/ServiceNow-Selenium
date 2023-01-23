package seleniumConfig;

import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;

import io.github.bonigarcia.wdm.WebDriverManager;

public class Browsers {

	public static WebDriver firefox() {
		return firefox(null);
	}

	public static WebDriver firefox(String remoteIp) {
		WebDriverManager.firefoxdriver().setup();
		FirefoxOptions options = new FirefoxOptions();

		try {
			if (remoteIp == null)
				return new FirefoxDriver(options);
			else
				return new RemoteWebDriver(new URL(remoteIp), options);
		} catch (WebDriverException e) {
			System.out.println("Skipping Firefox. It might not be installed on this System");
			e.printStackTrace();
		} catch (MalformedURLException e) {
			System.out.println("Invalid Remote URL");
			e.printStackTrace();
		}
		return null;
	}

	public static WebDriver chrome() {
		return chrome(null);
	}

	public static WebDriver chrome(String remoteIp) {
		WebDriverManager.chromedriver().setup();
		ChromeOptions options = new ChromeOptions();

		try {
			if (remoteIp == null)
				return new ChromeDriver(options);
			else
				return new RemoteWebDriver(new URL(remoteIp), options);
		} catch (WebDriverException e) {
			System.out.println("Skipping Chrome. It might not be installed on this System");
			e.printStackTrace();
		} catch (MalformedURLException e) {
			System.out.println("Invalid Remote URL");
			e.printStackTrace();
		}
		return null;
	}

	public static WebDriver edge() {
		return edge(null);
	}

	public static WebDriver edge(String remoteIp) {
		WebDriverManager.edgedriver().setup();

		EdgeOptions options = new EdgeOptions();

		try {
			if (remoteIp == null)
				return new EdgeDriver(options);
			else
				return new RemoteWebDriver(new URL(remoteIp), options);
		} catch (WebDriverException e) {
			System.out.println("Skipping Edge. It might not be installed on this System");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static WebDriver safari() {
		return safari(null);
	}
	
	public static WebDriver safari(String remoteIp) {
		WebDriverManager.safaridriver().setup();

		SafariOptions options = new SafariOptions();

		try {
			if (remoteIp == null)
				return new SafariDriver(options);
			else
				return new RemoteWebDriver(new URL(remoteIp), options);
		} catch (WebDriverException e) {
			System.out.println("Skipping Safari. It might not be installed on this System");
			e.printStackTrace();
		} catch (MalformedURLException e) {
			System.out.println("Invalid Remote URL");
			e.printStackTrace();
		}
		return null;
	}
	
	public static WebDriver internetExplorer() {
		return internetExplorer(null);
	}
	
	public static WebDriver internetExplorer(String remoteIp) {
		WebDriverManager.iedriver().setup();

		InternetExplorerOptions options = new InternetExplorerOptions();

		try {
			if (remoteIp == null)
				return new InternetExplorerDriver(options);
			else
				return new RemoteWebDriver(new URL(remoteIp), options);
		} catch (WebDriverException e) {
			System.out.println("Skipping InternetExplorer. It might not be installed on this System");
			e.printStackTrace();
		} catch (MalformedURLException e) {
			System.out.println("Invalid Remote URL");
			e.printStackTrace();
		}
		return null;
	}

}
