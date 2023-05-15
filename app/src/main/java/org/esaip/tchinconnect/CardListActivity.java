package org.esaip.tchinconnect;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;

import org.esaip.tchinconnect.R;

public class CardListActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.card_list);

        TextView profileLink = (TextView) findViewById(R.id.profileCardListProfileLink);
        profileLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(CardListActivity.this, ProfileActivity.class);
                //myIntent.putExtra("key", "value"); //Optional parameters
                startActivity(myIntent);
            }
        });




    }

}