package ios.tests;

import io.appium.java_client.ios.IOSDriver;
import ios.pages.SwagLabsPage;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;

import static helpers.utils.getProperty;

public class SwagLabsTest {

    protected IOSDriver driver;
    String sessionId;
    Boolean rdc;
    Boolean sauce;

    @BeforeMethod
    public void setup(Method method) throws MalformedURLException {

        System.out.println("Sauce - BeforeMethod hook");

        String region = getProperty("region", "eu");
        rdc = Boolean.parseBoolean(getProperty("rdc", "true"));

        String username = System.getenv("SAUCE_USERNAME");
        String accesskey = System.getenv("SAUCE_ACCESS_KEY");
        String methodName = method.getName();

        String sauceUrl;

        MutableCapabilities capabilities = new MutableCapabilities();


        if (region.equalsIgnoreCase("eu")) {
            sauceUrl = "@ondemand.eu-central-1.saucelabs.com:443";
        } else {
            sauceUrl = "@ondemand.us-west-1.saucelabs.com:443";
        }

        String SAUCE_REMOTE_URL = "https://" + username + ":" + accesskey + sauceUrl + "/wd/hub";
        URL url = new URL(SAUCE_REMOTE_URL);


        if (rdc == true) {
            String appName ="iOS.RealDevice.SauceLabs.Mobile.Sample.app.2.3.0.ipa";
            System.out.println("Sauce - Run on real device");
            capabilities.setCapability("deviceName", "iPhone 8*");
            // Enable touchID
            capabilities.setCapability("allowTouchIdEnroll", true);
            capabilities.setCapability("app", "storage:filename=" + appName);
        } else { // Simulator and Emulator
            String appName = "iOS.Simulator.SauceLabs.Mobile.Sample.app.2.3.0.zip";
            System.out.println("Sauce - Run on virtual mobile device");
            capabilities.setCapability("deviceName", "iPhone 8 Simulator");
            capabilities.setCapability("platformVersion", "13.4");
            capabilities.setCapability("app", "sauce-storage:" + appName);
        }
        capabilities.setCapability("platformName", "iOS");
        capabilities.setCapability("automationName", "XCUITEST");
        capabilities.setCapability("name", methodName);



        // Launch remote browser and set it as the current thread
        driver = new IOSDriver(url, capabilities);
        sessionId = ((RemoteWebDriver) driver).getSessionId().toString();
    }


    @Test
    public void Biometric_login_with_matching_touch () throws InterruptedException {
        System.out.println("Sauce - start test Biometric login with matching touch");

        // init
        SwagLabsPage page = new SwagLabsPage(driver);

        // If the biometry is not shown on iOS, enable it on the phone
        if (page.isBiometryDisplayed() == false){
            System.out.println("Sauce - Need to enable the biometry and restart the device");
            driver.toggleTouchIDEnrollment(true);
            driver.resetApp();

        }

        // Login with biometric auth
        page.login(true);

        // Verificsation
        Assert.assertTrue(page.isOnProductsPage());

    }

    @Test
    public void Biometric_login_with_non_matching_touch () throws InterruptedException {
        System.out.println("Sauce - start test Biometric login with a non matching touch");

        // init
        SwagLabsPage page = new SwagLabsPage(driver);

        // If the biometry is not shown on iOS, enable it on the phone
        if (page.isBiometryDisplayed() == false){
            System.out.println("Sauce - Need to enable the biometry and restart the device");
            driver.toggleTouchIDEnrollment(true);
            driver.resetApp();

        }

        // Login with biometric auth
        page.login(false);

        // Verificsation
        Assert.assertTrue(page.isRetryBiometryDisplay(rdc));

    }

    @AfterMethod
    public void teardown(ITestResult result) {
        System.out.println("Sauce - AfterMethod hook");
        ((JavascriptExecutor)driver).executeScript("sauce:job-result=" + (result.isSuccess() ? "passed" : "failed"));
        driver.quit();
    }

}