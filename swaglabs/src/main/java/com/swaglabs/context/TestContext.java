package com.swaglabs.context;

import com.swaglabs.manager.PageObjectManager;

public class TestContext {

    private PageObjectManager pageObjectManager;

    private ScenarioContext scenarioContext;

    public TestContext() {
        pageObjectManager = new PageObjectManager();
        scenarioContext = new ScenarioContext();
    }

    public PageObjectManager getPageObjectManagerInstance() {
        return pageObjectManager;
    }

    public ScenarioContext getScenarioContext() {
        return scenarioContext;
    }

}
