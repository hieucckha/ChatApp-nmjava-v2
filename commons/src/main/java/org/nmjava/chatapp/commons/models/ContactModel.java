package org.nmjava.chatapp.commons.models;

public class ContactModel {
    private String ImageSrc;
    private String Name;
    private String time;
    private String message;

    private boolean status;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getImageSrc() {
        return ImageSrc;
    }

    public void setImageSrc(String imageSrc) {
        ImageSrc = imageSrc;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
