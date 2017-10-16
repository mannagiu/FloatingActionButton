package com.giulia.floatingactionbutton;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener{
    private ViewPager mPager;
    private int[] layouts={R.layout.first_slide,R.layout.second_slide,R.layout.third_slide};
    private mPagerAdapter mPagerAdapter;
    private LinearLayout Dots_Layout;
    private ImageView[] dots;
    Button next;
    Button skip;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(new PreferenceManager(this).checkPreference())
        {
            loadHome();
        }
        /*
        if(Build.VERSION.SDK_INT>=19)
        {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        else
        {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }*/
        setContentView(R.layout.activity_welcome);
        mPager=(ViewPager) findViewById(R.id.viewpager);
        mPagerAdapter=new mPagerAdapter(layouts,this);
        mPager.setAdapter(mPagerAdapter);

        Dots_Layout=(LinearLayout)findViewById(R.id.dotLayout);
        next=(Button)findViewById(R.id.bnNext);
        skip=(Button)findViewById(R.id.bnSkip);

        next.setOnClickListener(this);
        skip.setOnClickListener(this);



        createDots(0);
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                createDots(position);
                if(position==layouts.length-1)
                {
                    next.setText("START");
                    skip.setVisibility(View.INVISIBLE);
                }
                else
                {

                    skip.setVisibility(View.VISIBLE);
                }




            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }

    private void createDots(int current_position)
    {

        if(Dots_Layout!=null)
            Dots_Layout.removeAllViews();

            dots=new ImageView[layouts.length];
        for(int i=0; i<layouts.length;i++){
            dots[i]=new ImageView(this);
            if(i==current_position)
            {
                dots[i].setImageDrawable(ContextCompat.getDrawable(this,R.drawable.active_dots));

            }
            else
            {
                dots[i].setImageDrawable(ContextCompat.getDrawable(this,R.drawable.default_dots));

            }

            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(4,0,4,0);
            Dots_Layout.addView(dots[i],params);


        }



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bnNext:
                loadNextSlide();

                break;
            case R.id.bnSkip:
                loadHome();
                new PreferenceManager(this).writePreference();
                break;




        }

    }
    private void loadHome(){
        startActivity(new Intent((this),LoadContents.class));
        finish();
    }
    private void loadNextSlide(){

        int nextSlide= mPager.getCurrentItem()+1;
        if(nextSlide<layouts.length)
        {
            mPager.setCurrentItem(nextSlide);
        }
        else
        {   loadHome();
            new PreferenceManager(this).writePreference();
        }

    }




}
