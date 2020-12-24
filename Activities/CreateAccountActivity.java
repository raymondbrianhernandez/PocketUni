package raymondhernandez.pocketuniv.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ServerValue;
import com.firebase.client.ValueEventListener;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

import raymondhernandez.pocketuniv.Model.Student;
import raymondhernandez.pocketuniv.R;
import raymondhernandez.pocketuniv.Utils.Constants;

public class CreateAccountActivity extends AppCompatActivity {

    private static final String LOG_TAG = CreateAccountActivity.class.getSimpleName();
    private ProgressDialog mAuthProgressDialog;
    private EditText mEmailEditText, mFirstNameEditText, mLastNameEditText;
    private String mEmailText,mFirstNameText,mLastNameText,mPassword;
    private Firebase mFirebaseRef;
    private SecureRandom mRandom = new SecureRandom();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        mFirebaseRef = new Firebase(Constants.FIREBASE_URL);

        initializeScreen();
    }

    public void initializeScreen(){
        mFirstNameEditText = (EditText) findViewById(R.id.edit_text_create_fname);
        mLastNameEditText = (EditText) findViewById(R.id.edit_text_create_lname);
        mEmailEditText = (EditText) findViewById(R.id.edit_text_create_email);

//        LinearLayout linearLayoutCreateAccount = (LinearLayout)findViewById(R.id.linear_layout_create_account_activity);
//        linearLayoutCreateAccount.setBackgroundResource(R.drawable.bg_create_account);

        /* Setup the progress dialog that is displayed later when authenticating with Firebase */
        mAuthProgressDialog = new ProgressDialog(this);
        mAuthProgressDialog.setTitle(getResources().getString(R.string.progress_dialog_loading));
        mAuthProgressDialog.setMessage(getResources().getString(R.string.progress_dialog_creating_user_with_firebase));
        mAuthProgressDialog.setCancelable(false);
    }

    public void onSignInPressed(View view){
        takeUserToSignInPage();
    }

    public void onCreateAccountPressed(View view){

        /* Get user entered details */
        mEmailText = mEmailEditText.getText().toString().trim();
        mFirstNameText = mFirstNameEditText.getText().toString();
        mLastNameText = mLastNameEditText.getText().toString();
        mPassword = new BigInteger(130, mRandom).toString(32);

        /* Validate */
        if(!isEmailValid(mEmailText)){
            return;
        }

//        if(!mPasswordText.equals(mPasswordCheckText)){
//            mPasswordCheckEditText.setError(getResources().getString(R.string.error_password_not_match));
//            return;
//        }

        mAuthProgressDialog.show();

        /* Create user in Firebase */

        mFirebaseRef.createUser(mEmailText, mPassword, new Firebase.ValueResultHandler<Map<String, Object>>() {
            @Override
            public void onSuccess(final Map<String, Object> result) {
                mAuthProgressDialog.dismiss();
                Log.i(LOG_TAG, "User account created successfully");
                Toast.makeText(CreateAccountActivity.this,"Check your mail inbox for temporary password",Toast.LENGTH_LONG).show();

                mFirebaseRef.resetPassword(mEmailText, new Firebase.ResultHandler() {
                    @Override
                    public void onSuccess() {
                        String uid = (String) result.get("uid");
                        createUserInFirebaseHelper(uid);
                    }

                    @Override
                    public void onError(FirebaseError firebaseError) {
                           /* Error occurred, log the error and dismiss the progress dialog */
                        Log.d(LOG_TAG, "" + firebaseError);
                        mAuthProgressDialog.dismiss();
                    }
                });

                takeUserToSignInPage();
            }

            @Override
            public void onError(FirebaseError firebaseError) {
                mAuthProgressDialog.dismiss();
                if (firebaseError.getCode() == FirebaseError.EMAIL_TAKEN) {
                    mEmailEditText.setError(String.format(getString(R.string.error_email_taken), mEmailText));
                } else {
                    Toast.makeText(CreateAccountActivity.this, firebaseError.getMessage(), Toast.LENGTH_LONG);
                }
            }
        });
    }

    private boolean isEmailValid(String mEmail) {
        boolean isGoodEmail =
                (mEmail != null && android.util.Patterns.EMAIL_ADDRESS.matcher(mEmail).matches());
        if (!isGoodEmail) {
            mEmailEditText.setError(String.format(getString(R.string.error_invalid_email), mEmail));
            return false;
        }
        return isGoodEmail;
    }

//    private boolean isPasswordValid(String mPassword){
//        boolean isGoodPassword = (mPassword!= null && mPassword.length() < 6);
//        return isGoodPassword;
//    }

    public void createUserInFirebaseHelper(final String uid){

        final String encodedEmail = mEmailText.replace(".", ",");
        /**
         * Create the user and uid mapping
         */
        HashMap<String, Object> userAndUidMapping = new HashMap<String, Object>();

        /* Set raw version of date to the ServerValue.TIMESTAMP value and save into dateCreatedMap */
        HashMap<String, Object> timestampJoined = new HashMap<>();
        timestampJoined.put(Constants.FIREBASE_PROPERTY_TIMESTAMP, ServerValue.TIMESTAMP);

        /* Create a HashMap version of the user to add */
        Student newUser = new Student(mFirstNameText,mLastNameText,encodedEmail, timestampJoined);
        HashMap<String, Object> newUserMap = (HashMap<String, Object>)
                new ObjectMapper().convertValue(newUser, Map.class);

        /* Add the user and UID to the update map */
        userAndUidMapping.put("/" + Constants.FIREBASE_LOCATION_USERS + "/" + encodedEmail,
                newUserMap);
        userAndUidMapping.put("/" + Constants.FIREBASE_LOCATION_UID_MAPPINGS + "/"
                + uid, encodedEmail);

        /* Try to update the database; if there is already a user, this will fail */
        mFirebaseRef.updateChildren(userAndUidMapping, new Firebase.CompletionListener() {
            @Override
            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                if (firebaseError != null) {
                    /* Try just making a uid mapping */
                    mFirebaseRef.child(Constants.FIREBASE_LOCATION_UID_MAPPINGS)
                            .child(uid).setValue(encodedEmail);
                }
                /**
                 *  The value has been set or it failed; either way, log out the user since
                 *  they were only logged in with a temp password
                 **/
                mFirebaseRef.unauth();
            }
        });
    }


    public void takeUserToSignInPage(){
        Intent intent = new Intent(CreateAccountActivity.this,LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
