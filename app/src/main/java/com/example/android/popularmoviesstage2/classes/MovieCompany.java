package com.example.android.popularmoviesstage2.classes;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.android.popularmoviesstage2.utils.NetworkUtils;

/**
 * Public class for managing information about companies related to movies.
 */
public class MovieCompany implements Parcelable {
    public static final Parcelable.Creator<MovieCompany> CREATOR = new Parcelable.Creator<MovieCompany>() {
        @Override
        public MovieCompany createFromParcel(Parcel source) {
            return new MovieCompany(source);
        }

        @Override
        public MovieCompany[] newArray(int size) {
            return new MovieCompany[size];
        }
    };
    private String description;
    private String headquarters;
    private String homepage;
    private int id;
    private String logo_path;
    private String name;
    private MovieCompany parent_company;

    // Getters and setters.

    /**
     * Constructor for MovieCompany class objects.
     *
     * @param description    is the description of the company, given in the currently selected
     *                       language for retrieving information from the TMDB API.
     * @param headquarters   is the physical location of company headquarters.
     * @param homepage       is the url of the company web page.
     * @param id             is a unique identifier for the company.
     * @param logo_path      is a string for appending to {@link NetworkUtils#THUMBNAIL_IMAGE_URL}
     *                       and get the logo of the company.
     * @param name           is the name of the company, given in the currently selected language
     *                       for retrieving information from the TMDB API.
     * @param parent_company is a {@link MovieCompany} object containing simple information about
     *                       the parent company, if exists, of the given company.
     */
    public MovieCompany(String description, String headquarters, String homepage, int id,
                        String logo_path, String name, MovieCompany parent_company) {
        this.description = description;
        this.headquarters = headquarters;
        this.homepage = homepage;
        this.id = id;
        this.logo_path = logo_path;
        this.name = name;
        this.parent_company = parent_company;
    }

    protected MovieCompany(Parcel in) {
        this.description = in.readString();
        this.headquarters = in.readString();
        this.homepage = in.readString();
        this.id = in.readInt();
        this.logo_path = in.readString();
        this.parent_company = in.readParcelable(MovieCompany.class.getClassLoader());
        this.name = in.readString();
    }

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

    public MovieCompany getParent_company() {
        return parent_company;
    }

    public void setParent_company(MovieCompany parent_company) {
        this.parent_company = parent_company;
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
}
