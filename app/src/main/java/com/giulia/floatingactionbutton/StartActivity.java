package com.giulia.floatingactionbutton;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class StartActivity extends AppCompatActivity {

    Button entra;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        entra=(Button) findViewById(R.id.entra);
        entra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent welcome=new Intent(StartActivity.this,WelcomeActivity.class);
                startActivity(welcome);
            }
        });
    }
}

