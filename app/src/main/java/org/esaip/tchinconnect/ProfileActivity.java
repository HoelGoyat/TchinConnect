package org.esaip.tchinconnect;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import org.esaip.tchinconnect.DBServices.CardServices;
import org.esaip.tchinconnect.DBServices.UserServices;
import org.esaip.tchinconnect.models.Card;
import org.esaip.tchinconnect.models.User;

import java.util.List;
import java.util.UUID;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        getSupportActionBar().hide();

        Intent intent = getIntent();

        UUID.fromString(intent.getStringExtra("card_id").toString());


        CardServices cardServices = new CardServices(getApplicationContext());


        Card userCard = cardServices.getCardById(UUID.fromString(intent.getStringExtra("card_id")));

        TextView userNames = (TextView) findViewById(R.id.profileUserNames);
        userNames.setText(userCard.getName() +" "+ userCard.getSurname().toUpperCase());

        TextView email = (TextView) findViewById(R.id.profileEmail);
        email.setText(userCard.getEmail());

        TextView jobTitle = (TextView) findViewById(R.id.profileJobTitle);
        jobTitle.setText(userCard.getJob());

        TextView jobDesc = (TextView) findViewById(R.id.profileJobDescr);
        jobDesc.setText(userCard.getJobDescription());


    }
}