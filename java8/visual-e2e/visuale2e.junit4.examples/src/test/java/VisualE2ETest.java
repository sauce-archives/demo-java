import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class VisualE2ETest {

    protected WebDriver webDriver;
    public String sauceUsername = System.getenv("SAUCE_USERNAME");
    public String sauceAccessKey = System.getenv("SAUCE_ACCESS_KEY");
    public String screenerApiKey = System.getenv("SCREENER_API_KEY");
    public MutableCapabilities capabilities = new MutableCapabilities();

    @Before
    public void setUp() throws Exception {
        if (sauceUsername == null || sauceUsername.isEmpty()) {
            throw new Exception("SAUCE_USERNAME environment variable needs to be defined");
        }

        if (sauceAccessKey == null || sauceAccessKey.isEmpty()) {
            throw new Exception("SAUCE_ACCESS_KEY environment variable needs to be defined");
        }

        if (screenerApiKey == null || screenerApiKey.isEmpty()) {
            throw new Exception("SCREENER_API_KEY environment variable needs to be defined");
        }

        capabilities.setCapability(CapabilityType.BROWSER_NAME, "chrome");
        capabilities.setCapability(CapabilityType.BROWSER_VERSION, "80.0");
        capabilities.setCapability(CapabilityType.PLATFORM_NAME, "Windows 10");

        MutableCapabilities sauceOptions = new MutableCapabilities();
        sauceOptions.setCapability("username", sauceUsername);
        sauceOptions.setCapability("accesskey", sauceAccessKey);
        capabilities.setCapability("sauce:options", sauceOptions);

        MutableCapabilities visualOptions = new MutableCapabilities();
        visualOptions.setCapability("apiKey", screenerApiKey);
        visualOptions.setCapability("projectName", "visual-e2e-test");
        visualOptions.setCapability("viewportSize", "1280x1024");
        capabilities.setCapability("sauce:visual", visualOptions);

        URL url = new URL("https://hub.screener.io/wd/hub");
        webDriver = new RemoteWebDriver(url, capabilities);
    }

    @Test
    public void testVisualE2E() {
        webDriver.get("https://screener.io");

        JavascriptExecutor js = (JavascriptExecutor) webDriver;
        js.executeScript("/*@visual.init*/", "My Visual Test");
        js.executeScript("/*@visual.snapshot*/", "Home");
        Map<String, Object> response = (Map<String, Object>) js.executeScript("/*@visual.end*/");
        assertEquals( true, response.get("passed"));
    }
}
