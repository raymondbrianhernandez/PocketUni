package raymondhernandez.pocketuniv.Activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.HashMap;
import java.util.Objects;

import raymondhernandez.pocketuniv.Model.Professor;
import raymondhernandez.pocketuniv.R;
import raymondhernandez.pocketuniv.Utils.Constants;

/**
 * Created by Chachi on 4/14/2016.
 */
public class ProfessorFragment extends DialogFragment {

    public Firebase profRef;
    public EditText firstNameEditText,lastNameEditText,emailEditText,phoneEditText,deptEditText;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        profRef = new Firebase(Constants.FIREBASE_URL_PROFESSORS);
    }

    public static ProfessorFragment newInstance(){
        ProfessorFragment professorFragment = new ProfessorFragment();
        return professorFragment;
    }

    public ProfessorFragment() {
    }
    /**
     * Open the keyboard automatically when the dialog fragment is opened
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    @Nullable
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View rootView = inflater.inflate(R.layout.contribute_professor, null);

        firstNameEditText = (EditText)rootView.findViewById(R.id.prof_fname_editText);
        lastNameEditText = (EditText)rootView.findViewById(R.id.prof_lname_editText);
        emailEditText = (EditText)rootView.findViewById(R.id.prof_email_editText);
        deptEditText = (EditText)rootView.findViewById(R.id.prof_dept_editText);
        phoneEditText = (EditText)rootView.findViewById(R.id.prof_phone_editText);

        builder.setView(rootView)
                /* Add action buttons */
                .setPositiveButton(R.string.submit_data, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        addProfessor(firstNameEditText.getText().toString(),lastNameEditText.getText().toString(),
                                emailEditText.getText().toString(),deptEditText.getText().toString(),
                                phoneEditText.getText().toString());
                    }
                });
        return builder.create();
    }

    public void addProfessor(String fn,String ln,String email,String dept,String phone) {
        final HashMap<String,Object> profMap = new HashMap<>();
        profMap.put("firstName",fn);
        profMap.put("lastName",ln);
        profMap.put("email",email);
        profMap.put("department",dept);
        profMap.put("Phone",Long.valueOf(phone).longValue());

        final String encodedEmail = email.replace(".",",");

        profRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String, Object> professors = (HashMap<String, Object>) dataSnapshot.getValue();
                if(professors.containsKey(encodedEmail)){

                }else{
                    profRef.child(encodedEmail).setValue(profMap);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }
}
