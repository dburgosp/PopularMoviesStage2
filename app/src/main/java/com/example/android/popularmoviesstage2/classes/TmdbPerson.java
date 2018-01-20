package com.example.android.popularmoviesstage2.classes;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class TmdbPerson implements Parcelable {
    private boolean adult;
    private ArrayList<Integer> also_known_as;
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

    public TmdbPerson(boolean adult, ArrayList<Integer> also_known_as, String biography, String birthday,
                      String deathday, int gender, String homepage, int id, String imdb_id, String name,
                      String place_of_birth, Double popularity, String profile_path) {
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

    // Getters and setters.

    public boolean isAdult() {
        return adult;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public ArrayList<Integer> getAlso_known_as() {
        return also_known_as;
    }

    public void setAlso_known_as(ArrayList<Integer> also_known_as) {
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

    protected TmdbPerson(Parcel in) {
        this.adult = in.readByte() != 0;
        this.also_known_as = new ArrayList<>();
        in.readList(this.also_known_as, Integer.class.getClassLoader());
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.adult ? (byte) 1 : (byte) 0);
        dest.writeList(this.also_known_as);
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
