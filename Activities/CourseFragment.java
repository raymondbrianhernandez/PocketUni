package raymondhernandez.pocketuniv.Activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.HashMap;

import raymondhernandez.pocketuniv.R;
import raymondhernandez.pocketuniv.Utils.Constants;

/**
 * Created by Chachi on 4/16/2016.
 */
public class CourseFragment extends DialogFragment {
    public Firebase courseRef;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        courseRef = new Firebase(Constants.FIREBASE_URL_COURSES);
    }

    public static CourseFragment newInstance(){
        CourseFragment courseFragment = new CourseFragment();
        return courseFragment;
    }

    public CourseFragment() {
    }

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
        View rootView = inflater.inflate(R.layout.contribute_course, null);

        final EditText codeEditText = (EditText)rootView.findViewById(R.id.course_code);
        final EditText nameEditText = (EditText)rootView.findViewById(R.id.course_name);
        final EditText majorEditText = (EditText)rootView.findViewById(R.id.course_major);

        builder.setView(rootView)
                /* Add action buttons */
                .setPositiveButton(R.string.submit_data, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        addCourse(codeEditText.getText().toString(),nameEditText.getText().toString(),
                                majorEditText.getText().toString());
                    }
                });
        return builder.create();
    }

    public void addCourse(final String code, String name, String major) {
        final HashMap<String,Object> courseMap = new HashMap<>();
        courseMap.put("Name",name);
        courseMap.put("Code",code);
        courseMap.put("Major",major);

        courseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String, Object> courses = (HashMap<String, Object>) dataSnapshot.getValue();
                if(courses == null){
                    courseRef.child(code).setValue(courseMap);
                }else {
                    if (courses.containsKey(code)) {
                    } else {
                        courseRef.child(code).setValue(courseMap);
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }
}
