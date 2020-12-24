package raymondhernandez.pocketuniv.Activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import raymondhernandez.pocketuniv.Model.Student;
import raymondhernandez.pocketuniv.R;
import raymondhernandez.pocketuniv.Utils.Constants;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends LoginBaseActivity {

    private static final String LOG_TAG = LoginActivity.class.getSimpleName();
    /* A dialog that is presented until the Firebase authentication finished. */
    private ProgressDialog mAuthProgressDialog;
    /* References to the Firebase */
    private Firebase mFirebaseRef;
    /* Listener for Firebase session changes */
    private Firebase.AuthStateListener mAuthStateListener;
    private EditText mEmailInput, mPasswordInput;


    private SharedPreferences mSharedPref;
    private SharedPreferences.Editor mSharedPrefEditor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        mSharedPrefEditor = mSharedPref.edit();

        /**
         * Create Firebase references
         */
        mFirebaseRef = new Firebase(Constants.FIREBASE_URL);
        mAuthStateListener = new Firebase.AuthStateListener() {
            @Override
            public void onAuthStateChanged(AuthData authData) {
                //mAuthProgressDialog.dismiss();

                /**
                 * If there is a valid session to be restored, start MainActivity.
                 * No need to pass data via SharedPreferences because app
                 * already holds userName/provider data from the latest session
                 */
                if (authData != null) {
                    Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
            }
        };
        /* Add auth listener to Firebase ref */
        mFirebaseRef.addAuthStateListener(mAuthStateListener);

        setContentView(R.layout.activity_login);

        /**
         * Link layout elements from XML and setup progress dialog
         */
        initializeScreen();

        mPasswordInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {

                if (actionId == EditorInfo.IME_ACTION_DONE || keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    signInPassword();
                }
                return true;
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        /**
         * This is the authentication listener that maintains the current user session
         * and signs in automatically on application launch
         */
        mAuthStateListener = new Firebase.AuthStateListener() {
            @Override
            public void onAuthStateChanged(AuthData authData) {
                mAuthProgressDialog.dismiss();

                /**
                 * If there is a valid session to be restored, start MainActivity.
                 * No need to pass data via SharedPreferences because app
                 * already holds userName/provider data from the latest session
                 */
                if (authData != null) {
                    Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
            }
        };
        /* Add auth listener to Firebase ref */
        mFirebaseRef.addAuthStateListener(mAuthStateListener);

        /**
         * Get the newly registered user email if present, use null as default value
         */
        String signupEmail = mSharedPref.getString(Constants.KEY_SIGNUP_EMAIL, null);

        /**
         * Fill in the email editText and remove value from SharedPreferences if email is present
         */
        if (signupEmail != null) {
            mEmailInput.setText(signupEmail);

            /**
             * Clear signupEmail sharedPreferences to make sure that they are used just once
             */
            mSharedPrefEditor.putString(Constants.KEY_SIGNUP_EMAIL, null).apply();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mFirebaseRef.removeAuthStateListener(mAuthStateListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    public void onSignInPressed(View view){
        signInPassword();
    }

    public void signInPassword() {
        String email = mEmailInput.getText().toString();
        String password = mPasswordInput.getText().toString();

        /**
         * If email and password are not empty show progress dialog and try to authenticate
         */
        if (email.equals("")) {
            mEmailInput.setError(getString(R.string.error_cannot_be_empty));
            return;
        }

        if (password.equals("")) {
            mPasswordInput.setError(getString(R.string.error_cannot_be_empty));
            return;
        }
        mAuthProgressDialog.show();
        mFirebaseRef.authWithPassword(email, password, new MyAuthResultHandler(Constants.PASSWORD_PROVIDER));
    }

    public void onSignUpPressed(View view){
        Intent intent = new Intent(LoginActivity.this,CreateAccountActivity.class);
        startActivity(intent);
    }

    public void initializeScreen(){

        mEmailInput = (EditText)findViewById(R.id.login_email);
        mPasswordInput = (EditText)findViewById(R.id.login_password);

        mAuthProgressDialog = new ProgressDialog(this);
        mAuthProgressDialog.setTitle(getString(R.string.progress_dialog_loading));
        mAuthProgressDialog.setMessage(getString(R.string.progress_dialog_authenticating_with_firebase));
        mAuthProgressDialog.setCancelable(false);

    }

    private class MyAuthResultHandler implements Firebase.AuthResultHandler {

        private final String provider;

        public MyAuthResultHandler(String provider) {
            this.provider = provider;
        }

        /**
         * On successful authentication call setAuthenticatedUser if it was not already
         * called in
         */
        @Override
        public void onAuthenticated(AuthData authData) {
            mAuthProgressDialog.dismiss();
            Log.i(LOG_TAG, provider + " " + getString(R.string.log_message_auth_successful));

            if (authData != null) {

                if (authData.getProvider().equals(Constants.PASSWORD_PROVIDER)) {


                    setAuthenticatedUserPasswordProvider(authData);

                /* Save provider name and encodedEmail for later use and start MainActivity */
                    mSharedPrefEditor.putString(Constants.KEY_PROVIDER, authData.getProvider()).apply();
                    mSharedPrefEditor.putString(Constants.KEY_ENCODED_EMAIL, mEncodedEmail).apply();

                /* Go to main activity */
                    Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
            }
        }

        @Override
        public void onAuthenticationError(FirebaseError firebaseError) {
            mAuthProgressDialog.dismiss();

            /**
             * Use utility method to check the network connection state
             * Show "No network connection" if there is no connection
             * Show Firebase specific error message otherwise
             */
            switch (firebaseError.getCode()) {
                case FirebaseError.INVALID_EMAIL:
                case FirebaseError.USER_DOES_NOT_EXIST:
                    mEmailInput.setError(getString(R.string.error_message_email_issue));
                    break;
                case FirebaseError.INVALID_PASSWORD:
                    mPasswordInput.setError(firebaseError.getMessage());
                    break;
                case FirebaseError.NETWORK_ERROR:
                    //showErrorToast(getString(R.string.error_message_failed_sign_in_no_network));
                    break;
                default:
                    //showErrorToast(firebaseError.toString());
            }
        }
    }

    /**
     * Helper method that makes sure a user is created if the user
     * logs in with Firebase's email/password provider.
     *
     * @param authData AuthData object returned from onAuthenticated
     */
    private void setAuthenticatedUserPasswordProvider(AuthData authData) {
        final String unprocessedEmail = authData.getProviderData().get(Constants.FIREBASE_PROPERTY_EMAIL).toString().toLowerCase();
        /**
         * Encode user email replacing "." with ","
         * to be able to use it as a Firebase db key
         */
        mEncodedEmail = unprocessedEmail.replace(".",",");

        final Firebase userRef = new Firebase(Constants.FIREBASE_URL_USERS).child(mEncodedEmail);

        /**
         * Check if current user has logged in at least once
         */
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String,Object> student = (HashMap<String, Object>) dataSnapshot.getValue();

                if (student != null) {

                    /**
                     * If recently registered user has hasLoggedInWithPassword = "false"
                     * (never logged in using password provider)
                     */
                    if (!Boolean.getBoolean(student.get("hasLoggedInWithPassword").toString())) {


                        /**
                         * Change password if user that just signed in signed up recently
                         * to make sure that user will be able to use temporary password
                         * from the email more than 24 hours
                         */
                        mFirebaseRef.changePassword(unprocessedEmail, mPasswordInput.getText().toString(), mPasswordInput.getText().toString(), new Firebase.ResultHandler() {
                            @Override
                            public void onSuccess() {
                                userRef.child(Constants.FIREBASE_PROPERTY_USER_HAS_LOGGED_IN_WITH_PASSWORD).setValue(true);
                            }

                            @Override
                            public void onError(FirebaseError firebaseError) {
                                Log.d(LOG_TAG, "" + firebaseError);
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.e(LOG_TAG,"" + firebaseError.getMessage());
            }
        });

    }
}

