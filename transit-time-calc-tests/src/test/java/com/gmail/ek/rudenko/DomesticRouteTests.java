package com.gmail.ek.rudenko;

import com.gmail.ek.rudenko.model.TestCaseData;
import com.gmail.ek.rudenko.page.CalculatorPage;
import com.gmail.ek.rudenko.util.PostcodeResolver;
import io.qameta.allure.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import static io.qameta.allure.SeverityLevel.NORMAL;
import static io.qameta.allure.SeverityLevel.CRITICAL;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Epic("European Road Freight Transit Time Calculator")
@Feature("Domestic Routes (Sweden -> Sweden)")
public class DomesticRouteTests {
    private WebDriver driver;
    private CalculatorPage calculatorPage;

    @BeforeEach
    public void setup() {
        driver = new ChromeDriver();
        calculatorPage = new CalculatorPage(driver);
    }
    @Severity(CRITICAL)
    @Description("Sweden -> Sweden | Successful cases should show transit time result")
    @ParameterizedTest(name = "{0}")
    @MethodSource("com.gmail.ek.rudenko.util.TestCaseDataLoader#successfulCasesDomestic")
    public void testSuccessfulCalculation(TestCaseData tc) {
        calculatorPage.open()
                .selectOriginCountry(tc.getOriginCountry())
                .enterOriginPostcode(PostcodeResolver.resolve(tc.getOriginPostcode(), tc.getOriginCountry()))
                .selectDestinationCountry(tc.getDestinationCountry())
                .enterDestinationPostcode(PostcodeResolver.resolve(tc.getDestinationPostcode(), tc.getDestinationCountry()))
                .clickCalculateButton();
        assertTrue(calculatorPage.isTransitTimeResultSectionVisible(), "Transit Time Result should be shown.");
    }

    @Severity(NORMAL)
    @Description("Sweden -> Sweden | Origin postcode is invalid, transit time result should not show")
    @ParameterizedTest(name = "{0}")
    @MethodSource("com.gmail.ek.rudenko.util.TestCaseDataLoader#originInvalidCasesDomestic")
    public void testInvalidOriginPostcode(TestCaseData tc) {
        calculatorPage.open()
                .selectOriginCountry(tc.getOriginCountry())
                .enterOriginPostcode(PostcodeResolver.resolve(tc.getOriginPostcode(), tc.getOriginCountry()))
                .selectDestinationCountry(tc.getDestinationCountry())
                .enterDestinationPostcode(PostcodeResolver.resolve(tc.getDestinationPostcode(), tc.getDestinationCountry()))
                .clickCalculateButton();
        assertFalse(calculatorPage.isTransitTimeResultSectionVisible(), "Transit Time Result section should NOT be shown.");
        assertTrue(calculatorPage.isOriginPostcodeErrorVisible(), "Expected error on origin postcode.");
    }

    @Severity(NORMAL)
    @Description("Sweden -> Sweden | Destination postcode is invalid, transit time result should not show")
    @ParameterizedTest(name = "{0}")
    @MethodSource("com.gmail.ek.rudenko.util.TestCaseDataLoader#destinationInvalidCasesDomestic")
    public void testInvalidDestinationPostcode(TestCaseData tc) {
        calculatorPage.open()
                .selectOriginCountry(tc.getOriginCountry())
                .enterOriginPostcode(PostcodeResolver.resolve(tc.getOriginPostcode(), tc.getOriginCountry()))
                .selectDestinationCountry(tc.getDestinationCountry())
                .enterDestinationPostcode(PostcodeResolver.resolve(tc.getDestinationPostcode(), tc.getDestinationCountry()))
                .clickCalculateButton();
        assertFalse(calculatorPage.isTransitTimeResultSectionVisible(), "Transit Time Result section should NOT be shown.");
        assertTrue(calculatorPage.isDestinationPostcodeErrorVisible(), "Expected error on destination postcode.");
    }
    @AfterEach
    public void teardown() {
        driver.quit();
    }
}