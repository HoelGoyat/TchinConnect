package org.esaip.tchinconnect.models.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import org.esaip.tchinconnect.models.User;

import java.util.List;

@Dao
public interface UserDao {

    // allowing the insert of the same word multiple times by passing a
    // conflict resolution strategy
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(User user);

    @Query("DELETE FROM users")
    void deleteAll();

    @Query("SELECT * FROM users")
    List<User> getUsers();



}
