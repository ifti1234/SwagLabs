package com.swaglabs.AppLaunchCapabilities;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;

public abstract class AppiumHelper {

    public static AppiumDriver<MobileElement> driverInit() {
        return AppiumController.instance.driver;
    }

    public static String getHostName() {
        return AppiumController.instance.host;
    }

    public static void removeDriver() {
        AppiumController.instance.driver = null;
    }
}