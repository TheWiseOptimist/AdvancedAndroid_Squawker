package android.example.com.squawker.fcm;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

// TODO completed (1) Make a new Service in the fcm package that extends from FirebaseMessagingService.

public class SquawkFirebaseMessagingService extends FirebaseMessagingService {

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
        if (remoteMessage.getData().size() > 0) {
            Map<String, String> data = remoteMessage.getData();
            String author=data.get("author");
            String authorKey=data.get("authorKey");
            String message=data.get("message");
            String date=data.get("date");
        }

    }

    // TODO (3) As part of the new Service - If there is message data, get the data using
    // the keys and do two things with it :
    // 1. Display a notification with the first 30 character of the message
    // 2. Use the content provider to insert a new message into the local database
    // Hint: You shouldn't be doing content provider operations on the main thread.
    // If you don't know how to make notifications or interact with a content provider
    // look at the notes in the classroom for help.


    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.e("NEW_TOKEN", s);
    }

}
