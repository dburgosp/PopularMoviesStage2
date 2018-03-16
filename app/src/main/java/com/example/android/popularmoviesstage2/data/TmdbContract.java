package com.example.android.popularmoviesstage2.data;

import android.provider.BaseColumns;

public class TmdbContract {
    private TmdbContract() {
    }

    /**
     * Table for managing languages.
     */
    public static class TmdbLanguagesEntry implements BaseColumns {
        public static final String TABLE_NAME = "languages";
        public static final String COLUMN_ISO_CODE = "isoCode";
    }

    /**
     * Table for managing countries.
     */
    public static class TmdbCountriesEntry implements BaseColumns {
        public static final String TABLE_NAME = "countries";
        public static final String COLUMN_ISO_CODE = "isoCode";
    }
}
