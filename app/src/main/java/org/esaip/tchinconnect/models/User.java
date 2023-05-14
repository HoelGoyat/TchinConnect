package org.esaip.tchinconnect.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;


import java.util.UUID;
import java.util.ArrayList;

@Entity(tableName = "users", foreignKeys = @ForeignKey(entity = Card.class,
        parentColumns = "user_id",
        childColumns = "personal_card_id",
        onDelete = 1))//NO_ACTION
public class User {

    @PrimaryKey
    @NonNull
    private UUID ID;
    //private Glass glass;
    @ColumnInfo(name="personal_card_id")
    private UUID personalCardId;
    //private ArrayList<Card> annuary;

    public User(UUID ID, /*Glass glass,*/ UUID personalCardID/*, ArrayList<Card> annuary*/) {
        this.ID = ID;
        //this.glass = glass;
        this.personalCardId = personalCardID;
        //this.annuary = annuary;
    }

    public User() {
        this.ID = UUID.randomUUID();
        //this.glass = null;
        this.personalCardId = null;
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

    public UUID getPersonalCardId() {
        return personalCardId;
    }

    public void setPersonalCardId(UUID personalCardId) {
        this.personalCardId = personalCardId;
    }

    /*public ArrayList<Card> getAnnuary() {
        return annuary;
    }

    public void setAnnuary(ArrayList<Card> annuary) {
        this.annuary = annuary;
    }*/
}
