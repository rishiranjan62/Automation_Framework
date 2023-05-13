package com.rishi.utility;

import java.io.IOException;

import org.testng.ITestResult;
import org.testng.log4testng.Logger;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.rishi.utility.extentreports.ExtentManager;



public class LogMe {
	
	private static Logger LOGGER;
	private static ExtentTest extentTest;

	public static ExtentTest getExtentTest() {
		return extentTest;
	}


	@SuppressWarnings("rawtypes")
	public LogMe(Class loggerClass) {
		LOGGER = Logger.getLogger(loggerClass);
	}

	public void logInfo(String message) {
		LOGGER.info("---INFO--- " + message);

		if (extentTest != null) {
			extentTest.log(Status.INFO, "---INFO--- " + message);
		}
	}

	public void logInfo(String message, Throwable t) {
		LOGGER.info("---INFO--- " + message, t);

		if (extentTest != null) {
			extentTest.log(Status.INFO, "---INFO--- " + message + ". Exception message is " + t.getMessage());
		}
	}

	public void logWarn(String message) {
		LOGGER.warn("---WARN--- " + message);

		if (extentTest != null) {
			extentTest.log(Status.WARNING, "---WARN--- " + message);
		}
	}

	public void logWarn(String message, String screenshotPath) {
		LOGGER.warn("---WARN--- " + message);

		if (extentTest != null) {
			extentTest.log(Status.WARNING, "---WARN--- " + message + extentTest.addScreenCaptureFromBase64String(screenshotPath));
		}
	}

	public void logWarn(String message, Throwable t, String... screenshotPath) {
		LOGGER.warn("---WARN--- " + message, t);

		if (extentTest != null) {
			if (screenshotPath == null) {
				extentTest.log(Status.WARNING, "---WARN--- " + message + ". Exception message is " + t.getMessage());
			} else {
				extentTest.log(Status.WARNING, "---WARN--- " + message + ". Exception message is " + t.getMessage()
						+ extentTest.addScreenCaptureFromBase64String(screenshotPath[0]));
			}
		}
	}

	public void logError(String message) {
		LOGGER.error("---ERROR--- " + message);

		if (extentTest != null) {
			extentTest.log(Status.FAIL, "---ERROR--- " + message);
		}
	}

	public void logError(String message, String screenshotPath) {
		LOGGER.error("---ERROR--- " + message);

		if (extentTest != null) {
			extentTest.log(Status.FAIL, "---ERROR--- " + message + extentTest.addScreenCaptureFromBase64String(screenshotPath));
		}
	}

	public void logError(String message, Throwable t, String... screenshotPath) {
		LOGGER.error("---ERROR--- Exception " + t.getClass().getSimpleName() + " encountered");
		LOGGER.error("---ERROR--- " + message, t);

		if (extentTest != null) {
			if (screenshotPath == null) {
				extentTest.log(Status.FAIL, "---ERROR--- " + message + ". Exception message is " + t.getMessage());
			} else {
				extentTest.log(Status.FAIL, "---ERROR--- " + message + ". Exception message is " + t.getMessage()
						+ extentTest.addScreenCaptureFromBase64String(screenshotPath[0]));
			}
		}
	}

	public void logAssert(boolean assertion, String msg) {
		if (assertion) {
			msg = "---INFO--- " + msg;

			if (extentTest != null) {
				extentTest.log(Status.INFO, msg);
			}
		} else {
			msg = "---ERROR--- " + msg;

			if (extentTest != null) {
				extentTest.log(Status.FAIL, msg);
			}
		}
		//LOGGER.assertLog(assertion, msg);
	}

	public void logBeginTestCase(String tsName) {
		LOGGER.info("---INFO---Strating test case " + tsName);
		extentTest = ExtentManager.getInstance().createTest(tsName);
	}

	public void logBeginTestCase(String tsName, String desc) {
		LOGGER.info("---INFO---Strating Test Case : " + tsName);
		LOGGER.info("---INFO---Test Case Description : " + desc);
		extentTest = ExtentManager.getInstance().createTest(tsName, desc);
	}

	public void logEndTestCase(String tsName) {
		LOGGER.info("---INFO---Test Case : " + tsName + " finished");
		ExtentManager.getInstance().flush();
	}

//	public void logTestStep(String stepNo, String desc) {
//		LOGGER.info("---INFO---Step No. " + stepNo + " : Description : " + desc);
//		//extentTest.log(Status.INFO, stepNo, desc);
//	}

	public void logTestStep(String status, String message, Throwable... throwables) throws IOException {
		Status stepStatus = Status.valueOf(status.toUpperCase());

		switch (stepStatus) {
		case PASS:
			if (throwables.length != 0) {
				LOGGER.info(
						"---PASS---" + message + " Exception " + throwables[0].getClass().getSimpleName() + " occured");
				extentTest.log(Status.PASS,
						"---PASS---" + message + " Exception " + throwables[0].getClass().getSimpleName() + " occured");
			} else {
				LOGGER.info("---PASS---" + message);
				extentTest.log(Status.PASS, "---PASS---" + message);
			}
			break;
		case FAIL:
			if (throwables.length != 0) {
				logError("---FAIL---" + message, throwables[0]);
			} else {
				logError("---FAIL---" + message);
			}
			break;
		case SKIP:
		case WARNING:
			if (throwables.length != 0) {
				logWarn("---WARNING---" + message, throwables[0]);
			} else {
				logWarn("---WARNING---" + message);
			}
			break;
		default:
			break;
		}
	}

	public void logTestStep(String status, String message, String filePath, Throwable... throwables) {
		Status stepStatus = Status.valueOf(status.toUpperCase());

		switch (stepStatus) {
		case PASS:
			if (throwables.length != 0) {
				LOGGER.info(
						"---PASS---" + message + " Exception " + throwables[0].getClass().getSimpleName() + " occured");
				extentTest.log(Status.PASS,
						"---PASS---" + message + " Exception " + throwables[0].getClass().getSimpleName() + " occured"
								+ extentTest.addScreenCaptureFromBase64String(filePath));
			} else {
				LOGGER.info("---PASS---" + message);
				extentTest.log(Status.PASS, "---PASS---" + message + extentTest.addScreenCaptureFromBase64String(filePath));
			}
			break;
		case FAIL:
			if (throwables.length != 0) {
				logError("---FAIL---" + message, throwables[0], filePath);
			} else {
				logError("---FAIL---" + message, filePath);
			}
			break;
		case SKIP:
		case WARNING:
			if (throwables.length != 0) {
				logWarn("---WARNING---" + message, throwables[0], filePath);
			} else {
				logWarn("---WARNING---" + message, filePath);
			}
			break;
		default:
			break;
		}
	}

}
