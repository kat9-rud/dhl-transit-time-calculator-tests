package com.gmail.ek.rudenko.util;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostcodeResolver {
    private static final Map<String, String> postcodeMap = new HashMap<>();

    public static class PostcodeMappingEntry {
        @CsvBindByName(column = "Country")
        String country;

        @CsvBindByName(column = "Label")
        String label;

        @CsvBindByName(column = "Postcode")
        String postcode;

        public String getKey() {
            return country.trim() + ":" + label.trim();
        }

        public String getPostcode() {
            return postcode;
        }
    }

    static {
        try (InputStreamReader reader = new InputStreamReader(
                PostcodeResolver.class.getResourceAsStream("/testdata/postcode-mappings.csv"))) {
            List<PostcodeMappingEntry> rows = new CsvToBeanBuilder<PostcodeMappingEntry>(reader)
                    .withType(PostcodeMappingEntry.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build()
                    .parse();

            for (PostcodeMappingEntry row : rows) {
                postcodeMap.put(row.getKey(), row.getPostcode());
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to load postcode mappings", e);
        }
    }

    public static String resolve(String label, String country) {
        return postcodeMap.getOrDefault(country + ":" + label, label);
    }

    public static String resolve(String label) {
        return postcodeMap.values().stream()
                .filter(postcode -> postcode.equalsIgnoreCase(label))
                .findFirst()
                .orElse(label);
    }
}