package com.swaglabs.utilities;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;

public class FolderStructure {

    public String directoryCreation(Properties prop,String scenarioName) {

        SimpleDateFormat year = new SimpleDateFormat("yyyy");
        SimpleDateFormat month = new SimpleDateFormat("MMM");
        SimpleDateFormat day = new SimpleDateFormat("dd");
        Calendar now = Calendar.getInstance();
        String dirPath = null;

        try {

            dirPath = System.getProperty("user.dir") + File.separator + prop.getProperty("SCREENSHOT_PATH") + File.separator + prop.getProperty("OS") + File.separator + day.format(now.getTime())+ "-" + month.format(now.getTime()) + "-" + year.format(now.getTime());
            File theDir = new File(dirPath);
            if (!theDir.exists()) {
                boolean result = false;
                try {
                    if(theDir.mkdirs())
                        result = true;
                } catch (SecurityException localSecurityException) {
                    return dirPath;
                }
                if (result) {
                    System.out.println("False");
                }
            }
        }
        catch(Exception e) {
            Log.error(e.getMessage());
        }
        return dirPath;
    }


    /**
     * Method to take a Mobile screenshot.
     *
     */
    public String takeScreenshots(String dirPath ,String imageName,AppiumDriver<MobileElement> driver) {
        try
        {
            dirPath = dirPath + "/" + imageName+ ".png";
            File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(scrFile, new File(dirPath));
        }
        catch(IOException e) {
            Log.error(e.getMessage());
        }
        return dirPath;
    }

}
