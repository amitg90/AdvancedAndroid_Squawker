package android.example.com.squawker.fcm;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.content.ContentValues;
import android.example.com.squawker.R;
import android.example.com.squawker.provider.SquawkContract;
import android.example.com.squawker.provider.SquawkProvider;
import android.os.AsyncTask;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import com.google.firebase.messaging.FirebaseMessagingService;

public class SquawkFirebaseMessageService extends FirebaseMessagingService {
    @SuppressLint("WrongThread")
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        final Map<String, String> map = remoteMessage.getData();
        Log.e("MessagingService", map.toString());

        // test: true
        // author: Ex. "TestAccount"
        // authorKey: Ex. "key_test"
        // message: Ex. "Hello world"
        // date: Ex. 1484358455343

        if (map.containsKey("message") == true) {
            final String message = map.get("message");
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);

            mBuilder.setSmallIcon(R.drawable.ic_duck);
            mBuilder.setContentTitle("Notification Alert, Click Me!");
            mBuilder.setContentText(message);

            NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

            // notificationID allows you to update the notification later on.
            mNotificationManager.notify(1, mBuilder.build());

            // add in local DB using content provider
            // Database operations should not be done on the main thread
            AsyncTask<Void, Void, Void> insertSquawkTask = new AsyncTask<Void, Void, Void>() {

                @Override
                protected Void doInBackground(Void... voids) {
                    ContentValues newMessage = new ContentValues();
                    newMessage.put(SquawkContract.COLUMN_AUTHOR, map.get("author"));
                    newMessage.put(SquawkContract.COLUMN_MESSAGE, message.trim());
                    newMessage.put(SquawkContract.COLUMN_DATE, map.get("date"));
                    newMessage.put(SquawkContract.COLUMN_AUTHOR_KEY, map.get("authorKey"));
                    getContentResolver().insert(SquawkProvider.SquawkMessages.CONTENT_URI, newMessage);
                    Log.e("Messaging Service", "Executing now..");
                    return null;
                }
            };


            insertSquawkTask.execute();
        }
    }
}
