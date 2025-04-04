package com.gmail.ek.rudenko;

import com.gmail.ek.rudenko.model.TestCaseData;
import com.gmail.ek.rudenko.util.PostcodeResolver;
import io.qameta.allure.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static io.qameta.allure.Allure.*;
import static io.qameta.allure.SeverityLevel.CRITICAL;
import static io.qameta.allure.SeverityLevel.NORMAL;
import static org.junit.jupiter.api.Assertions.*;

@Epic("European Road Freight Transit Time Calculator")
@Feature("Domestic Routes (Sweden -> Sweden)")
public class DomesticRouteTests extends BaseTest {
    @Severity(CRITICAL)
    @Description("Sweden -> Sweden | Successful cases should show transit time result")
    @ParameterizedTest(name = "{0}")
    @MethodSource("com.gmail.ek.rudenko.util.TestCaseDataLoader#successfulCasesDomestic")
    public void testSuccessfulCalculation(TestCaseData tc) {
        step("Fill out and submit Transit Time Calculator form", () -> {
            calculatorPage.open()
                    .selectOriginCountry(tc.getOriginCountry())
                    .enterOriginPostcode(PostcodeResolver.resolve(tc.getOriginPostcode(), tc.getOriginCountry()))
                    .selectDestinationCountry(tc.getDestinationCountry())
                    .enterDestinationPostcode(PostcodeResolver.resolve(tc.getDestinationPostcode(), tc.getDestinationCountry()))
                    .clickCalculateButton();
        });

        //        pause(1000); //useful for debug

        step("Verify Transit Time Result section and product options", () -> {
            assertTrue(calculatorPage.isTransitTimeResultSectionVisible(), "Transit Time Result should be shown.");
            assertEquals(4, calculatorPage.getProductOptionCount(), "Expected 4 product options for domestic route");
        });

        step("Check visibility of date pickers", () -> {
            assertTrue(calculatorPage.isDeliveryDatePickerVisible(), "Expected delivery date picker for domestic route.");
            assertTrue(calculatorPage.isPickupDatePickerVisible(), "Expected pickup date picker for domestic route.");
        });

        step("Go back and check that Transit Time Result section disappears", () -> {
            calculatorPage.clickEditPreviousStepButton();
            assertFalse(calculatorPage.isTransitTimeResultSectionVisible(), "Transit Time Result section should NOT be shown.");
        });
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
}