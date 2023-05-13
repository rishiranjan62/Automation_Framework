package com.rishi.test;

import java.lang.reflect.Method;

import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.rishi.pages.HomePage;
import com.rishi.utility.Constant;
import com.rishi.utility.LogMe;
import com.rishi.utility.UIHelper;
import com.rishi.utility.extentreports.ExtentManager;

public class SampleTest {
	static LogMe LOGGER = new LogMe(SampleTest.class);
	HomePage homePage;

	@BeforeTest
	public void beforeTest() {
		LOGGER.logInfo("*********EXECUTION STARTED**********\n\n");
	}

	@BeforeMethod
	public void startReporting(Method method) {
		LOGGER.logBeginTestCase(method.getName());
	}

	@Test
	public void sampletest() throws Exception {
		homePage = new HomePage();
		homePage.launchApplication(UIHelper.config.getProperty("Browser"), Constant.url);
		LOGGER.logTestStep("PASS", "Login successful As Local Admin and Dashboard page is getting displayed");
		homePage.clickMusic();
		
	
	}

	@Test
	public void sampletest1() throws Exception {
		homePage = new HomePage();
		homePage.launchApplication(UIHelper.config.getProperty("Browser"), Constant.url);
		LOGGER.logTestStep("PASS", "Login successful As Local Admin and Dashboard page is getting displayed");
		homePage.clickMusic();
		Assert.assertTrue(false);
	}

	@AfterMethod(alwaysRun=true)
	public void testResult(ITestResult result) throws Exception {
		String filePath;
		switch (result.getStatus()) {
		case ITestResult.SUCCESS:
			LogMe.getExtentTest().log(Status.PASS, "Test Case " + result.getName() + " is pass");
			break;
		case ITestResult.FAILURE:
			filePath = UIHelper.takeScreenshot(result.getName());
			LogMe.getExtentTest().log(Status.FAIL, "Test Case " + result.getName() + " failed",
					MediaEntityBuilder.createScreenCaptureFromPath(filePath).build());
			break;
		case ITestResult.SKIP:
			LogMe.getExtentTest().log(Status.SKIP, "Test Case " + result.getName() + " skiped");
		default:
			break;
		}
		LOGGER.logEndTestCase(result.getName());
	}

	@AfterSuite
	public void generateResult() {
		ExtentManager.getInstance().flush();
		UIHelper.closeWindow();
	}
}
