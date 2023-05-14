package org.esaip.tchinconnect;

import androidx.appcompat.app.AppCompatActivity;

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



        UserServices userServices = new UserServices(getApplicationContext());
        CardServices cardServices = new CardServices(getApplicationContext());

        User user = userServices.getAllUsers().get(0);

        Card userCard = cardServices.getCardByUserId(user.getID());

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