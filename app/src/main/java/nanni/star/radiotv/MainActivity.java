package nanni.star.radiotv;


import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import nanni.star.radiotv.Activities.NewsActivity;
import nanni.star.radiotv.Activities.ContactUsActivity;
import nanni.star.radiotv.Activities.LiveRadioActivity;
import nanni.star.radiotv.Activities.LiveTVActivity;

import nanni.star.radiotv.R;

public class MainActivity extends AppCompatActivity {

    private ImageView liveRadio, live_TV, news, contactus;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        initView();
        initListeners();
    }

   public void initView(){
        liveRadio = findViewById(R.id.Radioimage);
        live_TV = findViewById(R.id.videobroadcasting);
        news = findViewById(R.id.news);
        contactus = findViewById(R.id.contactimage);
    }

    public void initListeners(){
        liveRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, LiveRadioActivity.class));
            }
        });

        live_TV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, LiveTVActivity.class));

            }
        });

        news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, NewsActivity.class));

            }
        });

        contactus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ContactUsActivity.class));

            }
        });
    }
}
