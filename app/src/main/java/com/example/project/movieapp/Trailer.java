package com.example.project.movieapp;

/**
 * Created by hoang on 02/10/2015.
 */
public class Trailer {
    private String name;
    private String source;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Trailer(String name, String source) {

        this.name = name;
        this.source = source;
    }
}
