package android.example.com.squawker.fcm;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.example.com.squawker.MainActivity;
import android.example.com.squawker.R;
import android.example.com.squawker.provider.SquawkContract;
import android.example.com.squawker.provider.SquawkProvider;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

// TODO completed (1) Make a new Service in the fcm package that extends from FirebaseMessagingService.

public class SquawkFirebaseMessageService extends FirebaseMessagingService {

    // TODO completed (2) As part of the new Service - Override onMessageReceived. This method will
    // be triggered whenever a squawk is received. You can get the data from the squawk
    // message using getData(). When you send a test message, this data will include the
    // following key/value pairs:
    // test: true
    // author: Ex. "TestAccount"
    // authorKey: Ex. "key_test"
    // message: Ex. "Hello world"
    // date: Ex. 1484358455343

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Map<String, String> data = remoteMessage.getData();

        // TODO (3) As part of the new Service - If there is message data, get the data using
        // the keys and do two things with it :
        if (data.size() > 0) {
            // 1. Display a notification with the first 30 character of the message
            displayNotification(data);
            // 2. Use the content provider to insert a new message into the local database
            insertSquawk(data);
            // Hint: You shouldn't be doing content provider operations on the main thread.
            // If you don't know how to make notifications or interact with a content provider
            // look at the notes in the classroom for help.

            String author = data.get("author");
            String authorKey = data.get("authorKey");
            String message = data.get("message");
            String date = data.get("date");
        }
    }

    // 3.2
    private void insertSquawk(final Map<String, String> data) {
        AsyncTask<Void, Void, Void> insertSquawkTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                ContentValues newSquawk = new ContentValues();
                newSquawk.put(SquawkContract.COLUMN_AUTHOR, data.get(SquawkContract.COLUMN_AUTHOR));
                newSquawk.put(SquawkContract.COLUMN_AUTHOR_KEY, data.get(SquawkContract.COLUMN_AUTHOR_KEY));
                newSquawk.put(SquawkContract.COLUMN_MESSAGE, data.get(SquawkContract.COLUMN_MESSAGE));
                newSquawk.put(SquawkContract.COLUMN_DATE, data.get(SquawkContract.COLUMN_DATE));
                getContentResolver().insert(SquawkProvider.SquawkMessages.CONTENT_URI, newSquawk);

                return null;
            }
        };
        insertSquawkTask.execute();
    }

    // 3.1
    private void displayNotification(Map<String, String> data) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent =
                PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        String shortenedMessage = data.get(SquawkContract.COLUMN_MESSAGE);
        shortenedMessage =
                shortenedMessage.substring(0, Math.min(shortenedMessage.length(), 30));

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String channelId = "Squawker";
        CharSequence channelName = channelId;

        Notification notification = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel nc = new NotificationChannel(channelId, channelName, importance);
            nc.setDescription(channelId);
            nc.enableLights(true);
            nc.setLightColor(Color.YELLOW);
            nc.setVibrationPattern(new long[]{100, 200, 300, 400, 500});
            nc.enableVibration(true);


            notification = new Notification.Builder(this, channelId)
                    .setContentTitle(data.get(SquawkContract.COLUMN_AUTHOR))
                    .setContentText(shortenedMessage)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setAutoCancel(true)
//                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setContentIntent(pendingIntent)
                    .build();
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(nc);
                notificationManager.notify(123, notification);
                Log.e("NOTIFICATION", "API 26+ (" + android.os.Build.VERSION.SDK_INT + ")");
            }
        } else {
            notification = new NotificationCompat.Builder(this, channelId)
                    .setContentTitle(data.get(SquawkContract.COLUMN_AUTHOR))
                    .setContentText(shortenedMessage)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .build();
            if (notificationManager != null) {
                notificationManager.notify(123, notification);
                Log.e("NOTIFICATION", "API 25- (" + android.os.Build.VERSION.SDK_INT + ")");
            }
        }
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.e("NEW_TOKEN", s);
    }

}
