package com.example.baste.ippradio;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;

import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import java.io.IOException;


public class MainActivity extends WearableActivity {
    boolean vol2 = true;
    boolean started = false;
    private TextView powiad;
    private TextView info;
    private TextView etykieta_g;
    private TextView t1;
    private TextView t2;
    Button zamknij;
    private Button mStartStopButton;
    private Button play2;
    private Button button2;
    private ProgressBar progressBar;
    private int progressStatus = 0;
    Uri myUri = Uri.parse("http://radio.idzpodprad.pl/get/stream");
   // http://radio.idzpodprad.pl/get/stream


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
// Enables Always-on
        setAmbientEnabled();

        etykieta_g = findViewById(R.id.etykieta_g);
        t1 = findViewById(R.id.t1);
        t2 = findViewById(R.id.t2);
        zamknij = findViewById(R.id.koniec);
        final Button dzwiek = findViewById(R.id.dzwiek);
        mStartStopButton = findViewById(R.id.startstopbtn);
        play2 = findViewById(R.id.play2);
        button2 = findViewById(R.id.button2);
        info = findViewById(R.id.info);
        powiad = findViewById(R.id.powiad);
        progressBar = (ProgressBar) findViewById(R.id.progressBar2);
        etykieta_g.setVisibility(View.INVISIBLE);
        t1.setVisibility(View.INVISIBLE);
        t2.setVisibility(View.INVISIBLE);
        dzwiek.setVisibility(View.INVISIBLE);
        info.setText("Brak połączenia z IPP");
        // mStartStopButton.setEnabled(false);
        mStartStopButton.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
       // progressBar.setEnabled(false);

        Intent myIntent = new Intent(this , MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getService(this, 0, myIntent, 0);



        NotificationCompat.WearableExtender wearableExtender =
                new NotificationCompat.WearableExtender()
                        .setHintShowBackgroundOnly(false);

        Notification notification =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle("IPP Przypominam, że nadajemy ")
                        .setContentText("Nadajemy codziennie\n od poniedziałku do piątku o 13:00\n program Kowalski & Chojecki NA ŻYWO oraz \n wiadomości 18:00 -Kowalski & Chojecki Odpowiadają  \n Zapraszamy! ")
                        .setPriority(Notification.PRIORITY_MAX)
                        .setAutoCancel(true)
                        .setOngoing(true)


                        .extend(new NotificationCompat.WearableExtender()
                                .addAction(new NotificationCompat.Action.Builder(R.drawable.ic_launcher, "Wyczyść powiadomienie", pendingIntent).build())
                                .setContentAction(0))
                        .build();

        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(this);

        int notificationId = 1;

        notificationManager.notify(notificationId, notification);



        final MediaPlayer mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        final Animation animRotate = AnimationUtils.loadAnimation(this, R.anim.anim_rotate);
        final Animation animAlpha = AnimationUtils.loadAnimation(this, R.anim.anim_alpha);
        final Animation animScale = AnimationUtils.loadAnimation(this, R.anim.anim_scale);

        final SeekBar sbVolumeBooster = findViewById(R.id.sbVolumeBooster);
        final AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        assert audioManager != null;
        sbVolumeBooster.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        sbVolumeBooster.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
        sbVolumeBooster.setVisibility(View.INVISIBLE);
        sbVolumeBooster.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar arg0) {


            }

            @Override
            public void onStartTrackingTouch(SeekBar arg0) {

            }

            @Override
            public void onProgressChanged(SeekBar arg0, int progress, boolean arg2) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                        progress, 0);  // 0 can also be changed to AudioManager.FLAG_PLAY_SOUND

            }
        });

        dzwiek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view2) {
                if (vol2) {
                    vol2 = false;
                    view2.startAnimation(animAlpha);

                    sbVolumeBooster.setVisibility(View.VISIBLE);
                    etykieta_g.setVisibility(View.VISIBLE);
                    t1.setVisibility(View.VISIBLE);
                    t2.setVisibility(View.VISIBLE);
                    mStartStopButton.setVisibility(View.INVISIBLE);
                    info.setVisibility(View.INVISIBLE);
                    play2.setVisibility(View.INVISIBLE);
                } else {
                    vol2 = true;

                    view2.startAnimation(animAlpha);
                    sbVolumeBooster.setVisibility(View.INVISIBLE);
                    etykieta_g.setVisibility(View.INVISIBLE);
                    t1.setVisibility(View.INVISIBLE);
                    t2.setVisibility(View.INVISIBLE);
                    mStartStopButton.setVisibility(View.VISIBLE);
                    info.setVisibility(View.VISIBLE);


                }

            }
        });

        zamknij.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(animScale);
                finish();
                ///moveTaskToBack(true);
               // mediaPlayer.reset();
                System.exit(0);
            }
        });


        mStartStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (started) {
                    started = false;
                    view.startAnimation(animScale);
                    view.startAnimation(animAlpha);
                    mediaPlayer.pause();
                    mStartStopButton.setText("START");
                    mStartStopButton.setBackgroundResource(R.drawable.play3);

                } else {
                    view.startAnimation(animScale);
                    view.startAnimation(animAlpha);
                    mediaPlayer.start();
                    started = true;
                    info.setText("Na Żywo:\nMarian mówi jak jest");
                    mStartStopButton.setText("PAUZA");
                    mStartStopButton.setBackgroundResource(R.drawable.stop3);

                }

            }


        });


        play2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                try {
                    mediaPlayer.setDataSource(getApplicationContext(), myUri);

                } catch (IOException e) {

                    e.printStackTrace();

                }
                try {
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.setEnabled(true);


                    mediaPlayer.prepare();
                } catch (IOException e) {
                    info.setText("Idż pod prąd nie nadaje");

                    e.printStackTrace();
                }
                progressBar.setVisibility(View.INVISIBLE);
                progressBar.setEnabled(false);
                mediaPlayer.start();
                started = true;
                info.setText("Na Żywo:\n Marian mówi jak jest");
                mStartStopButton.setText("PAUZA");
                mStartStopButton.setBackgroundResource(R.drawable.stop3);
                play2.setVisibility(View.INVISIBLE);
                mStartStopButton.setVisibility(View.VISIBLE);
                dzwiek.setVisibility(View.VISIBLE);
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {

            }
        });


    }


}
