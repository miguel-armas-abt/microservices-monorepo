package com.demo.bbq.spiders;

import com.demo.bbq.properties.PropertiesReader;
import com.demo.bbq.properties.configuration.Configuration;
import com.demo.bbq.service.DriverProviderService;
import com.google.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class UnlockSpider {

  private static final String PASSWORD_FIELD = "security-token";

  private final PropertiesReader propertiesReader;
  private final DriverProviderService driverProviderService;

  public void unlock() throws InterruptedException {
    log.info("start {}", this.getClass().getSimpleName());
    ChromeDriver driver = driverProviderService.getChromeDriver();
    Configuration properties = propertiesReader.get().getConfiguration();
    long waitingTimeMillis = properties.getWaitingTimeMillis();

    driver.get(properties.getLogin().getUri());
    Thread.sleep(waitingTimeMillis*2);

    String unlockPassword = properties.getLogin().getUnlockPassword();

    WebElement passwordField = driver.findElement(By.id(PASSWORD_FIELD));
    passwordField.sendKeys(unlockPassword);

    WebElement continueButton = driver.findElement(By.cssSelector("input.btn.btn-primary.set-security-key"));
    continueButton.click();
  }
}