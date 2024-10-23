package be.pxl;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

public class FirstPlaywrightTest {

	@Test
	public void firstTest() {
		try (Playwright pw = Playwright.create()) { // guarantee the browser and pages will be closed in the end
			BrowserType.LaunchOptions launchOptions = new BrowserType.LaunchOptions().setHeadless(false);
			Browser browser = pw.chromium().launch(launchOptions);
			Page page = browser.newPage();
			page.navigate("https://www.lambdatest.com/");
			System.out.println("title " + page.title());
		}
		// playwright runs in headless mode by default
	}


	@Test
	public void differentBrowsers() {
		try (Playwright pw = Playwright.create()) { // guarantee the browser and pages will be closed in the end
			List<BrowserType> browserTypes = Arrays.asList(pw.chromium(), pw.firefox(), pw.webkit());
			for (BrowserType bt : browserTypes) {
				Page page = bt.launch().newPage();
				page.navigate("https://www.whatsmybrowser.org");
				page.screenshot(new Page.ScreenshotOptions().setPath(Path.of(bt.name() + ".png")));
			}
		}
	}

	@Test
	public void getValue(){
		try (Playwright pw = Playwright.create()) {
			// BrowserType.LaunchOptions launchOptions = new BrowserType.LaunchOptions().setHeadless(false);
			Browser browser = pw.chromium().launch();
			Page page = browser.newPage();
			page.navigate("http://localhost:5001/app/catalog.html");
			// Als je begint met . dan is het een class, # is voor id
			page.locator(".event-name").all().forEach(
					l -> System.out.println(l.innerText())
			);
		}
	}
}
