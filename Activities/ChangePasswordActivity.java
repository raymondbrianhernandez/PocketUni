package raymondhernandez.pocketuniv.Activities;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import raymondhernandez.pocketuniv.Utils.Constants;
import raymondhernandez.pocketuniv.Model.Student;
import raymondhernandez.pocketuniv.R;

public class ChangePasswordActivity extends AppCompatActivity {

    private static final String LOG_TAG = ChangePasswordActivity.class.getSimpleName();

    public Firebase mFirebaseRef;
    public TextInputEditText currentPasswordEditText,newPasswordEditText,confirmNewPasswordEditText;
    String currentPswd,newPswd,confirmNewPswd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        mFirebaseRef = new Firebase(raymondhernandez.pocketuniv.Utils.Constants.FIREBASE_URL);

        currentPasswordEditText = (TextInputEditText)findViewById(R.id.edit_text_current_password);
        newPasswordEditText = (TextInputEditText)findViewById(R.id.edit_text_newPassword);
        confirmNewPasswordEditText = (TextInputEditText)findViewById(R.id.edit_text_confirm_new_password);

    }

    public void onChangePasswordPressed(View view){

        currentPswd = currentPasswordEditText.getText().toString();
        newPswd =  newPasswordEditText.getText().toString();
        confirmNewPswd = confirmNewPasswordEditText.getText().toString();

        if(newPswd.equals(confirmNewPswd) && currentPswd!= ""){

            final String unprocessedEmail = mFirebaseRef.getAuth().getProviderData().get(Constants.FIREBASE_PROPERTY_EMAIL).toString().toLowerCase();

            String EncodedEmail = unprocessedEmail.replace(".",",");

            final Firebase userRef = new Firebase(Constants.FIREBASE_URL_USERS).child(EncodedEmail);

            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Student student = dataSnapshot.getValue(Student.class);

                    if (student != null) {
                            mFirebaseRef.changePassword(unprocessedEmail, currentPswd, confirmNewPswd, new Firebase.ResultHandler() {
                                @Override
                                public void onSuccess() {
                                    Toast.makeText(ChangePasswordActivity.this,
                                            getResources().getString(R.string.password_changed_successfully),
                                            Toast.LENGTH_SHORT).show();
                                    userRef.child(Constants.FIREBASE_PROPERTY_USER_HAS_LOGGED_IN_WITH_PASSWORD).setValue(true);
                                        /* The password was changed */
                                    Log.d(LOG_TAG, getString(R.string.log_message_password_changed_successfully) + confirmNewPswd);
                                    mFirebaseRef.unauth();
                                    Intent intent = new Intent(ChangePasswordActivity.this,LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                }

                                @Override
                                public void onError(FirebaseError firebaseError) {
                                    Log.d(LOG_TAG, "" + firebaseError);
                                }
                            });
                        }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    Log.e(LOG_TAG,"" + firebaseError.getMessage());
                }
            });
        }
    }
}
