package com.swaglabs.commonfunctions;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.swaglabs.AppLaunchCapabilities.AppiumHelper;
import com.swaglabs.utilities.Log;

import cucumber.api.Scenario;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.Activity;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.StartsActivity;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.PressesKey;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.ElementOption;
import io.appium.java_client.touch.offset.PointOption;
import io.qameta.allure.Allure;

public class CommonFunctions {

    AppiumDriver<MobileElement> driver;

    public CommonFunctions() {
        this.driver = AppiumHelper.driverInit();
    }

    public CommonFunctions(AppiumDriver<MobileElement> driver) {
        this.driver = driver;
    }

    /**
     * Method to wait till particular timeout
     *
     * @param ae
     *            Mobile Element Object
     * @param //driver
     */
    public void waitCondition(MobileElement ae, int timeOut) {
        WebDriverWait wait = new WebDriverWait(driver, timeOut);
        wait.until(ExpectedConditions.visibilityOf(ae));
    }

    /**
     * Method to hide active keyboard.
     *
     */
    public void hideKeyBoard() {
        if (driver instanceof AndroidDriver<?>) {
            ((AndroidDriver<MobileElement>) driver).isKeyboardShown();
            driver.hideKeyboard();
        }
    }

    /**
     * Method to clear and Type.
     *
     * @param: Element
     *             for the text field
     * @param: Text
     *             to enter
     */
    public void clearType(MobileElement ae, String text) {
        ae.clear();
        ae.sendKeys(text);
        try {
            hideKeyBoard();
        } catch (Exception e) {
            Log.info("No Hiding Keyboard");
        }
    }

    public void inputTextIos(MobileElement ae, String text) {
        ae.click();
        ae.sendKeys(Keys.CONTROL + "a");
        ae.sendKeys(Keys.DELETE);
        ae.click();
        ae.clear();
        ae.sendKeys(text);
    }

    /**
     * Method to dismiss alerts.
     *
     */
    public void dismissAlert() {
        // Alert simplealert = driver.switchTo().alert();
        // simplealert.dismiss();
        // driver.pressKeyCode();
        // driver.pressKeyCode(new KeyEvent(AndroidKey.ENTER));
        // driver.pressKeyCode(AndroidKeyCode.ENTER);
        if (driver instanceof AndroidDriver<?>) {
            ((PressesKey) this.driver).pressKey(new io.appium.java_client.android.nativekey.KeyEvent(AndroidKey.ENTER));
        }
    }

    /**
     * Method to wait the tread.
     *
     * @param duration
     *            wait time in long
     */
    public void driverWait(long duration) {
        try {
            Thread.sleep(duration);
        } catch (InterruptedException e) {
            // Log.info("Thread.wait failed to execute");
        }
    }

    public void driverWaitSessionExpiry() {
        try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            // Log.info("Thread.wait failed to execute");
        }
    }

    /**
     * Method to wait for visibility of element
     *
     * @param //ae
     *            Android element to wait for
     */

    public void waitConditionForListOfMobileElements(String xpath) {
        WebDriverWait wait = new WebDriverWait(driver, 50);
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(xpath))).stream()
                .map(element -> (MobileElement) element).collect(Collectors.toList());
    }

    public void waitCondition(MobileElement ae) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, 60);
            wait.until(ExpectedConditions.visibilityOf(ae));
        } catch (TimeoutException e) {
            genericVerticalScroll(ae, 6, 0.5, 0.2);
        }
    }

    public void waitCondition(List<WebElement> ae) {
        WebDriverWait wait = new WebDriverWait(driver, 60);
        wait.until(ExpectedConditions.visibilityOfAllElements(ae));
    }

    @SuppressWarnings("rawtypes")
    public void genericVerticalSwipe(double startyRatio) {
        Dimension dim = driver.manage().window().getSize();
        int xval = dim.getWidth() / 2;
        int starty = (int) (dim.getHeight() * startyRatio);
        int endy = (int) (dim.getHeight() * 0.20);
        new TouchAction(driver).press(ElementOption.point(xval, starty))
                .waitAction(new WaitOptions().withDuration(Duration.ofMillis(500)))
                .moveTo(ElementOption.point(xval, endy)).release().perform();
    }

    public String numberFormatter(String val) {
        NumberFormat formatter = new DecimalFormat("##,###");
        return formatter.format(Float.parseFloat(val));
    }

    public String numberFormatterDecimal(String val) {
        NumberFormat formatter = new DecimalFormat("##,##0.00");
        return formatter.format(Double.parseDouble(val));
    }

    public int randomNumber(int val, int min, int max) {
        Random ran = new Random();
        return ran.ints(val, min, max).findFirst().getAsInt();
    }

    public long randomLongNumber(long val, long min, long max) {
        Random ran = new Random();
        return ran.longs(val, min, max).findFirst().getAsLong();
    }

    public List<String> generateInvalidUaeNumberList(String length) {
        List<String> invalid = new ArrayList<String>();
        int rnm1 = RandomUtils.nextInt(0,
                Integer.parseInt(RandomStringUtils.randomNumeric(Integer.parseInt(length) + 1)));
        invalid.add(Integer.toString(rnm1));
        invalid.add(RandomStringUtils.randomNumeric(Integer.parseInt(length) + 2));
        invalid.add(RandomStringUtils.randomNumeric(Integer.parseInt(length) + 3));
        return invalid;
    }

    public List<String> generateInValidUaeNumber(String length) {
        String mobileNumberInvalid;
        List<String> invalidmobnumberlist = new ArrayList<String>();
        String[] rnmexclude9 = { "50", "52", "55", "56", "54", "58" };
        String[] rnmexclude10 = { "050", "052", "054", "055", "056", "058" };
        for (int i = 0; i <= 1; i++) {
            List<String> mobNumbrs = generateInvalidUaeNumberList(length);
            Random rand = new Random();

            mobileNumberInvalid = mobNumbrs.get(rand.nextInt(mobNumbrs.size()));
            if (mobileNumberInvalid.length() == 9) {
                for (String s : rnmexclude9) {
                    if (mobileNumberInvalid.substring(0, 2).equals(s)) {
                        List<String> mobNumbrs1 = generateInvalidUaeNumberList(length);
                        mobileNumberInvalid = mobNumbrs1.get(rand.nextInt(mobNumbrs1.size()));
                    }
                }
            } else if (mobileNumberInvalid.length() == 10) {
                for (String s : rnmexclude10) {
                    if (mobileNumberInvalid.substring(0, 3).equals(s)) {
                        List<String> mobNumbrs1 = generateInvalidUaeNumberList(length);
                        mobileNumberInvalid = mobNumbrs1.get(rand.nextInt(mobNumbrs1.size()));
                    }
                }
                for (String s : rnmexclude9) {
                    if (mobileNumberInvalid.substring(0, 2).equals(s)) {
                        List<String> mobNumbrs1 = generateInvalidUaeNumberList(length);
                        mobileNumberInvalid = mobNumbrs1.get(rand.nextInt(mobNumbrs1.size()));
                    }
                }
            }
            invalidmobnumberlist.add(mobileNumberInvalid);
        }
        return invalidmobnumberlist;
    }

    public String embedScreeshot(String imageName, Scenario sc, String directoryPath) {
        String path = null;
        try {
            path = takeScreenshots(directoryPath, imageName);
            File scrFile = new File(path);
            byte[] data = FileUtils.readFileToByteArray(scrFile);
            sc.embed(data, "image/png");
            sc.write(imageName);
        } catch (Exception e) {
            Log.error(e.getMessage());
        }
        return path;
    }

    public void embedScreenshot() {
        byte[] screenShot = ((TakesScreenshot) AppiumHelper.driverInit()).getScreenshotAs(OutputType.BYTES);
        Allure.getLifecycle().addAttachment(
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MMM-yy_hh:mm:ss")), "image/png", "png",
                screenShot);
    }

    public MobileElement findElementByAccessibility(String accessibility) {
        int counter = 1;
        MobileElement ie = null;
        do {
            try {
                driverWait(1000);
                ie = driver.findElementByAccessibilityId(accessibility);
            } catch (Exception e) {
                ie = null;
                counter++;
            }
        } while (ie == null && counter <= 15);
        return ie;
    }

    public MobileElement findElementByXpath(String xpath) {
        int counter = 1;
        MobileElement ie = null;
        do {
            try {
                ie = driver.findElementByXPath(xpath);
            } catch (Exception e) {
                ie = null;
                counter++;
                driverWait(1000);
            }
        } while (ie == null && counter <= 15);
        return ie;
    }

    public MobileElement findElementById(String id) {
        int counter = 1;
        MobileElement ie = null;
        do {
            try {
                ie = driver.findElementById(id);
            } catch (Exception e) {
                ie = null;
                counter++;
                driverWait(1000);
            }
        } while (ie == null && counter <= 15);
        return ie;
    }

    public MobileElement findElementByString(String text) {
        MobileElement ie = null;
        String xpath = "//*[@text='" + text + "']";
        try {
            ie = driver.findElementByXPath(xpath);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            ie=null;
        }
        return ie;
    }

    public MobileElement findElementByLabelString(String text, String type) {
        int counter = 1;
        MobileElement ie = null;
        do {
            try {
                driverWait(1000);
                String xpath = String.format("//%s[@label='%s']", type, text);
                ie = driver.findElementByXPath(xpath);
            } catch (NoSuchElementException e) {
                e.printStackTrace();
                ie = null;
                counter++;
                driverWait(1000);
            }
        } while (ie == null && counter <= 5);
        return ie;
    }

    public List<MobileElement> findElementsById(String id) {
        int counter = 1;
        List<MobileElement> ie = null;
        do {
            try {
                driverWait(1000);
                ie = driver.findElementsById(id);
            } catch (Exception e) {
                ie = null;
                counter++;

            }
        } while (ie == null && counter <= 15);
        return ie;
    }

    public List<MobileElement> findElementsByXpath(String xpath) {
        int counter = 1;
        List<MobileElement> ie = null;
        do {
            try {
                driverWait(1000);
                ie = driver.findElementsByXPath(xpath);
            } catch (Exception e) {
                ie = null;
                counter++;

            }
        } while (ie == null && counter <= 15);
        return ie;
    }

    /**
     * Method to compare two integer values
     *
     * @param actual
     *            actual int value
     * @param expected
     *            expected int value
     * @return boolean result
     */
    public boolean validateIntegers(int actual, int expected) {
        if (actual == expected) {
            return true;
        }
        return false;
    }

    /**
     * Method to valdate text.
     *
     * @param: Element
     *             for the text field
     * @param: Text
     *             to verify
     */
    public boolean validateText(MobileElement ae, String text) {
        System.out.println("Expected Text = " + text.replace("  ", " "));
        System.out.println("Actual Text = " + ae.getText().replace("\n", "").replace("  ", " "));
        if (ae.getText().replace("\n", "").replace("  ", " ").toUpperCase()
                .equals(text.toUpperCase().replace("  ", " "))) {
            return true;
        } else {
            return false;
        }
    }

    public boolean validateTextView(MobileElement ae, String text) {
        if (text.equals("")) {
            return ae.getText().equals(" ");
        } else {
            return ae.getText().equals(text);
        }
    }

    public String dateDifference(String firstDate, String secondDate) {
        SimpleDateFormat myFormat = new SimpleDateFormat("dd MM yyyy");
        try {
            Date date1 = myFormat.parse(firstDate);
            Date date2 = myFormat.parse(secondDate);
            long diff = date2.getTime() - date1.getTime();
            return String.valueOf(TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Method to validate api response. Text will be updated to "" in case null is
     * received
     *
     * @param: Element
     *             for the text field
     * @param: Text
     *             to verify
     */
    public boolean validateApiText(MobileElement ae, String text) {
        if (text == null) {
            text = "";
        }
        System.out.println("Full Name text in Validate Api text:-" + ae.getText());
        return ae.getText().equals(text);
    }

    /**
     * Method to get string api response. Text will be updated to "" in case null is
     * received
     *
     * @param: object
     *             from api for the text field
     */
    public String getApiVal(Object obj) {
        if (obj == null) {
            return "";
        }
        return obj.toString();
    }

    /**
     * Validate integer values
     *
     * @param ae
     *            MobileElement for text
     * @param text
     *            value to assert for
     * @return boolean value
     */
    public boolean validateInt(MobileElement ae, String text) {
        try {
            // assertEquals(NumberFormat.getNumberInstance(Locale.ENGLISH).parse(ae.getText()).intValue(),
            // (int) Double.parseDouble(text));
            // return true;
            if (NumberFormat.getNumberInstance(Locale.ENGLISH)
                    .parse(ae.getText().replace(",", "").replace("AED", "").trim())
                    .intValue() == (int) Double.parseDouble(text)) {
                return true;
            } else {
                return false;
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            System.out.println("Number format exception occoured");
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Exception occoured");
            return false;
        }
    }

    public int getIntegerValue(String text) {
        int number = 0;
        try {
            number = NumberFormat.getNumberInstance(Locale.ENGLISH).parse(text).intValue();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return number;
    }

    @SuppressWarnings("rawtypes")
    public void tapByCordinates(int xval, int yval, int shiftX, int shiftY)

    {
        new TouchAction(driver).tap(PointOption.point(xval + shiftX, yval + shiftY)).perform().release();
    }

    /**
     * Method to minimize the application on screen using Home button.
     *
     */
    public void minimizeApplication() {
        ((PressesKey) driver).pressKey(new io.appium.java_client.android.nativekey.KeyEvent(AndroidKey.HOME));
        // driver.pressKeyCode(AndroidKeyCode.HOME);
        driverWait(2000);

        // driver.pressKey(new KeyEvent(AndroidKey.HOME));
        // driverWait(2000);
    }

    /**
     * Method to navigate back.
     *
     */
    public void navigateBack() {
        // driver.pressKey(new KeyEvent(AndroidKey.BACK));
        driver.navigate().back();

    }

    public String getText(MobileElement ae) {
        return ae.getText();
    }

    public void relaunchApp() {
        driver.runAppInBackground(Duration.ofSeconds(10));
        driver.launchApp();
    }

    /**
     * Method to read a Toast message.
     *
     */
    /*
     * public String readToastMsg() { String imageScrShot = takeScreenshots();
     * <<<<<<< HEAD String result = null; File imagefile = new File(scrShotDir,
     * imageScrShot); ITesseract instance = new Tesseract(); ======= String result =
     * null; File imagefile = new File(scrShotDir, imageScrShot); ITesseract
     * instance = new Tesseract(); >>>>>>> 16af410559c24fb450c88ca4acf12351d7979ee1
     *
     * File testDataFolder =
     * net.sourceforge.tess4j.util.LoadLibs.extractTessResources("tessdata");
     *
     * instance.setDatapath(testDataFolder.getAbsolutePath()); try { result =
     * instance.doOCR(imagefile); } catch (TesseractException e) { <<<<<<< HEAD
     * Log.info(e.getMessage()); Log.info(e.toString()); } Log.info(result); return
     * result; } ======= Log.info(e.getMessage()); Log.info(e.toString()); }
     * Log.info(result); return result; } >>>>>>>
     * 16af410559c24fb450c88ca4acf12351d7979ee1
     */

    /**
     * Method to minimize the screen.
     *
     * @param: String
     *             locator locator value in ID format
     */

    public void selectMinimizedApplication(String locator) {
        ((PressesKey) driver).pressKey(new io.appium.java_client.android.nativekey.KeyEvent(AndroidKey.APP_SWITCH));
        driver.findElementById(locator).click();
        driverWait(2000);
    }

    /**
     * Method to change device orientation
     *
     * @param orient
     *            Orientation type
     */
    public void changeOrientation(String orient) {
        ScreenOrientation so = driver.getOrientation();
        switch (orient) {
            case "PORTRAIT": {
                if (!so.equals(ScreenOrientation.PORTRAIT)) {
                    driver.rotate(ScreenOrientation.PORTRAIT);
                }
                break;
            }
            case "LANDSCAPE": {
                if (!so.equals(ScreenOrientation.LANDSCAPE)) {
                    driver.rotate(ScreenOrientation.LANDSCAPE);
                }
                break;
            }
            default:
                break;
        }
    }

    @SuppressWarnings("rawtypes")
    public void tapActions(MobileElement ae) {
        // new TouchAction(driver).tap(ae).perform().release();
        new TouchAction(driver).tap(ElementOption.element(ae)).perform().release();
    }

    @SuppressWarnings("rawtypes")
    public void genericVerticalScroll(String xpath, int loopCount, double startyRatio, double endyRatio) {
        for (int i = 1; i < loopCount; i++) {
            try {
                if (driver.findElement(By.xpath(xpath)).isDisplayed()) {
                    break;
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            Dimension dim = driver.manage().window().getSize();
            int xval = dim.getWidth() / 2;
            int starty = (int) (dim.getHeight() * startyRatio);
            int endy = (int) (dim.getHeight() * endyRatio);

            new TouchAction(driver).press(ElementOption.point(xval, starty))
                    .waitAction(new WaitOptions().withDuration(Duration.ofMillis(500)))
                    .moveTo(ElementOption.point(xval, endy)).release().perform();
        }
    }

    @SuppressWarnings("rawtypes")
    public void genericVerticalScrollifElementEnabled(MobileElement ele, int loopCount, double startyRatio,
                                                      double endyRatio) {
        for (int i = 1; i < loopCount; i++) {
            try {
                if (ele.isEnabled()) {
                    break;
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            Dimension dim = driver.manage().window().getSize();
            int xval = dim.getWidth() / 2;
            int starty = (int) (dim.getHeight() * startyRatio);
            int endy = (int) (dim.getHeight() * endyRatio);

            new TouchAction(driver).press(ElementOption.point(xval, starty))
                    .waitAction(new WaitOptions().withDuration(Duration.ofMillis(1)))
                    .moveTo(ElementOption.point(xval, endy)).release().perform();
            System.out.println("loopcount is: " + i);
        }
    }

    @SuppressWarnings("rawtypes")
    public void genericVerticalScroll(MobileElement ele, int loopCount, double startyRatio, double endyRatio) {
        for (int i = 1; i < loopCount; i++) {
            Dimension dim = driver.manage().window().getSize();
            int xval = dim.getWidth() / 2;
            int starty = (int) (dim.getHeight() * startyRatio);
            int endy = (int) (dim.getHeight() * endyRatio);

            new TouchAction(driver).press(ElementOption.point(xval, starty))
                    .waitAction(new WaitOptions().withDuration(Duration.ofMillis(500)))
                    .moveTo(ElementOption.point(xval, endy)).release().perform();
            try {
                if (ele.isDisplayed()) {
                    break;
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    @SuppressWarnings("rawtypes")
    public void genericHorizontalScroll(MobileElement ele, int loopCount, double startxRatio, double endxRatio) {
        for (int i = 1; i < loopCount; i++) {
            Dimension dim = driver.manage().window().getSize();
            int yval = dim.getHeight() / 2;
            int startx = (int) (dim.getWidth() * startxRatio);
            int endx = (int) (dim.getWidth() * endxRatio);

            new TouchAction(driver).press(ElementOption.point(startx, yval))
                    .waitAction(new WaitOptions().withDuration(Duration.ofMillis(500)))
                    .moveTo(ElementOption.point(endx, yval)).release().perform();
            try {
                if (ele.isDisplayed()) {
                    break;
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Method to Swipe by elements.
     *
     * @param: startElement
     *             start element to swipe
     * @param: endElement
     *             end element to swipe
     */
    //// Swipe by elements from start to end point
    @SuppressWarnings("rawtypes")
    public void genericHorizontalSwipeByElements(MobileElement end, MobileElement start) {
        int startX = end.getLocation().getX();
        int startY = end.getLocation().getY();
        int endX = start.getLocation().getX() + 270;
        int endY = start.getLocation().getY();
        new TouchAction(driver).press(PointOption.point(startX, startY))
                .waitAction(WaitOptions.waitOptions(Duration.ofMillis(1000))).moveTo(PointOption.point(endX, endY))
                .release().perform();
    }

    @SuppressWarnings("rawtypes")
    public void clickElementsByCoardinates(int startX, int startY) {
        new TouchAction(driver).tap(PointOption.point(startX, startY)).perform();
        // new TouchAction(driver).tap(PointOption.point(startX,
        // startY)).release().perform();
    }

    /**
     * perform longpress on the element.
     *
     * @param ae
     *            identified element
     */
    @SuppressWarnings("rawtypes")
    public void performLongPress(MobileElement ae) {
        // TouchAction ta = new TouchAction(driver);
        // ta.longPress(ae).perform().release();
        new TouchAction(driver).longPress(ElementOption.element(ae)).perform().release();

    }

    /**
     * Method to Switch applications.
     *
     * @param: settingsAppPackageName
     *             String settingsAppPackageName is the App package name
     * @param: settingsAppActivityName
     *             String settingsAppActivityName is the App activity name
     */
    public void switchApplication(String settingsAppPackageName, String settingsAppActivityName) {
        Activity activity = new Activity(settingsAppPackageName, settingsAppActivityName);
        ((StartsActivity) driver).startActivity(activity);
    }

    public String takeScreenshots(String dirPath, String imageName) {
        try {
            dirPath = dirPath + imageName + ".png";
            File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(scrFile, new File(dirPath));
        } catch (IOException e) {
            Log.error(e.getMessage());
        }
        return dirPath;
    }

    public String directoryCreation(Properties prop) {

        SimpleDateFormat year = new SimpleDateFormat("yyyy");
        SimpleDateFormat month = new SimpleDateFormat("MMM");
        SimpleDateFormat day = new SimpleDateFormat("dd");
        Calendar now = Calendar.getInstance();
        String dirPath = null;

        try {
            // use System.getProperty in directory path
            dirPath = System.getProperty("user.dir") + prop.getProperty("SCREENSHOT_PATH") + "/"
                    + prop.getProperty("OS") + "/" + day.format(now.getTime()) + "-" + month.format(now.getTime()) + "-"
                    + year.format(now.getTime()) + "/";
            if (dirPath.contains("@")) {
                dirPath = dirPath.replace("@", "");
            }
            File theDir = new File(dirPath);
            if (!theDir.exists()) {
                boolean result = false;
                try {
                    if (theDir.mkdirs()) {
                        result = true;
                    }
                } catch (SecurityException localSecurityException) {
                    return dirPath;
                }
                if (result) {
                    System.out.println("False");
                }
            }
        } catch (Exception e) {
            Log.error(e.getMessage());
        }
        return dirPath;
    }

    /**
     * Method to perform a Vertical swipe
     *
     * @param startPercentage
     *            Start Y axis percentage of the swipe
     * @param endPercentage
     *            End Y axis percentage of the swipe
     */

    @SuppressWarnings("rawtypes")
    public void verticalSwipe(double anchorPercentage, double startPercentage, double endPercentage) {
        Dimension dim = driver.manage().window().getSize();
        int xanchorVal = (int) (dim.getWidth() * anchorPercentage) / 100;
        int startY = (int) (dim.getHeight() * startPercentage) / 100;
        int endY = (int) (dim.getHeight() * endPercentage) / 100;
        // new TouchAction(driver).press(xanchorVal,
        // startY).waitAction(Duration.ofMillis(2000)).moveTo(xanchorVal, endY)
        // .release().perform();

        new TouchAction(driver).press(ElementOption.point(xanchorVal, startY))
                .waitAction(new WaitOptions().withDuration(Duration.ofSeconds(1)))
                .press(ElementOption.point(xanchorVal, endY)).release().perform();
    }

    /**
     * Method to Horizontal Swipe by percentages.
     *
     * @param: startPercentage
     *             start percentage to swipe
     * @param: endPercentage
     *             end percentage to swipe
     * @param: anchorPercentage
     *             anchor percentage to swipe
     */
    @SuppressWarnings("rawtypes")
    public void horizontalSwipeByPercentage(double startPercentage, double endPercentage, double anchorPercentage) {
        Dimension dim = driver.manage().window().getSize();
        int startX = (int) (dim.width * startPercentage);
        int endX = (int) (dim.width * endPercentage);
        int yanchorVal = (int) (dim.height * anchorPercentage);
        // new TouchAction(driver).press(startX,
        // yanchorVal).waitAction(Duration.ofMillis(1000)).moveTo(endX,
        // yanchorVal)
        // .release().perform();

        new TouchAction(driver).press(ElementOption.point(startX, yanchorVal))
                .waitAction(new WaitOptions().withDuration(Duration.ofSeconds(1)))
                .press(ElementOption.point(endX, yanchorVal)).release().perform();
    }

    /**
     * Method to Swipe element left.
     *
     * @param: element
     *             element to swipe
     * @param: endElement
     *             end element to swipe
     */
    //// Swipe by elements from left to right
    @SuppressWarnings("rawtypes")
    public void horizontalSwipeElement(MobileElement element) {
        int startX = element.getLocation().getX() + 370;
        int startY = element.getLocation().getY();
        int endX = element.getLocation().getX();
        int endY = element.getLocation().getY();
        new TouchAction(driver).press(PointOption.point(startX, startY))
                .waitAction(WaitOptions.waitOptions(Duration.ofMillis(1000))).moveTo(PointOption.point(endX, endY))
                .release().perform();
    }

    @SuppressWarnings("rawtypes")
    public void swipeByElements(int startX, int startY, int endX, int endY) {
        new TouchAction(driver).press(ElementOption.point(startX, startY))
                .waitAction(new WaitOptions().withDuration(Duration.ofSeconds(1)))
                .moveTo(ElementOption.point(endX, endY)).release().perform();
    }

    /**
     * Method to enable/disable switch
     *
     * @param ae
     *            android element for switch
     * @param state
     *            desiderd state ENABLE/DISABLE
     */
    public void toggleSwitch(MobileElement ae, String state) {
        switch (state) {
            case "ENABLE": {
                if (!ae.isEnabled()) {
                    ae.click();
                }
                break;
            }
            case "DISABLE": {
                if (ae.isEnabled()) {
                    ae.click();
                }
                break;
            }
            default:
                break;
        }
    }

    /**
     * Method to enable/disable checkbox
     *
     * @param ae
     *            android element for switch
     * @param state
     *            desiderd state ENABLE/DISABLE
     */
    public void toggleCheckBox(MobileElement ae, String state) {
        switch (state) {
            case "ENABLE": {
                if (!ae.isSelected()) {
                    ae.click();
                }
                break;
            }
            case "DISABLE": {
                if (ae.isSelected()) {
                    ae.click();
                }
                break;
            }
            default:
                break;
        }
    }

    public String convertDate(String joining) {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String date = null;
        if (!joining.equals("")) {
            try {
                date = new SimpleDateFormat("dd/MM/yyyy").format(formatter.parse(joining));
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return date;
        }
        return "";
    }

    public String formatConvertDate(String joining) {
        String date = joining;
        if (date.charAt(1) == '/') {
            date = "0" + date;
        }
        if (date.charAt(4) == '/') {
            date = date.substring(0, 3) + "0" + date.substring(3);
        }
        return date;
    }

    public String formatEmiratesId(String id) {
        if (id.equals("")) {
            return null;
        }
        if (id.length() != 15) {
            return id;
        }
        return id.substring(0, 3) + "-" + id.substring(3, 7) + "-" + id.substring(7, 14) + "-" + id.substring(14);
    }

    public List<MobileElement> dropdownList(String list) {
        List<MobileElement> uiList = this.driver.findElements(By.xpath(list));
        return uiList;
    }

    public String maskData(String text, int unmaskedlength) {
        int length = text.length();
        String subString = text.substring(0, length - unmaskedlength);
        String res = subString.replaceAll("[A-Za-z0-9]", "*") + text.substring(length - unmaskedlength);
        return res;
    }

    public List<String> getListSubSet(List<String> dataList, String match) {
        List<String> listSubSet = new ArrayList<String>();
        for (String data : dataList) {
            if (data.contains(match)) {
                listSubSet.add(data);
            }
        }
        return listSubSet;
    }

    public Map<String, Integer> getCoardinatesOfWebElement(MobileElement ele) {
        Map<String, Integer> coard = new HashMap<String, Integer>();
        coard.put("x", ele.getLocation().getX());
        coard.put("y", ele.getLocation().getY());
        return coard;
    }

    /**
     * Method to Vertical Swipe by percentages.
     *
     * @param: startPercentage
     *             start percentage to swipe
     * @param: endPercentage
     *             end percentage to swipe
     * @param: anchorPercentage
     *             anchor percentage to swipe
     */
    // Vertical Swipe by percentages
    @SuppressWarnings("rawtypes")
    public void verticalSwipeByPercentages(double startPercentage, double endPercentage, double anchorPercentage) {
        Dimension size = driver.manage().window().getSize();
        int anchor = (int) (size.width * anchorPercentage);
        int startPoint = (int) (size.height * startPercentage);
        int endPoint = (int) (size.height * endPercentage);

        new TouchAction(driver).press(PointOption.point(anchor, startPoint))
                .waitAction(WaitOptions.waitOptions(Duration.ofMillis(2000)))
                .moveTo(PointOption.point(anchor, endPoint)).release().perform();
    }

    public String generateRandomAlphabets(int length) {
        return RandomStringUtils.randomAlphabetic(length);
    }

    public String generateRandomAlphanumeric(int length) {
        return RandomStringUtils.randomAlphanumeric(length);
    }

    /**
     * Method to wait for the element and then click on it
     *
     * @param ae
     *            android element for clicking
     */
    public void click(MobileElement ae) {
        if (driver instanceof IOSDriver<?>) {
            waitTillProgressIconIsDisabled();
        }
        waitCondition(ae);
        waitForElementToEnable(ae);
        ae.click();
    }

    /**
     * Click Without Wait
     */
    public void tap(MobileElement ae) {
        ae.click();
    }

    public String getAttribute(MobileElement ae, String attributeName) {
        return ae.getAttribute(attributeName);
    }

    public void waitTillProgressIconIsDisabled() {
        String xpathProgressIcon;
        if (AppiumHelper.driverInit() instanceof IOSDriver<?>) {
            xpathProgressIcon = "//*[@text='In progress']";
        } else {
            xpathProgressIcon = "//*[@id='progress']";
        }
        for (int counter = 0; counter < 10; counter++) {
            try {
                WebDriverWait wait = new WebDriverWait(this.driver, 10);
                wait.until(ExpectedConditions.visibilityOf(driver.findElementByXPath(xpathProgressIcon)));
            } catch (Exception e) {
                break;
            }
        }
    }

    public void waitTillProgressIconIsDisabled(MobileElement mobileElement, int timeInSecondsToWait) {
        for (int counter = 0; counter < timeInSecondsToWait; counter++) {
            try {
                WebDriverWait wait = new WebDriverWait(this.driver, 1);
                wait.until(ExpectedConditions.visibilityOf(mobileElement));
            } catch (Exception e) {
                break;
            }
        }
    }

    public void waitForElementToEnable(MobileElement ae) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, 30);
            wait.until(ExpectedConditions.elementToBeClickable(ae));
        } catch (TimeoutException e) {
            genericVerticalScroll(ae, 6, 0.5, 0.2);
        }
        if (!ae.isEnabled()) {
            throw new RuntimeException("Element is not enabled yet.");
        }
    }

    public boolean isElementEnabled(MobileElement ae) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, 30);
            wait.until(ExpectedConditions.elementToBeClickable(ae));
        } catch (TimeoutException e) {
            genericVerticalScroll(ae, 6, 0.5, 0.2);
        }
        if (ae.isEnabled()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean containsNewLine(String str) {
        Pattern regex = Pattern.compile("^(.*)$", Pattern.MULTILINE);
        return regex.split(str).length > 0;
    }

    public Map<String, String> setupDbDetails(String url, String username, String password) {
        HashMap<String, String> dbDetails = new HashMap<String, String>();
        dbDetails.put("URL", url);
        dbDetails.put("USERNAME", username);
        dbDetails.put("PASSWORD", password);
        return dbDetails;
    }

    public boolean isElementPresent(MobileElement element) {
        try {
            element.isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
        return true;
    }

    public boolean isElementPresent(MobileElement ae, int timeout) {
        try {
            WebDriverWait wait = new WebDriverWait(this.driver, timeout);
            wait.until(ExpectedConditions.visibilityOf(ae));
        } catch (Exception e) {
            return false;
        }
        return ae.isDisplayed();
    }

    public boolean isElementRemoved(MobileElement ae, int timeout) {
        boolean removalFlag = false;
        for (int counter = 0; counter < timeout; counter++) {
            try {
                WebDriverWait wait = new WebDriverWait(this.driver, 1);
                wait.until(ExpectedConditions.visibilityOf(ae));
            } catch (Exception e) {
                removalFlag = true;
                break;
            }
        }
        return removalFlag;
    }

    /**
     * Method to wait for the element and then perform send keys operation
     *
     * @param ae
     *            android element
     */
    public void sendkeys(MobileElement ae, String text) {
        this.waitForElementToEnable(ae);
        ae.clear();
        ae.sendKeys(text);
        try {
            hideKeyBoard();
        } catch (Exception e) {
            Log.info("No Hiding Keyboard");
        }
    }

    public String generateRandomInteger(int range) {
        String randAmount = Integer
                .toString(RandomUtils.nextInt(0, Integer.parseInt(RandomStringUtils.randomNumeric(range))));
        return randAmount;
    }

    public String generateRandomNumeric(int length) {
        return RandomStringUtils.randomNumeric(length);
    }

    /** Method to send keys using keyboard function. Can be used for IOS */
    @SuppressWarnings("deprecation")
    public void enterText(MobileElement ae, String text, int timeout) {
        ae.click();
        this.driverWait(timeout);
        try {
            driver.getKeyboard().sendKeys(text);
            driver.hideKeyboard();
        } catch (Exception e) {
            driver.getKeyboard().sendKeys(text);
            driver.hideKeyboard();
        }
    }

    /**
     * Method to set picker wheel value in experitest for iOS
     *
     * @param xpath
     *            xpath to picker wheel, it can be null
     *
     * @param pickerWheelIndex
     *            index to select picker wheel, 0 if one scrollable wheel and in
     *            case of date picker wheel like 01-Jan-2020 then indexes would be
     *            0,1,2
     *
     * @param pickerValue
     *            value to be selected in wheel
     *
     */
    public void setPickerWheelValue(String xpath, String pickerWheelIndex, String pickerValue) {
        if (xpath == null || pickerWheelIndex == null) {
            driver.executeScript("seetest: client.setPickerValues(\"NATIVE\",\"xpath=//*[@class='UIAPicker']\",0,0,\""
                    + pickerValue + "\")");
        } else {
            driver.executeScript("seetest: client.setPickerValues(\"NATIVE\",\"xpath=" + xpath + "\",0,"
                    + pickerWheelIndex + ",\"" + pickerValue + "\")");
        }

    }

}
