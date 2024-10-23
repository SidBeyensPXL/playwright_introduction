package be.pxl;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import org.junit.jupiter.api.*;

import java.nio.file.Path;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TemplateTest {

	// Shared between all tests in this class.
	private static Playwright playwright;
	private static Browser browser;
	private String URL = "http://localhost:5001/app/catalog.html";

	// New instance for each test method.
	private BrowserContext context;
	private Page page;

	@BeforeAll
	static void launchBrowser() {
		playwright = Playwright.create();
		BrowserType.LaunchOptions options = new BrowserType.LaunchOptions().setHeadless(false);
		browser = playwright.chromium().launch(options);
	}

	@AfterAll
	static void closeBrowser() {
		playwright.close();
	}

	@BeforeEach
	void createContextAndPage() {
		context = browser.newContext();
		page = context.newPage();
	}

	@AfterEach
	void closeContext() {
		context.close();
	}

	@Test // Task 2 in de presentatie
	public void catalogFilterTest() {
		page.navigate(URL);
		page.locator("#filter-text").fill("moon");
		page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Filter")).click();
		Locator rows = page.locator("tbody").locator("tr");
		assertThat(rows).hasCount(1);
		page.screenshot(new Page.ScreenshotOptions().setPath(Path.of("text-transform-capitalize.png")));
	}



	@Test // Task 3 in de presentatie
	public void testPurchaseFlow() {
		page.navigate("http://localhost:5001/app/catalog.html");
		page.getByTestId("purchase-button-1").click();
		String expectedEventName = "Alexander Lemtov Live";
		String actualEventName = page.locator("#event-name").textContent();
		assertEquals(expectedEventName, actualEventName, "Event name should be: Alexander Lemtov Live ");

		page.getByTestId("quantity").selectOption("3");
		page.getByTestId("place-order").click();
		String actualTicketCount = page.locator("div.header-cart p span").textContent();
		String actualTotalAmount = page.getByTestId("event-total").textContent();
		String expectedTicketCount = "3";
		String expectedTotalAmount = "$195.00";
		assertEquals(expectedTicketCount, actualTicketCount, "Ticket count should be 3");
		assertEquals(expectedTotalAmount, actualTotalAmount, "Total amount should match the expected price");
	}

	@Test // Task 4 in de presentatie
	public void testUpdatePurchaseFlow(){
		page.navigate("http://localhost:5001/app/catalog.html");
		page.getByTestId("purchase-button-1").click();
		page.getByTestId("quantity").selectOption("3");
		page.getByTestId("place-order").click();
		page.getByTestId("quantity-dropdown").selectOption("4");
		page.getByTestId("update-button").click();
		String actualTicketCount = page.locator("div.header-cart p span").textContent();
		String actualTotalAmount = page.getByTestId("event-total").textContent();
		String expectedTicketCount = "4";
		String expectedTotalAmount = "$260.00";
		assertEquals(expectedTicketCount, actualTicketCount, "Ticket count should be 4");
		assertEquals(expectedTotalAmount, actualTotalAmount, "Total amount should match the expected price");


	}
}
