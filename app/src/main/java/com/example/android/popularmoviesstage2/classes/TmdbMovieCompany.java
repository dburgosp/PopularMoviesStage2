package com.example.android.popularmoviesstage2.classes;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Public class for managing information about companies related to movies.
 */
public class TmdbMovieCompany implements Parcelable {
    private String description;
    private String headquarters;
    private String homepage;
    private int id;
    private String logo_path;
    private String name;
    private TmdbMovieCompany parent_company;

    /**
     * Constructor for TmdbMovieCompany class objects.
     *
     * @param description    is the description of the company, given in the currently selected
     *                       language for retrieving information from the TMDB API.
     * @param headquarters   is the physical location of company headquarters.
     * @param homepage       is the url of the company web page.
     * @param id             is a unique identifier for the company.
     * @param logo_path      is a string for appending to {@link Tmdb#TMDB_POSTER_SIZE_W185_URL}
     *                       and get the logo of the company.
     * @param name           is the name of the company, given in the currently selected language
     *                       for retrieving information from the TMDB API.
     * @param parent_company is a {@link TmdbMovieCompany} object containing simple information about
     *                       the parent company, if exists, of the given company.
     */
    public TmdbMovieCompany(String description, String headquarters, String homepage, int id,
                            String logo_path, String name, TmdbMovieCompany parent_company) {
        this.description = description;
        this.headquarters = headquarters;
        this.homepage = homepage;
        this.id = id;
        this.logo_path = logo_path;
        this.name = name;
        this.parent_company = parent_company;
    }

    // Getters and setters.

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHeadquarters() {
        return headquarters;
    }

    public void setHeadquarters(String headquarters) {
        this.headquarters = headquarters;
    }

    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogo_path() {
        return logo_path;
    }

    public void setLogo_path(String logo_path) {
        this.logo_path = logo_path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TmdbMovieCompany getParent_company() {
        return parent_company;
    }

    public void setParent_company(TmdbMovieCompany parent_company) {
        this.parent_company = parent_company;
    }

    // Parcelable configuration.

    protected TmdbMovieCompany(Parcel in) {
        this.description = in.readString();
        this.headquarters = in.readString();
        this.homepage = in.readString();
        this.id = in.readInt();
        this.logo_path = in.readString();
        this.parent_company = in.readParcelable(TmdbMovieCompany.class.getClassLoader());
        this.name = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.description);
        dest.writeString(this.headquarters);
        dest.writeString(this.homepage);
        dest.writeInt(this.id);
        dest.writeString(this.logo_path);
        dest.writeParcelable(this.parent_company, flags);
        dest.writeString(this.name);
    }

    public static final Parcelable.Creator<TmdbMovieCompany> CREATOR = new Parcelable.Creator<TmdbMovieCompany>() {
        @Override
        public TmdbMovieCompany createFromParcel(Parcel source) {
            return new TmdbMovieCompany(source);
        }

        @Override
        public TmdbMovieCompany[] newArray(int size) {
            return new TmdbMovieCompany[size];
        }
    };
}
