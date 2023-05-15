package org.esaip.tchinconnect;

public class Profil {
    private String firstName;
    private String secondName;
    private String email;
    private String job;
    private String description;
    private String picture;

    public Profil(String firstName, String secondName, String email, String job, String description, String picture) {
        this.firstName = firstName;
        this.secondName = secondName;
        this.email = email;
        this.job = job;
        this.description = description;
        this.picture = picture;
    }


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
