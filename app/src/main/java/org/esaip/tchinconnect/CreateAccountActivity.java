package org.esaip.tchinconnect;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.esaip.tchinconnect.databinding.ActivityMainBinding;

public class CreateAccountActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_account_1);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        Button nextButton = (Button) findViewById(R.id.create_account_1_next);
        nextButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Create the text message with a string
                TextInputEditText firstName = (TextInputEditText) findViewById(R.id.firstNameInput);
                TextInputEditText secondName = (TextInputEditText) findViewById(R.id.secondNameInput);
                TextInputEditText email = (TextInputEditText) findViewById(R.id.emailInput);

                setContentView(R.layout.create_account_2);

                //Intent sendIntent = new Intent();
                //sendIntent.setAction(Intent.ACTION_SEND);
                //sendIntent.setType("text/plain");
                //sendIntent.putExtra(Intent.EXTRA_TEXT, firstName.getText());
                //sendIntent.putExtra(Intent.EXTRA_TEXT, secondName.getText());
                //sendIntent.putExtra(Intent.EXTRA_TEXT, email.getText());
                // Start the activity
                //startActivity(sendIntent);

            }
        });

    }

}