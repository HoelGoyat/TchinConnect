package org.esaip.tchinconnect.models.DAO;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import org.esaip.tchinconnect.models.Card;
import org.esaip.tchinconnect.models.User;

@Database(entities = {User.class, Card.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();

}
