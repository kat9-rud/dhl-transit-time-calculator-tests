package com.gmail.ek.rudenko.util;

import com.gmail.ek.rudenko.model.TestCaseData;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Stream;

public class TestCaseDataLoader {
    private static final String DOMESTIC_FILE = "/testdata/domestic.csv";
    private static final String INTERNATIONAL_FILE = "/testdata/international.csv";

    private static final List<TestCaseData> DOMESTIC_CASES = load(DOMESTIC_FILE);
    private static final List<TestCaseData> INTERNATIONAL_CASES = load(INTERNATIONAL_FILE);

    private static List<TestCaseData> load(String resourcePath) {
        try (InputStreamReader reader = new InputStreamReader(
                TestCaseDataLoader.class.getResourceAsStream(resourcePath))) {
            return new CsvToBeanBuilder<TestCaseData>(reader)
                    .withType(TestCaseData.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build()
                    .parse();
        } catch (Exception e) {
            throw new RuntimeException("Failed to load: " + resourcePath, e);
        }
    }

    private static Stream<TestCaseData> filter(List<TestCaseData> all, String expected, String field) {
        return all.stream()
                .filter(tc -> tc.getExpectedResult().equalsIgnoreCase(expected) &&
                        (field == null || field.equalsIgnoreCase(tc.getInvalidField())));
    }

    // Domestic
    public static Stream<TestCaseData> successfulCasesDomestic() {
        return filter(DOMESTIC_CASES, "ok", null);
    }

    public static Stream<TestCaseData> originInvalidCasesDomestic() {
        return filter(DOMESTIC_CASES, "error", "origin");
    }

    public static Stream<TestCaseData> destinationInvalidCasesDomestic() {
        return filter(DOMESTIC_CASES, "error", "destination");
    }

    // International
    public static Stream<TestCaseData> successfulCasesInternational() {
        return filter(INTERNATIONAL_CASES, "ok", null);
    }

    public static Stream<TestCaseData> originInvalidCasesInternational() {
        return filter(INTERNATIONAL_CASES, "error", "origin");
    }

    public static Stream<TestCaseData> destinationInvalidCasesInternational() {
        return filter(INTERNATIONAL_CASES, "error", "destination");
    }
}
