package stepDefinition;

import java.io.File;
import java.net.MalformedURLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.xml.DOMConfigurator;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import com.swaglabs.AppLaunchCapabilities.AppiumController;
import com.swaglabs.AppLaunchCapabilities.AppiumHelper;
import com.swaglabs.commonfunctions.CommonFunctions;
import com.swaglabs.config.Config;
import com.swaglabs.utilities.Log;
import com.swaglabs.utilities.Reporter;
import com.swaglabs.utilities.XlsReader;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.AfterStep;
import cucumber.api.java.Before;
import io.qameta.allure.Allure;

public class Setup {

	public static XlsReader XLR = null;
	public static Scenario scenario = null;
	public static Properties CONFIG, CONSTANTS;
	static CommonFunctions com_fun = null;
	protected static String appUrl;
	protected static String otpUrl, pfUrl, btUrl;
	public static Map<String, String> dbDetails;
	protected static boolean uninstallRequired = false;
	public static int screenshotCount = 1, rowCount = 2;
	public static String directoryPath = null;
	public static String deviceStatus = "NORMAL";
	public AppiumController appiumManager;
	public static String mobileNoApproved;

	@SuppressWarnings("static-access")
	@Before
	public void beforeMethod(Scenario scenario) throws Exception {
		appiumManager = new AppiumController();

		DOMConfigurator.configure("log4j.xml");

		// getting instance of scenario
		this.scenario = scenario;
		Log.startTestCase(scenario.getName());
		// Initialize Configurations
		CONFIG = Config.initializeConfig();
		if (!scenario.getName().contains("API")) {
			try {
				AppiumController.instance.appLaunch(CONFIG);
				AppiumHelper.driverInit().manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		com_fun = new CommonFunctions(AppiumHelper.driverInit());
		directoryPath = com_fun.directoryCreation(CONFIG);
		if (scenario.getSourceTagNames().contains("@SanityBPMApproved")) {
			mobileNoApproved = XLR.getCellData("CustomerDetails", "MOBILE NUMBER", rowCount++);
		}
	}

	@After
	public void afterMethod(Scenario scenario) {
		if (scenario.isFailed()) {
			Log.info("Test Case failed: " + scenario.getName());
			try {
				if (!scenario.getName().contains("API")) {
					Reporter.wordReportCreator(scenario, directoryPath, 1, "");
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			try {
				if (!scenario.getName().contains("API")) {
//                    Reporter.wordReportCreator(scenario, directoryPath, 1,"");
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		AppiumController.instance.driver = null;
	}

	@AfterStep
	public void screenShotAfterEachStep(Scenario s) {
		if (!scenario.getName().contains("API")) {
			try {
				embedScreenshot(s);
				com_fun.embedScreeshot(String.valueOf(screenshotCount), s, directoryPath);
			} catch (Exception e) {
				System.out.println("Exception in Screenshot Handled");
			}
			screenshotCount++;
		}
	}

	public static String embedScreeshot(String imageName) {
		String path = null;
		try {
			path = com_fun.takeScreenshots(directoryPath, imageName);
			File scrFile = new File(path);
			byte[] data = FileUtils.readFileToByteArray(scrFile);
			Allure.getLifecycle().addAttachment(
					LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MMM-yy_hh:mm:ss")), "image/png", "png",
					data);
			scenario.embed(data, "image/png");
			scenario.write(imageName);
		} catch (Exception e) {
			Log.error(e.getMessage());
		}
		return path;
	}

	public static String getProperty(String propertyName) {
		return CONFIG.getProperty(propertyName);
	}

	public Map<String, String> getDbDetails() {
		dbDetails = new HashMap<String, String>();
		dbDetails.put("URL", CONFIG.getProperty("DB_URL"));
		dbDetails.put("USERNAME", CONFIG.getProperty("DB_UNAME"));
		dbDetails.put("PASSWORD", CONFIG.getProperty("DB_PWD"));
		return dbDetails;
	}

	public static void embedScreenshot(Scenario scenario) {
		byte[] screenShot = ((TakesScreenshot) AppiumHelper.driverInit()).getScreenshotAs(OutputType.BYTES);
		Allure.getLifecycle().addAttachment(
				LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MMM-yy_hh:mm:ss")), "image/png", "png",
				screenShot);
	}

	public static void uninstallApplication() {
		AppiumController.instance.uninstallApplication(CONFIG);
	}

	public static void captureScreeshot(int step) {
		com_fun.takeScreenshots(System.getProperty("user.dir") + CONFIG.getProperty("SCREENSHOT_PATH"),
				scenario.getName().split(":")[0] + "_0" + step);
	}

}
