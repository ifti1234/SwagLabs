package com.swaglabs.pages;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.openqa.selenium.support.PageFactory;

import com.swaglabs.AppLaunchCapabilities.AppiumHelper;
import com.swaglabs.commonfunctions.CommonFunctions;

import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;

public class ProductCataloguePage {

    public ProductCataloguePage() {
        PageFactory.initElements(new AppiumFieldDecorator(AppiumHelper.driverInit()), this);
    }

    @iOSXCUITFindBy(id = "to-do")
    @AndroidFindBy(xpath = "//android.view.ViewGroup[@content-desc=\"test-Cart drop zone\"]/android.view.ViewGroup/android.widget.TextView")
    public MobileElement product_Label;

    @iOSXCUITFindBy(id = "to-do")
    @AndroidFindBy(xpath = "//android.view.ViewGroup[@content-desc=\"test-Item\"]/descendant::*[@content-desc=\"test-Item title\"]")
    public List<MobileElement> product_Name;

    @iOSXCUITFindBy(id = "to-do")
    @AndroidFindBy(xpath = "//android.view.ViewGroup[@content-desc=\"test-Item\"]/descendant::*[@content-desc=\"test-ADD TO CART\"]")
    public MobileElement addAProduct;

    @iOSXCUITFindBy(id = "to-do")
    @AndroidFindBy(xpath = "//android.view.ViewGroup[@content-desc=\"test-Cart\"]/android.view.ViewGroup/android.widget.ImageView")
    public MobileElement cartOption;

    @iOSXCUITFindBy(id = "to-do")
    @AndroidFindBy(xpath = "//android.view.ViewGroup[@content-desc=\"test-REMOVE\"]")
    public MobileElement removeProduct;

    public void validateDashboardPageisdisplayed(CommonFunctions comfun) {
        comfun.waitCondition(product_Label);
        assertTrue("Dashboard page is not displayed", comfun.isElementPresent(product_Label));
    }

    public void addAProductinCart(CommonFunctions comfun) {
        comfun.waitCondition(addAProduct, 30);
        addAProduct.click();
    }

    public void addandValidateTheProductIntheCart(CommonFunctions comfun) {
        for (int i = 0; i < product_Name.size(); i++) {
            String text=comfun.getText(product_Name.get(i));
            comfun.click(addAProduct);
            comfun.waitCondition(cartOption, 20);
            comfun.click(cartOption);
            Boolean addedProduct = comfun.findElementByString(text).getText().equals(text);
            assertTrue("Product is not available in the cart", addedProduct);
        }
    }

    public void deleteAProductUsingProductName(CommonFunctions comfun) {
        for (int i = 0; i < product_Name.size(); i++) {
            String text=comfun.getText(product_Name.get(i));
            comfun.click(removeProduct);
            comfun.waitCondition(cartOption, 20);
            comfun.click(cartOption);
            assertNull("Item not Deleted", comfun.findElementByString(text));
            comfun.navigateBack();
        }
    }

    public void addDeleteProduct(CommonFunctions comfun) {
        for (int i = 0; i < product_Name.size(); i++) {
            comfun.click(addAProduct);
            assertTrue("Add Product Not Displayed", comfun.isElementPresent(removeProduct));
            comfun.waitCondition(cartOption, 20);
            comfun.click(cartOption);
            comfun.click(removeProduct);
            comfun.navigateBack();
        }
    }

    public void validateDeletedProduct(CommonFunctions comfun) {
        assertTrue("Added Product is Not Displayed", comfun.isElementPresent(addAProduct));
    }
}

