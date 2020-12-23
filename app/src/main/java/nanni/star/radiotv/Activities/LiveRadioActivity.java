package nanni.star.radiotv.Activities;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import nanni.star.radiotv.R;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;


import static nanni.star.radiotv.R.*;

public class LiveRadioActivity extends AppCompatActivity
{

    private SeekBar  volumeSeekBar;
    private TextView songName;
    private Button  playButton, backButton, repeat, playForword, playBackword,shuffle;
    ImageView audioImage;
    MediaPlayer mediaPlayer ;
    private AudioManager audioManager ;
    boolean started = false;
    ProgressDialog progressDialog;

    private static String imageUrl = "http://nannistar.com/cover/OnAir.jpg";
    public static String streamingURL = "http://217.116.9.142:9117\n";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.live_radio_ativity);

        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo == null || !networkInfo.isConnected() || !networkInfo.isAvailable()){
            try{
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

            }catch (Exception e){
                e.printStackTrace();
            }
        }


        initView();
        iniObjects();
        initListeners();
        setVolumeControlStream(audioManager.STREAM_MUSIC);
        initControls();


    }

    public void iniObjects()
    {
        new DownloadImageTask(audioImage).execute(imageUrl);
        new ParseSongName(songName).execute();
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
    }

    public void initView()
    {

        volumeSeekBar = findViewById(id.Volumebar);

        songName = findViewById(id.songname);

        audioImage = findViewById(id.audioImage);

        playButton = findViewById(id.playButton);
        backButton = findViewById(id.backButton);
        repeat = findViewById(id.repeate);
        playBackword = findViewById(id.playback);
        playForword = findViewById(id.playforword);
        shuffle = findViewById(R.id.shuffle);

    }

    public void initListeners()
    {
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                try {
                    mediaPlayer.setDataSource(streamingURL);
                    mediaPlayer.prepareAsync();

                } catch (IOException e) {
                    e.printStackTrace();
                }catch (IllegalArgumentException e){
                    e.printStackTrace();
                }catch (SecurityException e){
                    e.printStackTrace();
                }catch (IllegalStateException e){
                    e.printStackTrace();
                }
                if (started)
                {
                    mediaPlayer.pause();
                    mediaPlayer.reset();
                   started = false;
                   Toast.makeText(LiveRadioActivity.this,"Streaming Stop...",Toast.LENGTH_SHORT).show();
                    v.setBackgroundResource(drawable.ic_media_play);

                }
                else
                {
                    Toast.makeText(LiveRadioActivity.this,"Connecting... Please Wait!",Toast.LENGTH_LONG).show();
                  mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            mediaPlayer.start();
                        }
                    });
                    v.setBackgroundResource(drawable.ic_media_pause);
                    started = true;
                }
            }
        });


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                finish();
            }
        });

        repeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LiveRadioActivity.this,"You cannot repeat in live streaming",Toast.LENGTH_SHORT).show();
            }
        });
        playBackword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LiveRadioActivity.this,"InValid Action",Toast.LENGTH_SHORT).show();
            }
        });
        playForword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LiveRadioActivity.this,"InValid Action",Toast.LENGTH_SHORT).show();
            }
        });
        shuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LiveRadioActivity.this,"No data Available for Shuffling",Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void initControls() {
        try{
            audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            volumeSeekBar.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
            volumeSeekBar.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));

            volumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,progress,0);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP)
        {
            int index = volumeSeekBar.getProgress();
            volumeSeekBar.setProgress(index+1);
            return true;
        }
        else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)
        {
            int index = volumeSeekBar.getProgress();
            volumeSeekBar.setProgress(index - 1);
            return true;
        }
        else if (keyCode == KeyEvent.KEYCODE_BACK){
            mediaPlayer.stop();
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}

class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {


    ImageView bmImage;
    public DownloadImageTask(ImageView bmImage) {
        this.bmImage = bmImage;

    }

    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mIcon11;
    }

    protected void onPostExecute(Bitmap result) {
        bmImage.setImageBitmap(result);

    }


}

class ParseSongName extends AsyncTask<String, Void, String> {


    TextView songText;
    String temp="";

    public ParseSongName(TextView songText) {
        this.songText = songText;
    }

    protected String doInBackground(String... urls) {
        try {
            URL oracle = new URL("http://217.116.9.142:9117/currentsong?sid=1");
            URLConnection yc = oracle.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null)
                temp=temp+inputLine;
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return temp;
    }

    protected void onPostExecute(String result) {
       songText.setText(result);
    }

}

