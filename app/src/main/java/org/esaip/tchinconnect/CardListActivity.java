package org.esaip.tchinconnect;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.esaip.tchinconnect.DBServices.CardServices;
import org.esaip.tchinconnect.R;
import org.esaip.tchinconnect.adapters.CardAdapter;
import org.esaip.tchinconnect.models.Card;

import java.util.ArrayList;
import java.util.List;

public class CardListActivity extends AppCompatActivity implements CardAdapter.ItemClickListener {

    CardAdapter adapter;
    public List<Card> cardList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.card_list);

        CardServices cardServices = new CardServices(getApplicationContext());
        this.cardList = cardServices.getContacts();

        Card userCard = cardServices.getUserCard();



        TextView profileLink = (TextView) findViewById(R.id.profileCardListProfileLink);
        profileLink.setOnClickListener(view -> {
            Intent myIntent = new Intent(CardListActivity.this, ProfileActivity.class);
            myIntent.putExtra("card_id", userCard.getCardID().toString());
            startActivity(myIntent);
        });

        // set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.cardListRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CardAdapter(this, cardList);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

    }
    @Override
    public void onItemClick(View view, int position) {
        Intent myIntent = new Intent(CardListActivity.this, ProfileActivity.class);
        myIntent.putExtra("card_id", this.cardList.get(position).getCardID().toString());
        startActivity(myIntent);
    }




    public void goSettingActivity(View view) {
        Intent myIntent = new Intent(this, SettingActivity.class);
        startActivity(myIntent);
    }
}