package com.saucedemo.tests;

import com.pages.LoginPage;
import com.pages.ProductsPage;
import com.saucedemo.WebTestsBase;
import com.saucelabs.saucebindings.SauceOptions;
import com.saucelabs.saucebindings.SauceSession;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class DesktopTests extends WebTestsBase {
    @Before
    public void setUp() {
        SauceOptions sauceOptions = new SauceOptions();
        sauceOptions.setCapability("browserName", browserName);
        sauceOptions.setCapability("browserVersion", browserVersion);
        sauceOptions.setCapability("platformName", platform);
        sauceOptions.setName(testName.getMethodName());
        sauceOptions.setBuild(buildName);

        SauceSession session = new SauceSession(sauceOptions);
        driver = session.start();
        resultReportingTestWatcher.setDriver(driver);
    }

    @Test
    public void loginWorks() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.visit();
        loginPage.login("standard_user");
        assertTrue(new ProductsPage(driver).isDisplayed());
    }

    /*
     * Configure our data driven parameters
     * */
    @Parameterized.Parameter
    public String browserName;

    @Parameterized.Parameter(1)
    public String browserVersion;

    @Parameterized.Parameter(2)
    public String platform;


    @Parameterized.Parameters()
    public static Collection<Object[]> crossBrowserData() {
        return Arrays.asList(new Object[][] {
                { "chrome", "latest", "Windows 10" },
                { "chrome", "latest-1", "Windows 10" },
                { "safari", "latest", "macOS 10.15" },
                { "chrome", "latest", "macOS 10.14" },
                // Duplication below for demo purposes of massive parallelization
                { "chrome", "latest", "Windows 10" },
                { "chrome", "latest-1", "Windows 10" },
                { "safari", "latest", "macOS 10.15" },
                { "chrome", "latest", "macOS 10.14" },
                { "chrome", "latest", "Windows 10" },
                { "chrome", "latest-1", "Windows 10" },
                { "safari", "latest", "macOS 10.15" },
                { "chrome", "latest", "macOS 10.14" },
                { "chrome", "latest", "Windows 10" },
                { "chrome", "latest-1", "Windows 10" },
                { "safari", "latest", "macOS 10.15" },
                { "chrome", "latest", "macOS 10.14" },
                { "chrome", "latest", "Windows 10" },
                { "chrome", "latest-1", "Windows 10" },
                { "safari", "latest", "macOS 10.15" },
                { "chrome", "latest", "macOS 10.14" },
        });
    }
}
