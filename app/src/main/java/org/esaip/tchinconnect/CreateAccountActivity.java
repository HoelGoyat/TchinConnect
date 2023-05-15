package org.esaip.tchinconnect;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.google.android.material.textfield.TextInputEditText;

import org.esaip.tchinconnect.models.Card;
import org.esaip.tchinconnect.models.DAO.AppDatabase;
import org.esaip.tchinconnect.models.DAO.CardDao;
import org.esaip.tchinconnect.models.DAO.UserDao;
import org.esaip.tchinconnect.models.User;

public class CreateAccountActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_account_1);

        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "tchinDB").build();
        UserDao userDao = db.userDao();
        CardDao cardDao = db.cardDao();



        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        Button nextButton = (Button) findViewById(R.id.create_account_1_next);
        TextInputEditText firstName = (TextInputEditText) findViewById(R.id.firstNameInput);
        TextInputEditText secondName = (TextInputEditText) findViewById(R.id.createAccountSecondNameInput);
        TextInputEditText email = (TextInputEditText) findViewById(R.id.createAccountEmailInput);
        TextView profilePictPicker = (TextView) findViewById(R.id.createAccountSelectPicture);
        nextButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Create the text message with a string


                setContentView(R.layout.create_account_2);

                //Intent sendIntent = new Intent();
                //sendIntent.setAction(Intent.ACTION_SEND);
                //sendIntent.setType("text/plain");
                //sendIntent.putExtra(Intent.EXTRA_TEXT, firstName.getText());
                //sendIntent.putExtra(Intent.EXTRA_TEXT, secondName.getText());
                //sendIntent.putExtra(Intent.EXTRA_TEXT, email.getText());
                // Start the activity
                //startActivity(sendIntent);

                Button finishButton = (Button) findViewById(R.id.create_account_2_finish);
                EditText jobTitle = (EditText) findViewById(R.id.jobTitleInput);
                EditText jobDescr = (EditText) findViewById(R.id.jobDescr);


                String strFName = String.valueOf(firstName.getText());
                String strLName = String.valueOf(secondName.getText());
                String strEmail = String.valueOf(email.getText());
                String strPictu = "place_holder";

                finishButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //second layout inputs
                        String strJobTitle = String.valueOf(jobTitle.getText());
                        String strJobDescr = String.valueOf(jobDescr.getText());


                        Card mainUserCard = new Card();
                        User mainUser = new User();

                        mainUser.setPersonalCardId(mainUserCard.getUserID());

                        mainUserCard.setName(strFName);
                        mainUserCard.setSurname(strLName);
                        mainUserCard.setEmail(strEmail);
                        mainUserCard.setJob(strJobTitle);
                        mainUserCard.setJobDescription(strJobDescr);
                        mainUserCard.setImage(strPictu);
                        mainUserCard.setUserID(mainUser.getID());


                        AsyncTask.execute(() -> {
                            userDao.insert(mainUser);
                            cardDao.insert(mainUserCard);
                        });

                        Intent myIntent = new Intent(CreateAccountActivity.this, CardListActivity.class);
                        //myIntent.putExtra("key", "value"); //Optional parameters
                        startActivity(myIntent);
                    }
                });



            }
        });


        profilePictPicker.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
            }
        });



    }


}