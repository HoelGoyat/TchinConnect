package org.esaip.tchinconnect.models.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

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

    @Query("DELETE FROM cards")
    void deleteAll();

    @Query("SELECT * FROM cards")
    List<Card> getCards();

    @Query("SELECT * FROM cards WHERE user_id IS NOT NULL LIMIT 1")
    Card getCardUser();

    @Query("SELECT * FROM cards WHERE user_id IS :userId")
    Card getCardByUserId(UUID userId);

    @Query("SELECT * FROM cards WHERE card_id IS :cardId")
    Card getCardById(UUID cardId);

    @Query("SELECT * FROM cards WHERE user_id IS NULL")
    List<Card> getContacts();



}
