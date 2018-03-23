package com.example.android.popularmoviesstage2.classes;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Public class for managing information about people.
 */
public class TmdbPerson implements Parcelable {
    private boolean adult;
    private ArrayList<String> also_known_as;
    private String biography;
    private String birthday;
    private String deathday;
    private int gender;
    private String homepage;
    private int id;
    private String imdb_id;
    private String name;
    private String place_of_birth;
    private Double popularity;
    private String profile_path;

    /**
     * Public constructor for new instances of objects of this class, with complete information
     * about a person.
     *
     * @param adult          is a boolean value that indicates whether the person is related to
     *                       adult content or not.
     * @param also_known_as  is a list of aliases for the person.
     * @param biography      is a String containing the bio of the person.
     * @param birthday       is a String containing the birth date of the person.
     * @param deathday       is a String containing the death date of the person, is he/she is not
     *                       yet alive.
     * @param gender         is the gender of this person (0: undefined, 1: female, 2: male).
     * @param homepage       is the url of the official web page of the person.
     * @param id             is the unique identifier of the {@link TmdbPerson}.
     * @param imdb_id        is the IMDB code for appending to http://www.imdb.com/title/ and
     *                       get the web page of the movie on IMDB.
     * @param name           is the complete name of the person.
     * @param place_of_birth is a String containing the birth place of the person.
     * @param popularity     is a double value indicating how popular is the person on TMDB
     *                       (based on views, ratings, favourite additions, etc.).
     * @param profile_path   is a string for appending to {@link Tmdb#TMDB_POSTER_SIZE_W185_URL} and
     *                       get the main photograph of the person.
     */
    public TmdbPerson(boolean adult, ArrayList<String> also_known_as, String biography,
                      String birthday, String deathday, int gender, String homepage, int id,
                      String imdb_id, String name, String place_of_birth, Double popularity,
                      String profile_path) {
        this.adult = adult;
        this.also_known_as = also_known_as;
        this.biography = biography;
        this.birthday = birthday;
        this.deathday = deathday;
        this.gender = gender;
        this.homepage = homepage;
        this.id = id;
        this.imdb_id = imdb_id;
        this.name = name;
        this.place_of_birth = place_of_birth;
        this.popularity = popularity;
        this.profile_path = profile_path;
    }

    /**
     * Public constructor for new instances of objects of this class, with very limited information
     * about a person.
     *
     * @param adult        is a boolean value that indicates whether the person is related to
     *                     adult content or not.
     * @param id           is the unique identifier of the {@link TmdbPerson}.
     * @param name         is the complete name of the person.
     * @param profile_path is a string for appending to {@link Tmdb#TMDB_POSTER_SIZE_W185_URL} and
     *                     get the main photograph of the person.
     */
    public TmdbPerson(boolean adult, int id, String name, String profile_path) {
        this.adult = adult;
        this.also_known_as = null;
        this.biography = "";
        this.birthday = "";
        this.deathday = "";
        this.gender = 0;
        this.homepage = "";
        this.id = id;
        this.imdb_id = "";
        this.name = name;
        this.place_of_birth = "";
        this.popularity = 0.0;
        this.profile_path = profile_path;
    }

    // Getters and setters.

    public boolean isAdult() {
        return adult;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public ArrayList<String> getAlso_known_as() {
        return also_known_as;
    }

    public void setAlso_known_as(ArrayList<String> also_known_as) {
        this.also_known_as = also_known_as;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getDeathday() {
        return deathday;
    }

    public void setDeathday(String deathday) {
        this.deathday = deathday;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
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

    public String getImdb_id() {
        return imdb_id;
    }

    public void setImdb_id(String imdb_id) {
        this.imdb_id = imdb_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlace_of_birth() {
        return place_of_birth;
    }

    public void setPlace_of_birth(String place_of_birth) {
        this.place_of_birth = place_of_birth;
    }

    public Double getPopularity() {
        return popularity;
    }

    public void setPopularity(Double popularity) {
        this.popularity = popularity;
    }

    public String getProfile_path() {
        return profile_path;
    }

    public void setProfile_path(String profile_path) {
        this.profile_path = profile_path;
    }

    // Parcelable configuration.

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.adult ? (byte) 1 : (byte) 0);
        dest.writeStringList(this.also_known_as);
        dest.writeString(this.biography);
        dest.writeString(this.birthday);
        dest.writeString(this.deathday);
        dest.writeInt(this.gender);
        dest.writeString(this.homepage);
        dest.writeInt(this.id);
        dest.writeString(this.imdb_id);
        dest.writeString(this.name);
        dest.writeString(this.place_of_birth);
        dest.writeValue(this.popularity);
        dest.writeString(this.profile_path);
    }

    protected TmdbPerson(Parcel in) {
        this.adult = in.readByte() != 0;
        this.also_known_as = in.createStringArrayList();
        this.biography = in.readString();
        this.birthday = in.readString();
        this.deathday = in.readString();
        this.gender = in.readInt();
        this.homepage = in.readString();
        this.id = in.readInt();
        this.imdb_id = in.readString();
        this.name = in.readString();
        this.place_of_birth = in.readString();
        this.popularity = (Double) in.readValue(Double.class.getClassLoader());
        this.profile_path = in.readString();
    }

    public static final Parcelable.Creator<TmdbPerson> CREATOR = new Parcelable.Creator<TmdbPerson>() {
        @Override
        public TmdbPerson createFromParcel(Parcel source) {
            return new TmdbPerson(source);
        }

        @Override
        public TmdbPerson[] newArray(int size) {
            return new TmdbPerson[size];
        }
    };
}
