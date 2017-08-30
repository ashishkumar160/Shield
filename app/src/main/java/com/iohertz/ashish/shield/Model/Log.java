package com.iohertz.ashish.shield.Model;

/**
 * Created by ashish on 6/3/17.
 */

public class Log {
    private String log;
    private String logtime;
    private int thumbnail;

    public Log() {

    }

    public Log(String log,String logtime, int thumbnail) {
        this.log = log;
        this.logtime = logtime;
        this.thumbnail = thumbnail;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public String getLogtime() {
        return logtime;
    }

    public void setLogtime(String logtime) {
        this.logtime = logtime;
    }

    public int getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(int thumbnail) {
        this.thumbnail = thumbnail;
    }
}
