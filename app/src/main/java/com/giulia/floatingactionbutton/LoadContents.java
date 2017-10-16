package com.giulia.floatingactionbutton;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class LoadContents extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_contents);


    }


    public void loadSlides(View view)
    {
        new PreferenceManager(this).clearPreference();
        startActivity(new Intent(this,WelcomeActivity.class));
        finish();
    }

    public void loadFirstPage(View view){
        new PreferenceManager(this).clearPreference();
        startActivity(new Intent(this,SelectFileActivity.class));
        finish();

    }


}
