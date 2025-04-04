package com.gmail.ek.rudenko.model;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;

@Data
public class TestCaseData {
    @CsvBindByName(column = "TC")
    private String tc;

    @CsvBindByName(column = "Origin Country")
    private String originCountry;

    @CsvBindByName(column = "Origin Postcode")
    private String originPostcode;

    @CsvBindByName(column = "Destination Country")
    private String destinationCountry;

    @CsvBindByName(column = "Destination Postcode")
    private String destinationPostcode;

    @CsvBindByName(column = "Expected Result")
    private String expectedResult;

    @CsvBindByName(column = "Notes")
    private String notes;

    @CsvBindByName(column = "Invalid Field")
    private String invalidField;
    @Override
    public String toString() {
        return tc + ": " + originCountry + " (" + originPostcode + ") -> " + destinationCountry + " (" + destinationPostcode + ")";
    }
}
