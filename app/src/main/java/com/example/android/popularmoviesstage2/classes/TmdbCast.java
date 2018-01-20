package com.example.android.popularmoviesstage2.classes;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Public class for managing the cast of a movie.
 */
public class TmdbCast implements Parcelable {
    private int cast_id;
    private String character;
    private String credit_id;
    private int gender;
    private int id;
    private String name;
    private int order;
    private String profile_path;

    /**
     * Public constructor for new instances of objects of this class, with limited information for a
     * simple cast list.
     *
     * @param character    is the character name of this person in the current movie/tv show.
     * @param id           is the unique identifier of the {@link TmdbPerson}.
     * @param name         is the name of this person.
     * @param profile_path is the string to append to the url for getting the image file of this
     *                     person.
     */
    public TmdbCast(String character, int id, String name, String profile_path) {
        this.cast_id = 0;
        this.character = character;
        this.credit_id = "";
        this.gender = 0;
        this.id = id;
        this.name = name;
        this.order = 0;
        this.profile_path = profile_path;
    }

    /**
     * Public constructor for new instances of objects of this class, with complete information.
     *
     * @param cast_id
     * @param character    is the character name of this person in the current movie/tv show.
     * @param credit_id
     * @param gender       is the gender of this person (0: undefined, 1: female, 2: male).
     * @param id           is the unique identifier of the {@link TmdbPerson}.
     * @param name         is the name of this person.
     * @param order        is the designed order of this person into the cast list of the
     *                     current movie.
     * @param profile_path is the string to append to the url for getting the image file of this
     *                     person.
     */
    public TmdbCast(int cast_id, String character, String credit_id, int gender, int id, String name,
                    int order, String profile_path) {
        this.cast_id = cast_id;
        this.character = character;
        this.credit_id = credit_id;
        this.gender = gender;
        this.id = id;
        this.name = name;
        this.order = order;
        this.profile_path = profile_path;
    }

    // Getters and setters.

    public int getCast_id() {
        return cast_id;
    }

    public void setCast_id(int cast_id) {
        this.cast_id = cast_id;
    }

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    public String getCredit_id() {
        return credit_id;
    }

    public void setCredit_id(String credit_id) {
        this.credit_id = credit_id;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getProfile_path() {
        return profile_path;
    }

    public void setProfile_path(String profile_path) {
        this.profile_path = profile_path;
    }

    // Parcelable configuration.

    protected TmdbCast(Parcel in) {
        this.cast_id = in.readInt();
        this.character = in.readString();
        this.credit_id = in.readString();
        this.gender = in.readInt();
        this.id = in.readInt();
        this.name = in.readString();
        this.order = in.readInt();
        this.profile_path = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.cast_id);
        dest.writeString(this.character);
        dest.writeString(this.credit_id);
        dest.writeInt(this.gender);
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeInt(this.order);
        dest.writeString(this.profile_path);
    }

    public static final Parcelable.Creator<TmdbCast> CREATOR = new Parcelable.Creator<TmdbCast>() {
        @Override
        public TmdbCast createFromParcel(Parcel source) {
            return new TmdbCast(source);
        }

        @Override
        public TmdbCast[] newArray(int size) {
            return new TmdbCast[size];
        }
    };
}
