package org.esaip.tchinconnect;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import org.esaip.tchinconnect.DBServices.UserServices;
import org.esaip.tchinconnect.models.User;

import java.util.List;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        View view = this.getWindow().getDecorView();
        view.setBackgroundColor(getResources().getColor(R.color.tchin_green));

        UserServices userServices = new UserServices(getApplicationContext());

        List<User> users = userServices.getAllUsers();
        if(users.isEmpty()){
            findViewById(R.id.startBackground).setOnClickListener(new View.OnClickListener() {


                @Override
                public void onClick(View view) {
                    Intent myIntent = new Intent(StartActivity.this, CreateAccountActivity.class);
                    //myIntent.putExtra("key", "value"); //Optional parameters
                    startActivity(myIntent);
                }
            });
        }
        else {
            findViewById(R.id.startBackground).setOnClickListener(new View.OnClickListener() {


                @Override
                public void onClick(View view) {
                    Intent myIntent = new Intent(StartActivity.this, CardListActivity.class);
                    //myIntent.putExtra("key", "value"); //Optional parameters
                    startActivity(myIntent);
                }
            });
        }



    }
}