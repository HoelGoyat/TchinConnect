package org.esaip.tchinconnect.models;

import java.util.UUID;
import java.util.ArrayList;

public class Glass {

    private UUID ID;
    private User user;
    private ArrayList<Card> storedCards;
    private enum status {
        Idle,
        Synchronized,
        Away,
        Unsychronized,
        Occupied
    };

    private Glass(){
        this.ID = null;
        this.user = null;
        this.storedCards = null;
    }


}
