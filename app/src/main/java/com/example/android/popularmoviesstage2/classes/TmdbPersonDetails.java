package com.example.android.popularmoviesstage2.classes;

/**
 * Public class for managing the complete information about a person.
 */
public class TmdbPersonDetails {
    private TmdbPerson person;

    // Variables from append_to_response argument to the query.
    private TmdbExternalId externalIds;

    /**
     * Constructor for objects of this class, containing the whole information of a movie.
     *
     * @param person      is the {@link TmdbPerson} object with the basic info about the person.
     * @param externalIds is the {@link TmdbExternalId} object with the Facebook, Twitter, IMDB and
     *                    Instagram profiles of the person.
     */
    public TmdbPersonDetails(TmdbPerson person, TmdbExternalId externalIds) {
        this.person = person;
        this.externalIds = externalIds;
    }

    // Getters and setters.

    public TmdbPerson getPerson() {
        return person;
    }

    public void setPerson(TmdbPerson person) {
        this.person = person;
    }

    public TmdbExternalId getExternalIds() {
        return externalIds;
    }

    public void setExternalIds(TmdbExternalId externalIds) {
        this.externalIds = externalIds;
    }
}
