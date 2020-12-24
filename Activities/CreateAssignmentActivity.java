package raymondhernandez.pocketuniv.Activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.util.Calendar;
import java.util.GregorianCalendar;

import raymondhernandez.pocketuniv.Model.Assignment;
import raymondhernandez.pocketuniv.R;
import raymondhernandez.pocketuniv.Utils.Constants;

public class CreateAssignmentActivity extends AppCompatActivity {

    TextView dateText, timeText, assignNameText, assignCourseText, assignNotesText;
    private int mYear, mMonth, mDay, mHour, mMinute;
    String name,course,notes;
    long duedate;
    private Firebase mFirebaseRef, mFirebaseAssignRef;
    String email;
    TimePickerDialog timePickerDialog;
    DatePickerDialog datePickerDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_assignment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        email = intent.getStringExtra("Email");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dateText = (TextView) findViewById(R.id.assign_date);
        timeText = (TextView) findViewById(R.id.assign_time);
        assignNameText = (TextView) findViewById(R.id.assign_name);
        assignCourseText = (TextView) findViewById(R.id.assign_course);
        assignNotesText = (TextView) findViewById(R.id.assign_notes);

        mFirebaseRef = new Firebase(Constants.FIREBASE_URL);
        mFirebaseAssignRef = mFirebaseRef.child(Constants.FIREBASE_LOCATION_ASSIGNMENTS);

        setDateTime();
        dateText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Get Current Date
                datePickerDialog.show();
                return false;
            }
        });

        timeText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                timePickerDialog.show();
                return false;
            }
        });
    }

    public void addAssignment(View view){

        name = assignNameText.getText().toString();
        course = assignCourseText.getText().toString();
        notes = assignNotesText.getText().toString();
        duedate = (new GregorianCalendar(mYear,mMonth,mDay)).getTimeInMillis();
        duedate = duedate + (mHour * 60 * 60 * 10) + (mMinute * 60 * 10);

        final Assignment assignment = new Assignment(name,course,notes,duedate);

        Firebase queryRef = mFirebaseAssignRef.child(email);

        queryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot d: dataSnapshot.getChildren()){
                    if(d.child("name").getValue().equals(name)){
                        return;
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        mFirebaseAssignRef.child(email).push().setValue(assignment);
        Toast.makeText(this,"Assignment added",Toast.LENGTH_SHORT).show();
        super.onBackPressed();
    }

    public void setDateTime(){

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        datePickerDialog = new DatePickerDialog(CreateAssignmentActivity.this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        dateText.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                    }
                }, mYear, mMonth, mDay);

        // Get Current Time
        final Calendar c2 = Calendar.getInstance();
        mHour = c2.get(Calendar.HOUR_OF_DAY);
        mMinute = c2.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        timePickerDialog = new TimePickerDialog(CreateAssignmentActivity.this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {

                        timeText.setText(hourOfDay + ":" + minute);
                    }
                }, mHour, mMinute,false);
    }
}