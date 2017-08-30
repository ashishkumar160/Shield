package com.iohertz.ashish.shield.Model;

/**
 * Created by ashish on 6/3/17.
 */

public class User {
    private String name;
    private int thumbnail;

    public User() {

    }

    public User(String name, int thumbnail) {
        this.name = name;
        this.thumbnail = thumbnail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(int thumbnail) {
        this.thumbnail = thumbnail;
    }
}
