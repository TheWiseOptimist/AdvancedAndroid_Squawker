package android.example.com.squawker;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;

public class SquawkFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = SquawkFirebaseMessagingService.class.getSimpleName();

    @Override
    public void onNewToken(String token) {
//        super.onNewToken(s);
        Log.d(TAG, "Refreshed Token: " + token);
        sendRegistrationToServer(token);
    }

    private void sendRegistrationToServer(String token) {
        // TODO: 9/27/18 not currently implemented
    }
}
