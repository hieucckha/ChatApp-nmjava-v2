package org.nmjava.chatapp.client.models;

import java.util.Timer;
import java.util.Vector;

public class modelLoginList {
    String userName;
    String name;
    String times;

    public modelLoginList(String userName, String name, String times) {
        this.userName = userName;
        this.name = name;
        this.times = times;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTimes() {
        return times;
    }

    public void setTimes(String times) {
        this.times = times;
    }
}
