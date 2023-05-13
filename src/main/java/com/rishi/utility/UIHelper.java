package com.rishi.utility;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class UIHelper {

	private static WebDriver driver = null;
	public static Properties config;

	static {
		config = new Properties();
		FileInputStream propsInput = null;
		try {
			propsInput = new FileInputStream(System.getProperty("user.dir") + "/src/main/resources/config.properties");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			config.load(propsInput);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// create the instance of driver
	public static WebDriver createDriverInstance(String BrowserType) {
		switch (BrowserType.toUpperCase()) {
		case "FIREFOX":
			driver = new FirefoxDriver();
			break;
		case "CHROME":
			driver = new ChromeDriver();
			break;
		case "EDGE":
			driver = new EdgeDriver();
			break;
		case "SAFARI":
			driver = new SafariDriver();
			break;
		case "IE":
			driver = new InternetExplorerDriver();
			break;
		}
		maximize_window();
		return driver;
	}

	// maximize the window
	public static void maximize_window() {
		driver.manage().window().maximize();
	}

	// minimize the window
	public static void minimize_window() {
		driver.manage().window().minimize();
	}

	// execute javaScript
	public static Object executeScript(String script, Object... obj) {
		return ((JavascriptExecutor) driver).executeScript(script, obj);

	}

	// execute javaScript Async
	public static Object executeAsyncScript(String script, Object... args) {
		return ((JavascriptExecutor) (driver)).executeAsyncScript(script, args);
	}

	// highlight element
	public static void highlightElement(WebElement element) {
		executeScript("arguments[0].setAttribute('style', 'border: 2px solid blue;');", element);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {

		}
		executeScript("arguments[0].setAttribute('style', 'border: 3px solid blue;');", element);

	}

	public static void click(By by) {
		WebElement ele = findElement(by);
		highlightElement(ele);
		ele.click();
	}

	public static void clickIfPresentWithWait(By by, Duration waitTimeInSec) {
		WebElement ele = waitForElementPresent(by, waitTimeInSec);
		highlightElement(ele);
		ele.click();
	}

	public static WebElement findElement(By by) {
		return driver.findElement(by);
	}

	public static List<WebElement> findElements(By by) {
		return driver.findElements(by);
	}

	public static WebElement waitForElementPresent(By by, Duration waitTime) {
		return new WebDriverWait(driver, waitTime).until(ExpectedConditions.visibilityOfElementLocated(by));
	}

	public static void closeWindow() {
		driver.close();
	}

	public static void closeAllWindow() {
		driver.quit();
	}

	public static void launch(String url) {
		driver.navigate().to(url);
	}

	public static void selectFromDropdown(By by, String value, String method) {
		Select select = new Select(findElement(by));
		switch (method.toUpperCase()) {
		case "VALUE":
			select.selectByValue(value);
			break;
		case "VISIBLETEXT":
			select.selectByVisibleText(value);
			break;
		case "INDEX":
			select.selectByIndex(Integer.parseInt(value));
			break;

		}
	}

	public static void deSelectFromDropdown(By by, String value, String method) {
		Select select = new Select(findElement(by));
		switch (method.toUpperCase()) {
		case "VALUE":
			select.deselectByValue(value);
			break;
		case "VISIBLETEXT":
			select.deselectByVisibleText(value);
			break;
		case "INDEX":
			select.deselectByIndex(Integer.parseInt(value));
			break;
		case "ALL":
			select.deselectAll();
			break;

		}
	}

	public static List<String> getSelectedOptionTextFromDropdown(By by) {
		Select select = new Select(findElement(by));
		List<WebElement> selectedOptions = select.getAllSelectedOptions();
		List<String> selectedOptionsText = new ArrayList<String>();
		for (WebElement element : selectedOptions) {
			selectedOptionsText.add(element.getText());
		}
		return selectedOptionsText;
	}

	public static List<String> getAllOptionTextFromDropdown(By by) {
		Select select = new Select(findElement(by));
		List<WebElement> options = select.getOptions();
		List<String> optionsText = new ArrayList<String>();
		for (WebElement element : options) {
			optionsText.add(element.getText());
		}
		return optionsText;
	}

	public static boolean isAlertPresent() {
		boolean result = false;
		try {
			driver.switchTo().alert();
			result = true;
			driver.switchTo().defaultContent();
		} catch (NoAlertPresentException e) {
		}
		return result;
	}

	public static void scrollWindow(String direction) {
		if (direction.equalsIgnoreCase("Up")) {
			executeScript("scroll(250, 0)");
		} else {
			executeScript("scroll(0, 250)");
		}
	}

	public static Alert getAlert(int... waitTime) {
		if (waitTime.length == 0)
			return new WebDriverWait(driver, Duration.ofSeconds(waitTime[0]))
					.until(ExpectedConditions.alertIsPresent());
		else
			return new WebDriverWait(driver, Duration.ofSeconds(Integer.valueOf(config.getProperty(""))))
					.until(ExpectedConditions.alertIsPresent());
	}

	public static void alertAccept(WebDriver driver) throws Exception {
		getAlert().accept();
		driver.switchTo().defaultContent();
	}

	public static void alertDismiss(WebDriver driver) throws Exception {
		getAlert().dismiss();
		driver.switchTo().defaultContent();
	}

	public static void alertAccept(WebDriver driver, int waitTime) {
		getAlert(waitTime).accept();
		driver.switchTo().defaultContent();
	}

	public static void alertDismiss(WebDriver driver, int waitTime) {
		getAlert(waitTime).dismiss();
		driver.switchTo().defaultContent();
	}

	public static void inputText(By by, String data) {
		WebElement element = findElement(by);
		element.clear();
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
		}
		highlightElement(element);
		element.sendKeys(data);
	}

	public static String getText(By by) {
		WebElement element = findElement(by);
		highlightElement(element);
		return element.getText();
	}

	public static List<String> getTexts(By by) {
		List<WebElement> elements = findElements(by);
		List<String> elementsText = new LinkedList<String>();
		for (WebElement ele : elements) {
			highlightElement(ele);
			elementsText.add(ele.getText());
		}
		return elementsText;
	}

	public static void moveToElement(By by) {
		WebElement element = findElement(by);
		highlightElement(element);
		new Actions(driver).moveToElement(element).perform();
	}

	public static void doubleClick(By by) {
		WebElement element = findElement(by);
		highlightElement(element);
		new Actions(driver).doubleClick(element).perform();
	}

	public static void rightClick(By by) {
		WebElement element = findElement(by);
		highlightElement(element);
		new Actions(driver).contextClick(element).perform();
	}

	public static void dragAndDropElement(By from, By to) {
		WebElement fromElement = findElement(from);
		WebElement toElement = findElement(to);
		new Actions(driver).dragAndDrop(fromElement, toElement).perform();
	}

	public static String switchToChildWindow() {
		String parentWindow = driver.getWindowHandle();
		Set<String> handles = driver.getWindowHandles();
		for (String window : handles) {
			if (!window.equals(parentWindow))
				driver.switchTo().window(window);
		}
		return parentWindow;
	}

	public static void switchToWindow(String window) {
		driver.switchTo().window(window);
	}

	public static void switchToFrame(String method, String value, By by) {
		switch (method.toUpperCase()) {
		case "INDEX":
			driver.switchTo().frame(Integer.parseInt(value));
			break;
		case "NAME":
			driver.switchTo().frame(value);
			break;
		case "WEBELEMENT":
			driver.switchTo().frame(driver.findElement(by));
			break;
		}
	}

	public static void switchBack() {
		driver.switchTo().defaultContent();
	}

	public static void clickElementJScript(By by) {
		WebElement element = null;

		try {
			element = findElement(by);
		} catch (Exception e) {
			// LOGGER.error("Element located by " + by + " not found.", e);
		}

		clickElementJScript(element);
	}

	public static void clickElementJScript(WebElement element) {
		executeScript("arguments[0].scrollIntoView(true);", element);
		executeScript("arguments[0].click();", element);
	}

	public static void fileUpload(String filePath) throws AWTException, InterruptedException {
		StringSelection selection = new StringSelection(filePath);
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents(selection, selection);
		Robot robot = new Robot();
		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_V);
		robot.keyRelease(KeyEvent.VK_V);
		Thread.sleep(6000);
		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_ENTER);
		robot.keyRelease(KeyEvent.VK_CONTROL);
		robot.keyRelease(KeyEvent.VK_ENTER);
	}

	public static Map<String, List<WebElement>> getGridCellsColWise(String tableXPath) throws Exception {
		Map<String, List<WebElement>> gridElements = new HashMap<String, List<WebElement>>();

		String colXPath = tableXPath + "//th";
		List<WebElement> colElements = findElements(By.xpath(colXPath));

		int colNum = 1;

		for (WebElement colHeader : colElements) {
			String rowXPath = tableXPath + "//tbody/tr[" + colNum + "]";
			List<WebElement> gridCells = findElement(By.xpath(rowXPath)).findElements(By.xpath(".//td"));

			// gridElements.put(getChildText(colHeader), gridCells);
		}
		return gridElements;
	}

	public static Map<String, List<String>> getGridCellValuesColWise(WebDriver driver, String tableXPath)
			throws Exception {
		Map<String, List<WebElement>> gridElements = getGridCellsColWise(tableXPath);
		Map<String, List<String>> gridCellValues = new HashMap<String, List<String>>();

		Set<String> headers = gridElements.keySet();

		for (String header : headers) {
			List<WebElement> colElements = gridElements.get(header);
			List<String> colValues = new ArrayList<String>();

			for (WebElement cell : colElements) {
				// colValues.add(getChildText(cell));
			}
			gridCellValues.put(header, colValues);
		}

		return gridCellValues;
	}

	public static void inputTextJScript(By by, String data) {
		WebElement element = findElement(by);
		element.clear();
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
		}
		executeScript("arguments[0].scrollIntoView(true);", element);
		executeScript("arguments[0].setAttribute('value', '" + data + "');", element);
	}

	public static void clickEnter(By by) throws Exception {
		WebElement element = findElement(by);
		new Actions(driver).moveToElement(element).build().perform();
		element.sendKeys(Keys.ENTER);
	}

	public static String takeScreenshot(String methodName) throws IOException {
		Date d = new Date();
		String filePath = System.getProperty("user.dir") + "/Screenshot/" + methodName
				+ d.toString().replace(":", "_").replace(" ", "_") + ".png";
		File file = new File(filePath);
		TakesScreenshot ts = (TakesScreenshot) driver;
		File srcFile = ts.getScreenshotAs(OutputType.FILE);
		FileHandler.copy(srcFile, file);
		return filePath;
	}

}
