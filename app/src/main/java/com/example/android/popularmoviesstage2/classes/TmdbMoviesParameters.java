package com.example.android.popularmoviesstage2.classes;

/**
 * Class for managing the parameters to use in a search for a movies list from TMDB.
 */
public class TmdbMoviesParameters {
    private String mLanguage;
    private String mSortBy;
    private String mCertification;
    private Double mVoteAverage;
    private int mVoteCount;
    private String mRegion;
    private String mReleaseType;
    private String mInitDate;
    private String mEndDate;

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
        mLanguage = language;
        mSortBy = sortBy;
        mCertification = certification;
        mVoteAverage = voteAverage;
        mVoteCount = voteCount;
        mRegion = region;
        mReleaseType = releaseType;
        mInitDate = initDate;
        mEndDate = endDate;
    }

    // Getters and setters.
    public String getLanguage() {
        return mLanguage;
    }

    public void setLanguage(String language) {
        mLanguage = language;
    }

    public String getSortBy() {
        return mSortBy;
    }

    public void setSortBy(String sortBy) {
        mSortBy = sortBy;
    }

    public String getCertification() {
        return mCertification;
    }

    public void setCertification(String certification) {
        mCertification = certification;
    }

    public Double getVoteAverage() {
        return mVoteAverage;
    }

    public void setVoteAverage(Double voteAverage) {
        mVoteAverage = voteAverage;
    }

    public int getVoteCount() {
        return mVoteCount;
    }

    public void setVoteCount(int voteCount) {
        mVoteCount = voteCount;
    }

    public String getRegion() {
        return mRegion;
    }

    public void setRegion(String region) {
        mRegion = region;
    }

    public String getReleaseType() {
        return mReleaseType;
    }

    public void setReleaseType(String releaseType) {
        mReleaseType = releaseType;
    }

    public String getInitDate() {
        return mInitDate;
    }

    public void setInitDate(String initDate) {
        mInitDate = initDate;
    }

    public String getEndDate() {
        return mEndDate;
    }

    public void setEndDate(String endDate) {
        mEndDate = endDate;
    }
}
