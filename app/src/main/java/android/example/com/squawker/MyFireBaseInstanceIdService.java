package android.example.com.squawker;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyFireBaseInstanceIdService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        String myToken = FirebaseInstanceId.getInstance().getToken();

        Log.e("MyFireBaseInstanceId", myToken);
    }
}
