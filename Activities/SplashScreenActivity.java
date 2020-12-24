package raymondhernandez.pocketuniv.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;

import raymondhernandez.pocketuniv.R;
import raymondhernandez.pocketuniv.Utils.Constants;

public class SplashScreenActivity extends AppCompatActivity {

    private Firebase.AuthStateListener mAuthStateListener;
    private Firebase mFirebaseRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFirebaseRef = new Firebase(Constants.FIREBASE_URL);
        mAuthStateListener = new Firebase.AuthStateListener() {
            @Override
            public void onAuthStateChanged(AuthData authData) {
                /**
                 * If there is a valid session to be restored, start MainActivity.
                 * No need to pass data via SharedPreferences because app
                 * already holds userName/provider data from the latest session
                 */
                if (authData != null) {
                    Intent intent = new Intent(SplashScreenActivity.this,DashboardActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
            }
        };

        mFirebaseRef.addAuthStateListener(mAuthStateListener);

        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
