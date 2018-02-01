package com.example.android.popularmoviesstage2.classes;

/**
 * Public class for managing the tags related to a movie.
 */
public class TmdbKeyword {
    private int id;
    private String name;

    /**
     * Constructor for objects of this class.
     *
     * @param id   is the unique identifier of the keyword.
     * @param name is the name of the keyword.
     */
    public TmdbKeyword(int id, String name) {
        this.id = id;
        this.name = name;
    }

    // Getters and setters.

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
}
