package com.swaglabs.manager;

import com.swaglabs.pages.LoginPage;
import com.swaglabs.pages.ProductCataloguePage;

public class PageObjectManager {

    private LoginPage loginPage;

    private ProductCataloguePage productCataloguePage;

    public LoginPage getloginPage() {
        return (loginPage == null) ? loginPage = new LoginPage() : loginPage;
    }

    public ProductCataloguePage getDashboardPage() {
        return (productCataloguePage == null) ? productCataloguePage = new ProductCataloguePage() : productCataloguePage;
    }
}
