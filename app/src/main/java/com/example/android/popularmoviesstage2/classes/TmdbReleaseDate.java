package com.example.android.popularmoviesstage2.classes;

/**
 * Public class for managing the release dates of a movie in a given country.
 */
public class TmdbReleaseDate {
    private String certification;
    private String iso_639_1;
    private String note;
    private String release_date;
    private int type;

    /**
     * Constructor for objects of this class.
     *
     * @param certification is the age rating of the movie in the given country.
     * @param iso_639_1     is the ISO 639-1 code of the release language.
     * @param note          is an optional note for the release.
     * @param release_date  is the release date.
     * @param type          is the release type. Available values: 1 (Premiere), 2 (Theatrical
     *                      (limited)), 3 (Theatrical), 4 (Digital), 5 (Physical) and 6 (TV).
     */
    public TmdbReleaseDate(String certification, String iso_639_1, String note, String release_date, int type) {
        this.certification = certification;
        this.iso_639_1 = iso_639_1;
        this.note = note;
        this.release_date = release_date;
        this.type = type;
    }

    // Getters and setters.

    public String getCertification() {
        return certification;
    }

    public void setCertification(String certification) {
        this.certification = certification;
    }

    public String getIso_639_1() {
        return iso_639_1;
    }

    public void setIso_639_1(String iso_639_1) {
        this.iso_639_1 = iso_639_1;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
