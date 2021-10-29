package com.dodonov.oogosu.utils;

public class SearchStringUtils {

    private SearchStringUtils() {
        throw new IllegalStateException("Utility class");
    }

    private static final char[] UNNECESSARY_SYMBOLS = {'.', ':', '(', ')', '!', '\'', '<', '>', '&', '|', '/'};

    public static String generateOrFields(String search) {
        String[] searchArr = replaceUnnecessarySymbols(search).split("\\s+");
        StringBuilder searchSB = new StringBuilder();
        for (int i = 0; i < searchArr.length; i++) {
            if (i == 0) {
                searchSB.append(searchArr[i].trim()).append(":*");
            } else {
                searchSB.append(" | ").append(searchArr[i].trim()).append(":*");
            }
        }
        return searchSB.toString();
    }

    public static String generateAndFields(String search) {
        String[] searchArr = replaceUnnecessarySymbols(search).split("\\s+");
        StringBuilder searchSB = new StringBuilder();
        for (int i = 0; i < searchArr.length; i++) {
            if (i == 0) {
                searchSB.append(searchArr[i].trim()).append(":*");
            } else {
                searchSB.append(" & ").append(searchArr[i].trim()).append(":*");
            }
        }
        return searchSB.toString();
    }

    private static String replaceUnnecessarySymbols(String value) {
        for (char unnecessarySymbol : UNNECESSARY_SYMBOLS) {
            value = value.replace(unnecessarySymbol, ' ');
        }
        return value.trim();
    }

    public static String normalizeAddressString(String fullAddress) {
        if (fullAddress == null) return null;
        if (fullAddress.isEmpty()) return "";
        return fullAddress
                .trim()
                .replace(".", "  ")
                .replace(",", "  ")
                .replace("-", "  ")
                .replace(";", "  ")
                .replace(";", "  ")
                .replace("_", "  ")
                .replaceAll("\\s[А-Яа-я]\\s", " ")
                .replaceAll("\\s+", " ");
    }

    /**
     * Проверяем содержит результат поиска искомую строку. И не содержит ли подстветку поле
     *
     * @param searchText  искомый текст
     * @param resultField результат поиска
     */
    public static boolean isThisMatchedResult(String searchText, String resultField) {
        String replacedSearchText = searchText.replace("*", "(.*)").toLowerCase().trim();
        return !resultField.contains("<b>") && !resultField.contains("</b>") && resultField.toLowerCase().trim().matches(replacedSearchText);
    }
}
