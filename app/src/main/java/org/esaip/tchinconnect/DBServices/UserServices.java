package org.esaip.tchinconnect.DBServices;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.room.Room;

import org.esaip.tchinconnect.models.DAO.AppDatabase;
import org.esaip.tchinconnect.models.DAO.UserDao;
import org.esaip.tchinconnect.models.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class UserServices{

    private AppDatabase db ;

    public UserServices(Context context){
        this.db = Room.databaseBuilder(context,
            AppDatabase.class, "tchinDB")
                .fallbackToDestructiveMigration()
                .build();
    }

    public List<User> getAllUsers(){
        try {
            return new GetUsersAsyncTask().execute().get();
        } catch (ExecutionException e) {
            //throw new RuntimeException(e);
            return new ArrayList<>();
        } catch (InterruptedException e) {
            //throw new RuntimeException(e);
            return new ArrayList<>();
        }
    }


    private class GetUsersAsyncTask extends AsyncTask<Void, Void,List<User>>
    {
        @Override
        protected List<User> doInBackground(Void... url) {
            return db.userDao().getUsers();
        }
    }
}