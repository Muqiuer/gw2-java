package com.itheima.Dao;

import java.sql.Date;

public class Information {
    private int id;
    private String username;
    private Date time;
    private String information;

    public Information(Date time, String information) {
        this.time = time;
        this.information = information;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    @Override
    public String toString() {
        return "Information{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", time=" + time +
                ", information='" + information + '\'' +
                '}';
    }
}
