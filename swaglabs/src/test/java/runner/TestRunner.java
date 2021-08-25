package runner;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(features = { "src/test/resources/feature/" }, plugin = {
        "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:", "rerun:target/failed_scenarios.txt",
        "pretty", "json:target/cucumber_reports/Cucumber.json",
        "io.qameta.allure.cucumber4jvm.AllureCucumber4Jvm" }, glue = { "stepDefinition" }, tags = {
        "@SwagLabsAddToCart" }, dryRun = false, monochrome = true, junit = "--step-notifications")

public class TestRunner {

}
