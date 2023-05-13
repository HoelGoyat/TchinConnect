package org.esaip.tchinconnect.models.DAO;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import org.esaip.tchinconnect.models.User;

@Database(entities = {User.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();

}
