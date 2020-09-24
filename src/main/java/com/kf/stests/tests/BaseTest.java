package com.kf.stests.tests;

import com.codeborne.selenide.WebDriverRunner;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Properties;

public class BaseTest {
    public final Logger logger = LogManager.getLogger(getClass());

    public BaseTest() {
        Properties prop = new Properties();

        try {
            prop.load(new FileInputStream("settings.properties"));
            for (String name : prop.stringPropertyNames()) {
                String value = prop.getProperty(name);
                System.setProperty(name, value);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
//        chromeOptions = new ChromeOptions();
//        chromeOptions.addArguments("user-agent=bot Googlebot selenium webdriver selenium-bot webdriver-bot");
//        chromeOptions.setCapability("version", "85");
//    }
//    @BeforeMethod(alwaysRun = true)
//    public void prepare() throws MalformedURLException {
//        RemoteWebDriver driver = new RemoteWebDriver(new URL("http://188.213.168.204:4444/wd/hub"), chromeOptions);
//        WebDriverRunner.setWebDriver(driver);
//    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        if (!result.isSuccess()) {
            String location = System.getProperty("user.dir") + "/out/screenshots/" + System.getProperty("OUT") + "/";  //location for images
            String methodName = result.getName(); // fetching test method name
            File outFile = new File(location + '/' + methodName + "_" + LocalDateTime.now().toString().replaceAll("[: ]", "-") + ".png");
            outFile.getParentFile().mkdirs();
            try {
                File screenshots = ((TakesScreenshot) WebDriverRunner.getWebDriver())
                        .getScreenshotAs(OutputType.FILE);
                FileUtils.copyFile(
                        screenshots,
                        outFile
                );
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @AfterClass(alwaysRun = true)
    public void tearDown() {
        WebDriverRunner.closeWebDriver();
    }

    @AfterSuite
    public void after(ITestContext result) throws IOException {
        String message = result.getFailedTests().size() > 0 ? "FAILED" : "Passed";
        Files.write(Paths.get("result.log"), List.of(message));
    }
}
