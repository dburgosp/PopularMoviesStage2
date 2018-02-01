package com.example.android.popularmoviesstage2.utils;

import android.util.Log;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

public final class LocaleUtils {
    private static final String TAG = LocaleUtils.class.getSimpleName();

    /**
     * Create a private constructor because no one should ever create a {@link LocaleUtils}
     * object. This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name DateTimeUtils (and an object instance of DateTimeUtils is not
     * needed).
     */
    private LocaleUtils() {
    }

    /**
     * Helper method to get the locale's country name in the current device language.
     *
     * @return a string containing the locale's country name in the current device language.
     */
    public static String getCurrentCountryName() {
        String currentCountryName = Locale.getDefault().getDisplayCountry();
        Log.e(TAG, "(getCurrentCountryName) Current country name: " + currentCountryName);
        return currentCountryName;
    }

    /**
     * Helper method to get the name of the USA in the current device language.
     *
     * @return a string containing the name of the USA in the current device language.
     */
    public static String getUSCountryName() {
        String countryName = Locale.US.getDisplayCountry();
        Log.e(TAG, "(getUSCountryName) Country name for the USA: " + countryName);
        return countryName;
    }

    /**
     * Helper method to get a country name in the current device language.
     *
     * @param isoCountry is the input string with the ISO 3166 alpha-2 country code or the UN M.49
     *                   numeric-3 area code of the country / area.
     * @return a string with the country name in the current device language.
     */
    public static String getCountryName(String isoCountry) {
        Log.e(TAG, "(getCountryName) Country code: " + isoCountry);
        Locale locale = new Locale(Locale.getDefault().getLanguage(), isoCountry);
        String countryName = locale.getDisplayCountry();
        Log.e(TAG, "(getCountryName) Country name: " + countryName);
        return countryName;
    }

    /**
     * Helper method to get a language name in the current device language.
     *
     * @param isoLanguage is the input string with the ISO 639 alpha-2 or alpha-3 language code, or
     *                    the language subtag up to 8 characters in length of the language.
     * @return a string with the language name in the current device language.
     */
    public static String getLanguageName(String isoLanguage) {
        Log.e(TAG, "(getLanguageName) isoLanguage IN: " + isoLanguage);

        // Correct obsolete ISO 639 codes.
        switch (isoLanguage) {
            case "cn":
                isoLanguage = "zh";
                break;
        }
        Locale locale = new Locale(isoLanguage);
        String language = locale.getDisplayLanguage();
        Log.e(TAG, "(getLanguageName) language OUT: " + language);
        return language;
    }

    /**
     * Helper method to format a quantity with a given currency in a locale-specific manner.
     *
     * @param number   is the input quantity.
     * @param currency is the ISO 4217 numeric code for the currency.
     * @return a string with the formatted quantity in the given currency.
     */
    public static String getCurrencyFormat(double number, String currency) {
        Log.e(TAG, "(getCurrencyFormat) Amount: " + number + ". Currency: " + currency);
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        formatter.setCurrency(Currency.getInstance(currency));
        String currencyFormat = formatter.format(number);

        // Remove decimals. Look for decimal separator and make substrings.
        DecimalFormat fmt = (DecimalFormat) DecimalFormat.getCurrencyInstance(Locale.getDefault());
        String symbol = String.valueOf(fmt.getDecimalFormatSymbols().getDecimalSeparator());
        int index = currencyFormat.indexOf(symbol);
        currencyFormat = currencyFormat.substring(0, index) +
                currencyFormat.substring(index + 3, currencyFormat.length());

        Log.e(TAG, "(getCurrencyFormat) Formatted amount: " + currencyFormat);
        return currencyFormat;
    }
}
