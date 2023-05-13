package com.rishi.utility.extentreports;

import java.util.Date;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ExtentManager {

	private static ExtentReports extentReports;

	private ExtentManager() {
	}

	public synchronized static ExtentReports getInstance() {
		if (extentReports == null) {
			extentReports=new ExtentReports();
			Date d = new Date();
			String fileName = d.toString().replace(":", "_").replace(" ", "_") + ".html";
			ExtentSparkReporter reporter = new ExtentSparkReporter(
					System.getProperty("user.dir") + "/Reports/extentReport_" + fileName);
			reporter.config().setReportName("Extent Test Report");
			reporter.config().setTheme(Theme.DARK);
			reporter.config().setTimelineEnabled(true);
			reporter.config().setTimeStampFormat("MMM dd, yyyy HH:mm:ss");
			extentReports.attachReporter(reporter);
			extentReports.setSystemInfo("Author", "Rishi");
			
		}
		return extentReports;
	}
	
}
