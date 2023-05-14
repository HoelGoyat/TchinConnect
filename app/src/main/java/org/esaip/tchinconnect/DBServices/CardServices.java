package org.esaip.tchinconnect.DBServices;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.room.Room;

import org.esaip.tchinconnect.models.Card;
import org.esaip.tchinconnect.models.DAO.AppDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class CardServices{

    private AppDatabase db ;

    public CardServices(Context context){
        this.db = Room.databaseBuilder(context,
                        AppDatabase.class, "tchinDB")
                .fallbackToDestructiveMigration()
                .build();
    }

    public List<Card> getAllCards(){
        try {
            return new GetCardsAsyncTask().execute().get();
        } catch (ExecutionException e) {
            //throw new RuntimeException(e);
            return new ArrayList<>();
        } catch (InterruptedException e) {
            //throw new RuntimeException(e);
            return new ArrayList<>();
        }
    }

    public Card getCardByUserId(UUID userId){
        try {
            return new getCardByUserIDAsyncTask(userId).execute().get();
        } catch (ExecutionException e) {
            //throw new RuntimeException(e);
            return new Card();
        } catch (InterruptedException e) {
            //throw new RuntimeException(e);
            return new Card();
        }
    }


    private class GetCardsAsyncTask extends AsyncTask<Void, Void,List<Card>>
    {
        @Override
        protected List<Card> doInBackground(Void... url) {
            return db.cardDao().getCards();
        }
    }

    private class getCardByUserIDAsyncTask extends AsyncTask<Void, Void,Card> {

        private UUID userId;

        public getCardByUserIDAsyncTask(UUID userId){this.userId = userId;}

        @Override
        protected Card doInBackground(Void... url) {
            return db.cardDao().getCardByUserId(this.userId);
        }
    }
}