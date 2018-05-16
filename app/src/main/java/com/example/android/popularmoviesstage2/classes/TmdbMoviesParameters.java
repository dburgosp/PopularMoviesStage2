package com.example.android.popularmoviesstage2.classes;

/**
 * Class for managing the parameters to use in a search for a movies list from TMDB.
 */
public class TmdbMoviesParameters {
    private String language;
    private String sortBy;
    private String certification;
    private Double voteAverage;
    private int voteCount;
    private String region;
    private String releaseType;
    private String initDate;
    private String endDate;

    /**
     * Constructor for objects of this class.
     *
     * @param language      is the language of the results.
     * @param sortBy        is the sort order for the list of results.
     * @param certification is the minimum age rating of the movies_menu in the list for the current
     *                      country (region parameter).
     * @param voteAverage   is the minimum users rating of the movies_menu in the list.
     * @param voteCount     is the minimum number of users votes of the movies_menu in the list.
     * @param region        is the region for getting results.
     * @param releaseType   is the value or list of values to filter release types by.
     * @param initDate      is the initial date of the time range to filter movies_menu, if needed.
     * @param endDate       is the end date of the time range to filter movies_menu, if needed.
     */
    public TmdbMoviesParameters(String language, String sortBy, String certification,
                                Double voteAverage, int voteCount, String region,
                                String releaseType, String initDate, String endDate) {
        this.language = language;
        this.sortBy = sortBy;
        this.certification = certification;
        this.voteAverage = voteAverage;
        this.voteCount = voteCount;
        this.region = region;
        this.releaseType = releaseType;
        this.initDate = initDate;
        this.endDate = endDate;
    }

    // Getters and setters.
    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public String getCertification() {
        return certification;
    }

    public void setCertification(String certification) {
        this.certification = certification;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(Double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getReleaseType() {
        return releaseType;
    }

    public void setReleaseType(String releaseType) {
        this.releaseType = releaseType;
    }

    public String getInitDate() {
        return initDate;
    }

    public void setInitDate(String initDate) {
        this.initDate = initDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
