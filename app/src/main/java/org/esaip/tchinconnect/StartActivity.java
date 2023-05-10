package org.esaip.tchinconnect;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.os.SystemClock;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        View view = this.getWindow().getDecorView();
        view.setBackgroundColor(getResources().getColor(R.color.tchin_green));


        findViewById(R.id.startBackground).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(StartActivity.this, CreateAccountActivity.class);
                //myIntent.putExtra("key", "value"); //Optional parameters
                startActivity(myIntent);
            }
        });

    }
}