package org.esaip.tchinconnect.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;


import java.util.UUID;
import java.util.ArrayList;

@Entity(tableName = "users")
public class User {

    @PrimaryKey
    @NonNull
    private UUID ID;
    //private Glass glass;
    @Embedded
    private Card personalCard;
    //private ArrayList<Card> annuary;

    public User(UUID ID, /*Glass glass,*/ Card personalCard/*, ArrayList<Card> annuary*/) {
        this.ID = ID;
        //this.glass = glass;
        this.personalCard = personalCard;
        //this.annuary = annuary;
    }

    public User() {
        this.ID = UUID.randomUUID();
        //this.glass = null;
        this.personalCard = null;
        //this.annuary = null;
    }

    // Getters and Setters


    public UUID getID() {
        return ID;
    }

    public void setID(UUID ID) {
        this.ID = ID;
    }

   /* public Glass getGlass() {
        return glass;
    }

    public void setGlass(Glass glass) {
        this.glass = glass;
    }*/

    public Card getPersonalCard() {
        return personalCard;
    }

    public void setPersonalCard(Card personalCard) {
        this.personalCard = personalCard;
    }

    /*public ArrayList<Card> getAnnuary() {
        return annuary;
    }

    public void setAnnuary(ArrayList<Card> annuary) {
        this.annuary = annuary;
    }*/
}
