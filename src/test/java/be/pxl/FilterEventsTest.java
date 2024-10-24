package be.pxl;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.AriaRole;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

class FilterEventsTest {

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


	@Test
	public void catalogFilterTest() {
		page.navigate(URL);
		page.locator("#filter-text").fill("moon");
		page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Filter")).click();
		// Find all <tr> elements inside the tbody
		Locator rows = page.locator("tbody").locator("tr");
		// Assert that the number of rows is exactly 1
		assertThat(rows).hasCount(1);
		assertThat(page.getByTestId("event-name-2")).hasText("To the Moon and Back");
		// text has style text-transform capitalize
		assertThat(page.getByTestId("event-name-2")).hasCSS("text-transform", "capitalize");
		page.screenshot(new Page.ScreenshotOptions().setPath(Path.of("text-transform-capitalize.png")));
		System.out.println(page.getByTestId("event-name-2").textContent());
	}

}
