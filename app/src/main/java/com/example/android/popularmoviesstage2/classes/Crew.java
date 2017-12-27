package com.example.android.popularmoviesstage2.classes;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Public class for managing the crew of a movie.
 */
public class Crew implements Parcelable {
    public static final Parcelable.Creator<Crew> CREATOR = new Parcelable.Creator<Crew>() {
        @Override
        public Crew createFromParcel(Parcel source) {
            return new Crew(source);
        }

        @Override
        public Crew[] newArray(int size) {
            return new Crew[size];
        }
    };
    private String credit_id;
    private String department;
    private int gender;
    private int id;
    private String job;
    private String name;
    private String profile_path;

    /**
     * Public constructor for new instances of objects of this class, with very limited
     * information for a simple crew list.
     *
     * @param id           is the unique identifier of the {@link Person}.
     * @param job          is the job of the member of the crew,
     * @param name         is the name of this person.
     * @param profile_path is the string to append to the url for getting the image file of this
     *                     person.
     */
    public Crew(int id, String job, String name, String profile_path) {
        this.credit_id = "";
        this.department = "";
        this.gender = 0;
        this.id = id;
        this.job = job;
        this.name = name;
        this.profile_path = profile_path;
    }

    // Getters and setters.

    /**
     * Public constructor for new instances of objects of this class, with complete information.
     *
     * @param credit_id
     * @param department   is the department which the member of the crew works in.
     * @param gender       is the gender of this person (0: undefined, 1: female, 2: male).
     * @param id           is the unique identifier of the {@link Person}.
     * @param job          is the job of the member of the crew,
     * @param name         is the name of this person.
     * @param profile_path is the string to append to the url for getting the image file of this
     *                     person.
     */
    public Crew(String credit_id, String department, int gender, int id, String job, String name,
                String profile_path) {
        this.credit_id = credit_id;
        this.department = department;
        this.gender = gender;
        this.id = id;
        this.job = job;
        this.name = name;
        this.profile_path = profile_path;
    }

    protected Crew(Parcel in) {
        this.credit_id = in.readString();
        this.department = in.readString();
        this.gender = in.readInt();
        this.id = in.readInt();
        this.job = in.readString();
        this.name = in.readString();
        this.profile_path = in.readString();
    }

    public String getCredit_id() {
        return credit_id;
    }

    public void setCredit_id(String credit_id) {
        this.credit_id = credit_id;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
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

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfile_path() {
        return profile_path;
    }

    public void setProfile_path(String profile_path) {
        this.profile_path = profile_path;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.credit_id);
        dest.writeString(this.department);
        dest.writeInt(this.gender);
        dest.writeInt(this.id);
        dest.writeString(this.job);
        dest.writeString(this.name);
        dest.writeString(this.profile_path);
    }
}
