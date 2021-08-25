package stepDefinition;

import com.swaglabs.commonfunctions.CommonFunctions;
import com.swaglabs.context.TestContext;
import com.swaglabs.pages.LoginPage;
import com.swaglabs.pages.ProductCataloguePage;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class StepDefinitions {
	private LoginPage loginPage;
	private ProductCataloguePage productCataloguePage;
	TestContext testContext;
	CommonFunctions comfun = Setup.com_fun;

	public StepDefinitions(TestContext context) {
		testContext = context;
		loginPage = testContext.getPageObjectManagerInstance().getloginPage();
		productCataloguePage = testContext.getPageObjectManagerInstance().getDashboardPage();

	}

	@Given("Launch the mobile app and validate Welcome Page")
	public void Launch_the_mobile_app_and_validate_Welcome_Page() throws InterruptedException {
		loginPage.verifyWelcomePageOfTheApplication(comfun);
	}

	@When("Login to the application with {string} and {string}")
	public void Login_to_the_application(String userName, String password) throws InterruptedException {
		userName = Setup.CONFIG.getProperty("USERNAME");
		password = Setup.CONFIG.getProperty("PASSWORD");
		loginPage.loginToTheApplication(comfun, userName, password);
	}

	@Then("Validate user is landed on Dashboard of the application")
	public void Validate_user_is_landed_on_Dashboard_of_the_application() throws InterruptedException {
		productCataloguePage.validateDashboardPageisdisplayed(comfun);
	}

	@When("user added a product")
	public void user_added_a_product() {
		productCataloguePage.addAProductinCart(comfun);
	}

	@When("Add and validate added Product in the cart")
	public void Add_and_validate_added_Product_in_the_cart() throws InterruptedException {
		productCataloguePage.addandValidateTheProductIntheCart(comfun);
	}

	@And("User navigated back to the dashboard")
	public void validate_added_product_in_the_cart() {
		comfun.navigateBack();
	}

	@When("Delete a product and validate its deleted from cart")
	public void Delete_a_product_and_validate_its_deleted_from_cart() throws InterruptedException {
		productCataloguePage.deleteAProductUsingProductName(comfun);
	}

	@When("Add and delete functionality while checkingout")
	public void Add_and_delete_functionality_while_checkingout() throws InterruptedException {
		productCataloguePage.addDeleteProduct(comfun);
	}

	@When("validate Deleted product")
	public void validate_Deleted_product() throws InterruptedException {
		productCataloguePage.validateDeletedProduct(comfun);
	}

}
