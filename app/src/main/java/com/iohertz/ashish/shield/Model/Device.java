package com.iohertz.ashish.shield.Model;

import com.iohertz.ashish.shield.R;

/**
 * Created by ashish on 6/3/17.
 */

public class Device {
    private String name;
    private String type;
    private int[] images = new int[] {
            R.drawable.knight,
            R.drawable.vision
    };

    public Device() {

    }

    public Device(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getThumbnail() {
        if (type.equals("bell")) {
            return images[1];
        } else {
            return images[0];
        }
    }

    public void setType(String type) {
        this.type = type;
    }
}
