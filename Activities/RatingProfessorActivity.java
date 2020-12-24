package raymondhernandez.pocketuniv.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.HashMap;

import raymondhernandez.pocketuniv.R;
import raymondhernandez.pocketuniv.Utils.Constants;

public class RatingProfessorActivity extends AppCompatActivity {

    public double knowledge2, involvement2, grading2,accessibility2,enthusiastic2,communication2;
    public TextView enthuTextView,accessTextView,commTextView,knowTextView,invlvTextView,gradeTextView;
    public AppCompatSeekBar enthuSeekBar,accessSeekBar,commSeekBar,knowSeekBar,invlvSeekBar,gradeSeekBar;
    public AppCompatSpinner courseSpinner;
    private String[] Courses;
    private Intent intent;
    public final Firebase firebaseRef = new Firebase(Constants.FIREBASE_URL);

    private int knowledge, involvement, grading,accessibility,enthusiastic,communication;
    private String course;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating_professor);
        intent = getIntent();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_rating_prof);
        toolbar.setTitle(intent.getStringExtra("Name"));
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Courses = intent.getStringArrayExtra("Courses");
        intializeScreen();
    }

    public void intializeScreen() {
        accessTextView = (TextView) findViewById(R.id.access_text);
        enthuTextView = (TextView) findViewById(R.id.enthu_text);
        commTextView = (TextView) findViewById(R.id.communication_text);
        knowTextView = (TextView) findViewById(R.id.knowledge_text);
        invlvTextView = (TextView) findViewById(R.id.involvement_text);
        gradeTextView = (TextView) findViewById(R.id.grading_text);

        courseSpinner = (AppCompatSpinner) findViewById(R.id.course_spinner);

        knowSeekBar = (AppCompatSeekBar) findViewById(R.id.knowledge_seekbar);
        invlvSeekBar = (AppCompatSeekBar) findViewById(R.id.involvement_seekbar);
        gradeSeekBar = (AppCompatSeekBar) findViewById(R.id.grading_seekbar);
        accessSeekBar = (AppCompatSeekBar) findViewById(R.id.access_seekbar);
        enthuSeekBar = (AppCompatSeekBar) findViewById(R.id.enthu_seekbar);
        commSeekBar = (AppCompatSeekBar) findViewById(R.id.communication_seekbar);

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, Courses);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        courseSpinner.setAdapter(spinnerAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        courseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                course = courseSpinner.getSelectedItem().toString();
                handleSeekbars();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public void handleSeekbars(){

        knowSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            int progress = 0;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
                progress = progressValue;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {        }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                knowTextView.setText(getString(R.string.Knowledge_text,progress));
                knowledge = progress;
            }
        });

        invlvSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            int progress = 0;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
                progress = progressValue;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {        }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                invlvTextView.setText(getString(R.string.Involve_text,progress));
                involvement = progress;
            }
        });

        gradeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            int progress = 0;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
                progress = progressValue;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {        }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                gradeTextView.setText(getString(R.string.Grading_text,progress));
                grading = progress;
            }
        });
        accessSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            int progress = 0;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
                progress = progressValue;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {        }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                accessTextView.setText(getString(R.string.accessibility_text,progress));
                accessibility = progress;
            }
        });

        enthuSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            int progress = 0;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
                progress = progressValue;
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {        }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                enthuTextView.setText(getString(R.string.Enthusiasm_text,progress));
                enthusiastic = progress;
            }
        });

        commSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            int progress = 0;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
                progress = progressValue;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                commTextView.setText(getString(R.string.Communication_text,progress));
                communication = progress;
            }
        });
    }

    public void submitRatings(View view){
        final String profEmail = intent.getStringExtra("Email").replace(".", ",");
        final Firebase ratingsRef = firebaseRef.child(Constants.FIREBASE_LOCATION_RATINGS);
        final Firebase profRatingRef = ratingsRef.child(Constants.FIREBASE_LOCATION_RATINGS_PROFESSORS);
        final String studentEmail = firebaseRef.getAuth().getProviderData().get("email").toString();
        final String studentEncodeEmail = studentEmail.replace(".", ",");

        final HashMap<String,Object> ratingMap = new HashMap<>();
        HashMap<String,Object> courseRateMap = new HashMap<>();
        courseRateMap.put("Accessibilty", accessibility);
        courseRateMap.put("Enthusiastic", enthusiastic);
        courseRateMap.put("Communication", communication);
        courseRateMap.put("Knowledge", knowledge);
        courseRateMap.put("Involvement", involvement);
        courseRateMap.put("Grading", grading);

        ratingMap.put(studentEncodeEmail, courseRateMap);

        profRatingRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String, Object> ratings = (HashMap<String, Object>) dataSnapshot.getValue();
                if(ratings == null){
                    profRatingRef.child(profEmail).child(course).setValue(ratingMap);
                }else {
                    Firebase profRef = profRatingRef.child(profEmail);
                    if (ratings.containsKey(profEmail)) {
                        profRef.child(course).updateChildren(ratingMap);
                    } else {
                        profRef.child(course).setValue(ratingMap);
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        Toast.makeText(this,"Ratings Submitted to database",Toast.LENGTH_LONG).show();
        //test();
        super.onBackPressed();
    }
}
