package com.example.android.popularmoviesstage2.classes;

import java.util.ArrayList;

/**
 * Class for managing the parameters to use in a search for a movies list from TMDB.
 */
public class TmdbTVSeriesParameters {
    private String mLanguage;
    private String mSortBy;
    private Double mVoteAverage;
    private int mVoteCount;
    private String mInitAirDate;
    private String mEndAirDate;
    private ArrayList<String> mNetworks;

    /**
     * Constructor for objects of this class.
     *
     * @param language    is the mLanguage of the results.
     * @param sortBy      is the sort order for the list of results.
     * @param voteAverage is the minimum users rating of the TV series in the list.
     * @param voteCount   is the minimum number of users votes of the TV series in the list.
     * @param initAirDate is the initial date of the time range to filter TV series, if needed.
     * @param endAirDate  is the end date of the time range to filter TV series, if needed.
     * @param networks    is the list of mNetworks to filter TV series, if needed.
     */
    public TmdbTVSeriesParameters(String language, String sortBy, Double voteAverage, int voteCount,
                                  String initAirDate, String endAirDate, ArrayList<String> networks) {
        mLanguage = language;
        mSortBy = sortBy;
        mVoteAverage = voteAverage;
        mVoteCount = voteCount;
        mInitAirDate = initAirDate;
        mEndAirDate = endAirDate;
        mNetworks = networks;
    }

    // Getters and setters.
    public String getLanguage() {
        return mLanguage;
    }

    public void setLanguage(String mLanguage) {
        mLanguage = mLanguage;
    }

    public String getSortBy() {
        return mSortBy;
    }

    public void setSortBy(String sortBy) {
        mSortBy = sortBy;
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

    public void setVoteCount(int mVoteCount) {
        mVoteCount = mVoteCount;
    }

    public String getInitAirDate() {
        return mInitAirDate;
    }

    public void setInitAirDate(String initAirDate) {
        mInitAirDate = initAirDate;
    }

    public String getEndAirDate() {
        return mEndAirDate;
    }

    public void setEndAirDate(String mEndAirDate) {
        mEndAirDate = mEndAirDate;
    }

    public ArrayList<String> getNetworks() {
        return mNetworks;
    }

    public void setNetworks(ArrayList<String> networks) {
        mNetworks = networks;
    }
}
