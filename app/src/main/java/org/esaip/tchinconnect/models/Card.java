package org.esaip.tchinconnect.models;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.UUID;

@Entity
public class Card {

    @PrimaryKey
    UUID userID;
    private String name;
    private String surname;
    private String email;
    private String job;
    private String jobDescription;
    //temporary string
    private String image;

    public Card(UUID newUserID,String newName, String newSurname, String newEmail, String newJob, String newJobDescription, String newImage){
        this.userID = newUserID;
        this.name = newName;
        this.surname = newSurname;
        this.email = newEmail;
        this.job = newJob;
        this.jobDescription = newJobDescription;
        this.image = newImage;
    }

    public Card() {
        this.userID = UUID.randomUUID();
        this.name = null;
        this.surname = null;
        this.email = null;
        this.job = null;
        this.jobDescription = null;
        this.image = null;
    }

    // Getters and Setters
    public UUID getUserID() {
        return userID;
    }

    public void setUserID(UUID userID) {
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
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

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
