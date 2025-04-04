package com.gmail.ek.rudenko.page;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.openqa.selenium.support.ui.ExpectedConditions.*;

public class CalculatorPage {
    private final WebDriver driver;
    private final WebDriverWait wait;
    private final String URL = "https://www.dhl.com/se-en/home/freight/tools/european-road-freight-transit-time-calculator.html";
    private final By originCountryDropdown = By.id("origin-country");
    private final By originPostcodeField = By.id("origin-postcode");
    private final By destinationCountryDropdown = By.id("destination-country");
    private final By destinationPostcodeField = By.id("destination-postcode");
    private final By calculateButton = By.cssSelector("button[class*='input-submit']");
    private final By transitTimeResultSection = By.cssSelector("div[class*='options']:nth-of-type(2)");
    private final By editPreviousStepButton = By.cssSelector("p[class*='goto-previous']");
    private final By strictlyNecessaryOnlyButton = By.cssSelector("div#onetrust-button-group #onetrust-reject-all-handler");
    private final By productOptionBoxes = By.cssSelector("div[class*='productdetail']");
    private final By pickupDatePicker = By.id("leadtime-datepicker");
    private final By deliveryDatePicker = By.id("leadtime-datepicker2");

    public CalculatorPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public CalculatorPage open() {
        driver.get(URL);
        driver.manage().window().maximize();
        WebElement button = wait.until(elementToBeClickable(strictlyNecessaryOnlyButton));
        button.click();
        return this;
    }

    public CalculatorPage selectOriginCountry(String country) {
        WebElement dropdown = wait.until(elementToBeClickable(originCountryDropdown));
        new Select(dropdown).selectByVisibleText(country);
        return this;
    }

    public CalculatorPage enterOriginPostcode(String postcode) {
        WebElement field = wait.until(visibilityOfElementLocated(originPostcodeField));
        field.clear();
        field.sendKeys(postcode);
        return this;
    }

    public CalculatorPage selectDestinationCountry(String country) {
        WebElement dropdown = wait.until(elementToBeClickable(destinationCountryDropdown));
        new Select(dropdown).selectByVisibleText(country);
        return this;
    }

    public CalculatorPage enterDestinationPostcode(String postcode) {
        WebElement field = wait.until(visibilityOfElementLocated(destinationPostcodeField));
        field.clear();
        field.sendKeys(postcode);
        return this;
    }

    public CalculatorPage clickCalculateButton() {
        WebElement button = wait.until(presenceOfElementLocated(calculateButton));
        //brings the desired element within the viewport
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", button);

        new Actions(driver)
                .moveToElement(button)
                .perform();
        button.click();

        return this;
    }

    public boolean isTransitTimeResultSectionVisible() {
        try {
            WebElement section = wait.until(visibilityOfElementLocated(transitTimeResultSection));
            return section.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isErrorDisplayedForField(By fieldLocator) {
        String value = fieldLocator.toString();
        String fieldId = value.substring(value.indexOf(":") + 2);
        String errorSelector = "#" + fieldId + " + p[class*='zip-error-message']";

        try {
            WebElement error = wait.until(visibilityOfElementLocated(By.cssSelector(errorSelector)));
            return error.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isOriginPostcodeErrorVisible() {
        return isErrorDisplayedForField(originPostcodeField);
    }

    public boolean isDestinationPostcodeErrorVisible() {
        return isErrorDisplayedForField(destinationPostcodeField);
    }

    public CalculatorPage clickEditPreviousStepButton() {
        WebElement button = wait.until(elementToBeClickable(
                driver.findElement(transitTimeResultSection).findElement(editPreviousStepButton)
        ));
        button.click();
        return this;
    }

    public int getProductOptionCount() {
        return driver.findElements(productOptionBoxes).size();
    }

    public boolean isPickupDatePickerVisible() {
        try {
            WebElement picker = wait.until(visibilityOfElementLocated(pickupDatePicker));
            return picker.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isDeliveryDatePickerVisible() {
        try {
            WebElement picker = wait.until(visibilityOfElementLocated(deliveryDatePicker));
            return picker.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}