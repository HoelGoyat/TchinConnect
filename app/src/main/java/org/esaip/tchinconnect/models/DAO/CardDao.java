package org.esaip.tchinconnect.models.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;

import org.esaip.tchinconnect.models.Card;
import org.esaip.tchinconnect.models.User;

import java.util.List;
import java.util.UUID;

@Dao
public interface CardDao {

    // allowing the insert of the same word multiple times by passing a
    // conflict resolution strategy
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Card card);

    //@Query("DELETE FROM word_table")
    void deleteAll();

    //@Query("SELECT * FROM word_table ORDER BY word ASC")
    List<Card> getCard();

    //@Query("SELECT * FROM ")
    Card getCardByUserId(UUID userId);



}
