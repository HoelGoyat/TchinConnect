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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.card_list);

        CardServices cardServices = new CardServices(getApplicationContext());
        ArrayList<Card> cards = (ArrayList<Card>) cardServices.getAllCards();



        TextView profileLink = (TextView) findViewById(R.id.profileCardListProfileLink);
        profileLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(CardListActivity.this, ProfileActivity.class);
                //myIntent.putExtra("key", "value"); //Optional parameters
                startActivity(myIntent);
            }
        });

        // set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.cardListRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CardAdapter(this, cards);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

    }
    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(this, "You clicked " + this.adapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
    }




}