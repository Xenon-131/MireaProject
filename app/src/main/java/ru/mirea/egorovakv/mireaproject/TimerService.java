package ru.mirea.egorovakv.mireaproject;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class TimerService extends Service {
    private MediaPlayer mediaPlayer;
    Thread playerThread;
    private int maxDuration;
    private int timeMultiplier = 1000;
    private String focus;
    private String rest;
    private String session;
    public static final String CHANNEL_ID = "TimerForegroundServiceChannel";
    public TimerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        focus  = intent.getStringExtra("focus duration");
        rest = intent.getStringExtra("rest duration");
        session = intent.getStringExtra("session duration");
        maxDuration = Integer.parseInt(session);

        playerThread = new Thread(new Runnable() {
            public void run() {
                long endTime = System.currentTimeMillis() + maxDuration*timeMultiplier;
                while(System.currentTimeMillis() < endTime){
                    long periodEndTime = System.currentTimeMillis() + Integer.parseInt(focus) *timeMultiplier;
                    if (mediaPlayer!= null) {
                        mediaPlayer.start();
                    }
                    while (System.currentTimeMillis() < periodEndTime) {
                        synchronized (this) {
                            try {
                                wait(periodEndTime - System.currentTimeMillis());
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                    periodEndTime = System.currentTimeMillis() + Integer.parseInt(rest) *timeMultiplier;
                    if (mediaPlayer!= null) {
                        mediaPlayer.pause();
                    }
                    while (System.currentTimeMillis() < periodEndTime) {
                        synchronized (this) {
                            try {
                                wait(periodEndTime - System.currentTimeMillis());
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                }

            }

        });
        playerThread.start();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                stopForeground(true);
            }
        });
        return super.onStartCommand(intent, flags, startId);
    }
    @Override
    public void onCreate() {
        super.onCreate();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentText("Timer")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("FocusTimer"))
                .setContentTitle("Timer");
        int importance = NotificationManager.IMPORTANCE_DEFAULT;

        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Timer Notification", importance);

        channel.setDescription("MIREA Channel");
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.createNotificationChannel(channel);
        startForeground(1, builder.build());



        mediaPlayer= MediaPlayer.create(this, R.raw.sumeru);
        mediaPlayer.setLooping(false);
    }
    @Override
    public void onDestroy() {
        stopForeground(true);
        //playerThread.interrupt();
//        mediaPlayer.stop();
//        mediaPlayer.prepareAsync();
    }
}