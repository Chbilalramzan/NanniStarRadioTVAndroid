package nanni.star.radiotv.Activities;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;

import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.VideoView;
import nanni.star.radiotv.R;


public class LiveTVActivity extends AppCompatActivity {

   VideoView videoView;
   Button subscribe;
   String TAG = "nanni-star-radio-tv";
   private static ProgressDialog progressDialog;
   private static final String url = "https://59583158c050f.streamlock.net:443/7042/7042/playlist.m3u8";

   ConnectivityManager connectivityManager;
   NetworkInfo networkInfo;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.live_tv_activity);

        connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo == null || !networkInfo.isConnected() || !networkInfo.isAvailable()) {
            try {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                alertDialog.setTitle("Check Internet");
                alertDialog.setMessage("Internet not available, Cross check your internet connectivity and try again");
                alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
                alertDialog.setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).setCancelable(false).show();

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            initView();
            initListeners();
            progressDialog = ProgressDialog.show(LiveTVActivity.this, "", "Buffering video...", true);
            progressDialog.setCancelable(true);
            playVideo();
        }
    }
    public void initView(){
        videoView = findViewById(R.id.videoView);
        subscribe = findViewById(R.id.subscribe);
    }

    public void initListeners(){
        subscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/channel/UCzBomP-uBt8O2MHVXHaTWCg")));
            }
        });
    }
    public void playVideo()
        {
            try
            {
                getWindow().setFormat(PixelFormat.TRANSLUCENT);
                MediaController mediaController = new MediaController(this);
                mediaController.setAnchorView(videoView);

                Uri uri = Uri.parse(url);
                videoView.setMediaController(mediaController);
                videoView.setVideoURI(uri);

            }catch (Exception e)
            {
                e.printStackTrace();
                progressDialog.dismiss();
            }

            videoView.requestFocus();
            videoView.setSoundEffectsEnabled(true);
            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    videoView.start();
                    progressDialog.dismiss();
                }
            });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            videoView.pause();
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
