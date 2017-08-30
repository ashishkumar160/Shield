package com.iohertz.ashish.shield.Model;

/**
 * Created by ashish on 6/3/17.
 */

public class System {
    private String name;
    private String id;
    private int thumbnail;

    public System() {

    }

    public System(String name, String id, int thumbnail) {
        this.name = name;
        this.id = id;
        this.thumbnail = thumbnail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(int thumbnail) {
        this.thumbnail = thumbnail;
    }
}
