package co.techxpose.firebaseui;

import android.app.Application;
import com.firebase.client.Firebase;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

/**
 * Created by vindu-thind on 12/3/17.
 */

public class Firebaseui extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
if (FirebaseApp.getApps(this).isEmpty()) {
    FirebaseDatabase.getInstance().setPersistenceEnabled(true);
}

    }
}
