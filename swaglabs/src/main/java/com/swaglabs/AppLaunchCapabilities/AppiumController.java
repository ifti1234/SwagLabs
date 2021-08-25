package com.swaglabs.AppLaunchCapabilities;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.IOSMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.remote.MobilePlatform;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import org.openqa.selenium.remote.DesiredCapabilities;

public class AppiumController {

    public static final AppiumController instance = new AppiumController();

    public AppiumDriver<MobileElement> driver;

    public String host;

    public void appLaunch(Properties config) throws MalformedURLException {
        host = config.getProperty("HOST");
        if (driver != null) {
            return;
        }
        DesiredCapabilities capabilities = new DesiredCapabilities();

        // defining directory of the path where app is present
        String appUrl = System.getProperty("user.dir") + config.getProperty("APP_URL");
        File appDir = new File(appUrl);
        File app = new File(appDir, config.getProperty("APP_NAME"));

        // defining appium url
        String appiumUrl = "https://" + config.getProperty("APPIUM_IP") + ":" + config.getProperty("APPIUM_PORT")
                + "/wd/hub";

        //Defining Swaglabs URL
        String cloudUrl = "https://" + config.getProperty("UNAME") + ":" + config.getProperty("ACCESS_KEY") + "@"
                + config.getProperty("SAUCELABS_URL") + "/wd/hub" ;
        System.out.println("Cloud URL is : " + cloudUrl);

        switch (config.getProperty("OS")) {

            default:
                System.out.println("OS version is not correct");
                break;

            case "ANDROID":
                capabilities.setCapability("platformName", "Android");
                capabilities.setCapability("deviceName", config.getProperty("DEVICE_ID"));
                capabilities.setCapability("platformVersion", "10.0");
                capabilities.setCapability("browserName", "");
                capabilities.setCapability("deviceOrientation", "portrait");
                capabilities.setCapability("appiumVersion", "1.16.0");
                capabilities.setCapability(AndroidMobileCapabilityType.APP_PACKAGE, config.getProperty("APP_PACKAGE"));
                capabilities.setCapability(AndroidMobileCapabilityType.APP_ACTIVITY,
                        config.getProperty("APP_ACTIVITY"));
                capabilities.setCapability(MobileCapabilityType.APP, config.getProperty("APP"));

                if (config.getProperty("HOST").equalsIgnoreCase("CLOUD")) {
                    System.out.println("-------Running on SauceLabs-------");
                    capabilities.setCapability("instrumentApp", Boolean.valueOf(config.getProperty("INSTRUMENT_APP")));
                    if (config.getProperty("REPORT") != null) {
                        capabilities.setCapability("generateReport", config.getProperty("REPORT"));
                    }
                    driver = new AndroidDriver<MobileElement>(new URL(cloudUrl), capabilities);
                } else {
                    capabilities.setCapability(MobileCapabilityType.APP, app.getAbsolutePath());
                    driver = new AndroidDriver<MobileElement>(new URL(appiumUrl), capabilities);
                }
                break;

            case "IOS":
                capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, MobilePlatform.IOS);
                capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION,
                        config.getProperty("PLATFORM_VERSION"));
                capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, config.getProperty("DEVICE_NAME"));
                capabilities.setCapability(MobileCapabilityType.UDID, config.getProperty("DEVICE_ID"));
                capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME,
                        config.getProperty("AUTOMATION_NAME_IOS"));
                capabilities.setCapability(IOSMobileCapabilityType.XCODE_ORG_ID, config.getProperty("XCODE_ORG_ID"));
                capabilities.setCapability(IOSMobileCapabilityType.XCODE_SIGNING_ID, config.getProperty("SIGNING_ID"));
                capabilities.setCapability(IOSMobileCapabilityType.UPDATE_WDA_BUNDLEID,
                        config.getProperty("DRIVER_BUNDLE_ID"));
                capabilities.setCapability(IOSMobileCapabilityType.USE_NEW_WDA, true);
                capabilities.setCapability(IOSMobileCapabilityType.WDA_LOCAL_PORT,
                        Integer.parseInt(config.getProperty("WDAPORT")));
                capabilities.setCapability(MobileCapabilityType.FULL_RESET, config.getProperty("FULL_RESET"));
                capabilities.setCapability(MobileCapabilityType.NO_RESET, true);
                capabilities.setCapability(IOSMobileCapabilityType.SIMPLE_ISVISIBLE_CHECK, true);
                capabilities.setCapability(IOSMobileCapabilityType.PREVENT_WDAATTACHMENTS, true);
                capabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT,
                        config.getProperty("SESSION_TIMEOUT"));
                capabilities.setCapability("newSessionWaitTimeout", "1200");
                if (config.getProperty("HOST").equalsIgnoreCase("CLOUD")) {
                    System.out.println("-------Running on ExperiTest-------");
                    // capabilities.setCapability("appiumVersion", "1.16.0-p1");
                    capabilities.setCapability("instrumentApp", Boolean.valueOf(config.getProperty("INSTRUMENT_APP")));
                    capabilities.setCapability("appBuildVersion", config.getProperty("BUILD_VERSION"));
                    capabilities.setCapability(MobileCapabilityType.APP, config.getProperty("APP_SEETEST_IOS"));
                    capabilities.setCapability("accessKey", config.getProperty("ACCESS_KEY"));
                    if (config.getProperty("REPORT") != null) {
                        capabilities.setCapability("report.disable", config.getProperty("REPORT"));
                    }
                    driver = new IOSDriver<MobileElement>(new URL(cloudUrl), capabilities);
                } else {
                    capabilities.setCapability(MobileCapabilityType.APP, app.getAbsolutePath());
                    driver = new IOSDriver<MobileElement>(new URL(appiumUrl), capabilities);
                }
                break;
        }
    }

    public void appClose(Properties config) {
        try {
            if (driver != null) {
                switch (config.getProperty("OS")) {

                    default:
                        System.out.println("OS version is not correct");
                        break;

                    case "ANDROID":
                        driver.executeScript("seetest: client.applicationClose(\"cloud:com.adib.mobile\")");
                        break;
                    case "IOS":
                        if (config.getProperty("HOST").equalsIgnoreCase("CLOUD")) {
                            driver.executeScript("seetest: client.applicationClose(\"cloud:com.adib.mobilebanking\")");
                        } else {
                            driver.closeApp();
                        }
                        break;
                }
                driver.quit();
            }
        } catch (Exception e) {
            e.printStackTrace();
            driver = null;
        } finally {
            driver = null;
        }
    }

    public void uninstallApplication(Properties config) {
        try {
            if (driver != null) {
                switch (config.getProperty("OS")) {

                    default:
                        System.out.println("OS version is not correct");
                        break;

                    case "ANDROID":
                        if (config.getProperty("HOST").equalsIgnoreCase("CLOUD")) {
                            driver.executeScript("seetest: client.uninstall(\"cloud:com.adib.mobile\")");
                        } else {
                            driver.removeApp("com.adib.mobile");
                        }
                        break;

                    case "IOS":
                        if (config.getProperty("HOST").equalsIgnoreCase("CLOUD")) {
                            driver.executeScript("seetest: client.uninstall(\"cloud:com.adib.mobilebanking\")");
                        } else {
                            driver.removeApp("com.adib.mobilebanking");
                        }
                        break;
                }
                driver.quit();
            }
        } catch (Exception e) {
            e.printStackTrace();
            driver = null;
        } finally {
            driver = null;
        }
    }

    public void resetApplication(Properties config, boolean ifReset) {
        try {
            if (ifReset && driver != null) {
                switch (config.getProperty("OS")) {

                    default:
                        System.out.println("OS version is not correct");
                        break;

                    case "ANDROID":
                        if (config.getProperty("HOST").equalsIgnoreCase("CLOUD")) {
                            // can be used to clear application data
                            driver.executeScript("seetest: client.applicationClearData(\"cloud: com.adib.mobile\")");
                        } else {
                            driver.resetApp();
                        }
                        break;

                    case "IOS":
                        if (config.getProperty("HOST").equalsIgnoreCase("CLOUD")) {
                            driver.executeScript("seetest: client.applicationClearData(\"com.adib.mobilebanking\")");
                        } else {
                            driver.resetApp();
                        }
                        break;
                }
                // relaunch app after clearing app data
                driver.launchApp();
            }
        } catch (Exception e) {
            e.printStackTrace();
            driver = null;
        }
    }

}
