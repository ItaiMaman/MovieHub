package com.example.moviehub.models;

import java.util.Date;
import java.util.List;

public class Event {

    private Date date;
    private Movies.Movie movie;
    private String id;
    private List<String> friends;

    public Event() {
    }

    public Event(Date date, Movies.Movie movie, String id, List<String> friends) {
        this.date = date;
        this.movie = movie;
        this.id = id;
        this.friends = friends;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Movies.Movie getMovie() {
        return movie;
    }

    public void setMovie(Movies.Movie movie) {
        this.movie = movie;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getFriends() {
        return friends;
    }

    public void setFriends(List<String> friends) {
        this.friends = friends;
    }
}
