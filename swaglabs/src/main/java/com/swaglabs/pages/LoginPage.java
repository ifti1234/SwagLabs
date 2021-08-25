package com.swaglabs.pages;

import static org.junit.Assert.assertTrue;

import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;

import org.openqa.selenium.support.PageFactory;

import com.swaglabs.AppLaunchCapabilities.AppiumHelper;
import com.swaglabs.commonfunctions.CommonFunctions;

public class LoginPage {

    public LoginPage() {
        PageFactory.initElements(new AppiumFieldDecorator(AppiumHelper.driverInit()), this);
    }

    @iOSXCUITFindBy(id = "to-do")
    @AndroidFindBy(xpath = "//android.widget.ScrollView[@content-desc=\"test-Login\"]/android.view.ViewGroup/android.widget.ImageView[1]")
    public MobileElement welcomePageText;

    @iOSXCUITFindBy(id = "to-do")
    @AndroidFindBy(xpath = "//android.widget.EditText[@content-desc=\"test-Username\"]")
    public MobileElement usernametextField;

    @iOSXCUITFindBy(id = "to-do")
    @AndroidFindBy(xpath = "//android.widget.EditText[@content-desc=\"test-Password\"]")
    public MobileElement passwordtextField;

    @iOSXCUITFindBy(id = "to-do")
    @AndroidFindBy(xpath = "//android.view.ViewGroup[@content-desc=\"test-LOGIN\"]")
    public MobileElement submitButton;

    public void verifyWelcomePageOfTheApplication(CommonFunctions comfun) throws InterruptedException {
        comfun.waitCondition(welcomePageText, 30);
        assertTrue("User not on Welcome Page", comfun.isElementPresent(welcomePageText));
    }

    public void loginToTheApplication(CommonFunctions comfun, String userName, String password) {
        usernametextField.sendKeys(userName);
        passwordtextField.sendKeys(password);
        comfun.click(submitButton);
    }

}
