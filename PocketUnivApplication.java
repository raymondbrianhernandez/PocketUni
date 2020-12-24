package raymondhernandez.pocketuniv;

import android.app.Application;

import com.firebase.client.Firebase;

/**
 * Created 3/26/2016.
 */
public class PocketUnivApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
        Firebase.getDefaultConfig().setPersistenceEnabled(true);
    }
}
