package com.rishi.pages;

import org.openqa.selenium.WebDriver;

import com.rishi.locators.HomePageLocator;
import com.rishi.utility.UIHelper;

public class HomePage extends HomePageLocator {
	public WebDriver driver = null;

	public void launchApplication(String browserType,String url) {
		driver = UIHelper.createDriverInstance(browserType);
		UIHelper.launch(url);
	}
	
	public void clickMusic() {
		UIHelper.click(music);
	}
	

}
